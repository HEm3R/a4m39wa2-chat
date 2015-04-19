package org.ctu.fee.a4m39wa2.chalupa.chat.api.resources;

import org.ctu.fee.a4m39wa2.chalupa.chat.api.SelectionContext;
import org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.RoomDto;
import org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error.MessageDto;
import org.ctu.fee.a4m39wa2.chalupa.chat.api.exceptions.DuplicateKeyValueException;
import org.ctu.fee.a4m39wa2.chalupa.chat.api.exceptions.RequestedEntityNotFoundException;
import org.ctu.fee.a4m39wa2.chalupa.chat.api.filters.selectable.Field;
import org.ctu.fee.a4m39wa2.chalupa.chat.api.filters.selectable.Selectable;
import org.ctu.fee.a4m39wa2.chalupa.chat.logic.MessageCrud;
import org.ctu.fee.a4m39wa2.chalupa.chat.logic.RoomCrud;
import org.ctu.fee.a4m39wa2.chalupa.chat.logic.RoomLogGenerator;
import org.ctu.fee.a4m39wa2.chalupa.chat.logic.exceptions.UniqueConstraintViolationException;
import org.ctu.fee.a4m39wa2.chalupa.chat.model.Message;
import org.ctu.fee.a4m39wa2.chalupa.chat.model.Room;
import org.ctu.fee.a4m39wa2.chalupa.chat.security.Authenticated;
import org.ctu.fee.a4m39wa2.chalupa.chat.access.BusinessRole;
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
import javax.ws.rs.DELETE;
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

@Authenticated
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
public class RoomResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private RoomCrud roomCrud;

    @Inject
    private MessageCrud messageCrud;

    @Inject
    private RoomLogGenerator logGenerator;

    @Inject
    private MapperFacade mapperFacade;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private SelectionContext selectionContext;

    @GET
    @Selectable(limit = 5, fields = {@Field("name")})
    public Response getAllRooms() {
        final List<Room> rooms = roomCrud.findAll(selectionContext);
        final List<RoomDto> roomDtos = mapperFacade.mapAsList(rooms, RoomDto.class);

        final GenericEntity<List<RoomDto>> result = new GenericEntity<List<RoomDto>>(roomDtos) {};
        return Response.ok(result).build();
    }

    @Secured(BusinessRole.ADMIN)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRoom(@Valid @ConvertGroup(from = Default.class, to = Create.class) @NotNull RoomDto roomDto) {
        try {
            final Room room = roomCrud.create(roomDto.getName());
            return Response.created(createGetRoomUri(room.getId())).build();
        } catch (UniqueConstraintViolationException e) {
            throw new DuplicateKeyValueException(e.getField());
        }

    }

    @Secured(BusinessRole.ADMIN)
    @PUT
    @Path("/{id : \\d+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeRoomName(
            @PathParam("id") long id,
            @Valid @ConvertGroup(from = Default.class, to = Update.class) @NotNull RoomDto roomDto) {

        Room room = findRoom(id);

        try {
            roomCrud.changeName(room, roomDto.getName());
        } catch (UniqueConstraintViolationException e) {
            throw new DuplicateKeyValueException(e.getField());
        }

        return Response.noContent().build();
    }

    @GET
    @Path("/{id : \\d+}")
    public Response getRoom(@PathParam("id") long id) {
        Room room = findRoom(id);
        final RoomDto result = mapperFacade.map(room, RoomDto.class);
        return Response.ok(result).build();
    }

    @Secured(BusinessRole.ADMIN)
    @DELETE
    @Path("/{id : \\d+}")
    public Response deleteRoom(@PathParam("id") long id) {
        roomCrud.remove(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id : \\d+}/messages")
    @Selectable(limit = 20, fields = {@Field(value = "authorId", entityField = "user")})
    public Response getMessages(@PathParam("id") long id) {
        Room room = findRoom(id);

        final List<Message> messages = messageCrud.findAllByRoom(room, selectionContext);
        final List<MessageDto> messageDtos = mapperFacade.mapAsList(messages, MessageDto.class);

        final GenericEntity<List<MessageDto>> result = new GenericEntity<List<MessageDto>>(messageDtos) {};
        return Response.ok(result).build();
    }

    @POST
    @Path("/{id : \\d+}/messages")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMessage(@PathParam("id") long id, @Valid @NotNull MessageDto messageDto) {
        Room room = findRoom(id);
        Message message = messageCrud.create(room, messageDto.getText());

        return Response.created(createGetMessageUri(room.getId(), message.getId())).build();
    }

    @GET
    @Path("/{id : \\d+}/messages/{messageId : \\d+}")
    public Response getMessage(@PathParam("id") long id, @PathParam("messageId") long messageId) {
        Room room = findRoom(id);
        Message message = messageCrud.find(messageId);

        if (message == null) {
            throw new RequestedEntityNotFoundException(createGetMessageUri(room.getId(), messageId));
        }

        final MessageDto result = mapperFacade.map(message, MessageDto.class);
        return Response.ok(result).build();
    }

    @Secured(BusinessRole.ADMIN)
    @DELETE
    @Path("/{id : \\d+}/messages/{messageId : \\d+}")
    public Response removeMessage(@PathParam("id") long id, @PathParam("messageId") long messageId) {
        messageCrud.remove(messageId);
        return Response.noContent().build();
    }

    @Secured(BusinessRole.ADMIN)
    @PUT
    @Path("/{id : \\d+}/log")
    public Response generateLog(@PathParam("id") long id) {
        Room room = findRoom(id);
        logGenerator.requestLogGeneration(room);

        return Response.noContent().build();
    }

    private Room findRoom(long id) {
        Room room = roomCrud.find(id);
        if (room == null) {
            throw new RequestedEntityNotFoundException(createGetRoomUri(id));
        }
        return room;
    }

    private URI createGetRoomUri(Long id) {
        final UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
        return uriBuilder.path(RoomResource.class).path("/{id : \\d+}").build(id);
    }

    private URI createGetMessageUri(Long roomId, Long messageId) {
        final UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
        return uriBuilder
                .path(RoomResource.class)
                .path("/{id : \\d+}/messages/{messageId : \\d+}")
                .build(roomId, messageId);
    }
}
