package org.ctu.fee.a4m39wa2.chalupa.chat.api.resources;

import org.ctu.fee.a4m39wa2.chalupa.chat.api.SelectionContext;
import org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.UserDto;
import org.ctu.fee.a4m39wa2.chalupa.chat.api.exceptions.DuplicateKeyValueException;
import org.ctu.fee.a4m39wa2.chalupa.chat.api.exceptions.RequestedEntityNotFoundException;
import org.ctu.fee.a4m39wa2.chalupa.chat.api.filters.selectable.Field;
import org.ctu.fee.a4m39wa2.chalupa.chat.api.filters.selectable.Selectable;
import org.ctu.fee.a4m39wa2.chalupa.chat.logic.UserCrud;
import org.ctu.fee.a4m39wa2.chalupa.chat.logic.exceptions.UniqueConstraintViolationException;
import org.ctu.fee.a4m39wa2.chalupa.chat.model.User;
import org.ctu.fee.a4m39wa2.chalupa.chat.security.Authenticated;
import org.ctu.fee.a4m39wa2.chalupa.chat.security.BusinessRole;
import org.ctu.fee.a4m39wa2.chalupa.chat.security.Secured;
import org.ctu.fee.a4m39wa2.chalupa.chat.security.SecurityContext;
import org.ctu.fee.a4m39wa2.chalupa.chat.validation.group.Create;
import org.ctu.fee.a4m39wa2.chalupa.chat.validation.group.Update;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

import ma.glasnost.orika.MapperFacade;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private UserCrud userCrud;

    @Inject
    private MapperFacade mapperFacade;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private SelectionContext selectionContext;

    @Secured(BusinessRole.ADMIN)
    @GET
    @Selectable(limit = 5, fields = {@Field("username")})
    public Response getAllUsers() {
        final List<User> users = userCrud.findAll(selectionContext);
        final List<UserDto> userDtos = mapperFacade.mapAsList(users, UserDto.class);

        final GenericEntity<List<UserDto>> result = new GenericEntity<List<UserDto>>(userDtos) {};
        return Response.ok(result).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid @ConvertGroup(from = Default.class, to = Create.class) @NotNull UserDto userDto) {
        try {
            final User user = userCrud.create(userDto.getUsername(), userDto.getPassword());
            return Response.created(createGetUserUri(user.getId())).build();
        } catch (UniqueConstraintViolationException e) {
            throw new DuplicateKeyValueException(e.getField());
        }

    }

    @Authenticated
    @PUT
    @Path("/{id : \\d+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeUserPassword(
            @PathParam("id") long id,
            @Valid @ConvertGroup(from = Default.class, to = Update.class) @NotNull UserDto userDto) {

        if (!securityContext.getUser().getId().equals(id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        userCrud.changePassword(securityContext.getUser(), userDto.getPassword());
        return Response.noContent().build();
    }

    @Secured(BusinessRole.ADMIN)
    @GET
    @Path("/{id : \\d+}")
    public Response getUser(@PathParam("id") long id) {
        final User user = userCrud.find(id);
        if (user == null) {
            throw new RequestedEntityNotFoundException(createGetUserUri(id));
        }
        final UserDto result = mapperFacade.map(user, UserDto.class);
        return Response.ok(result).build();
    }

    @Secured(BusinessRole.ADMIN)
    @GET
    @Path("/{id : \\d+}/roles")
    public Response getUserRoles(@PathParam("id") long id) {
        final User user = userCrud.find(id, true);
        if (user == null) {
            throw new RequestedEntityNotFoundException(createGetUserUri(id));
        }

        final GenericEntity<List<BusinessRole>> result = new GenericEntity<List<BusinessRole>>(user.getBusinessRoles()) {};
        return Response.ok(result).build();
    }

    private URI createGetUserUri(Long id) {
        final UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
        return uriBuilder.path(UserResource.class).path("/{id : \\d+}").build(id);
    }
}
