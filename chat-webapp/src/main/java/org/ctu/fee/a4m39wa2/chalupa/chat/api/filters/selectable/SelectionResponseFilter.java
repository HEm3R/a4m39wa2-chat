package org.ctu.fee.a4m39wa2.chalupa.chat.api.filters.selectable;

import org.ctu.fee.a4m39wa2.chalupa.chat.api.SelectionContext;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import java.io.IOException;

@Selectable
@Provider
@Priority(2)
public class SelectionResponseFilter implements ContainerResponseFilter {

    private static final String TOTAL_HEADER = "X-Total";

    @Inject
    private SelectionContext selectionContext;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        final MultivaluedMap<String, Object> responseHeaders = responseContext.getHeaders();

        final Integer total = selectionContext.getTotal();
        if (total != null) {
            responseHeaders.add(TOTAL_HEADER, selectionContext.getTotal());
        } else {
            if (responseContext.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                throw new RuntimeException("Missing total value for path " + requestContext.getUriInfo().getPath());
            }
        }
    }
}
