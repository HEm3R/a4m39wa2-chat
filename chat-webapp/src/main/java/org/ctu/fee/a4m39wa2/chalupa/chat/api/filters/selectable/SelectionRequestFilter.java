package org.ctu.fee.a4m39wa2.chalupa.chat.api.filters.selectable;

import org.ctu.fee.a4m39wa2.chalupa.chat.api.SelectionContext;
import org.ctu.fee.a4m39wa2.chalupa.chat.api.exceptions.InvalidFieldException;
import org.ctu.fee.a4m39wa2.chalupa.chat.dao.OrderDirection;
import org.ctu.fee.a4m39wa2.chalupa.chat.utils.TwoValueObject;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Instance of filter created for each {@link Selectable} annotation.
 */
@Priority(2)
public class SelectionRequestFilter implements ContainerRequestFilter {

    private static final String DELIMITER = ":";

    private static final String OFFSET_HEADER = "X-Offset";
    private static final String LIMIT_HEADER = "X-Limit";
    private static final String ORDER_BY_HEADER = "X-Order-By";

    private static final String OFFSET_PARAM = "offset";
    private static final String LIMIT_PARAM = "limit";
    private static final String ORDER_BY_PARAM = "orderBy";

    private Selectable selectable;
    private List<TwoValueObject<String, OrderDirection>> defaultOrders = new LinkedList<>();
    private Map<String, String> allowedFields;

    @Inject
    private SelectionContext selectionContext;

    void init(Selectable selectable) {
        this.selectable = selectable;

        allowedFields = new HashMap<>(selectable.fields().length);
        for (Field field : selectable.fields()) {
            allowedFields.put(field.value(), getEntityField(field));
        }

        for (Order o : selectable.orderBy()) {
            defaultOrders.add(new TwoValueObject<>(getEntityField(o.value()), o.direction()));
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        final List<String> orderBy = getAllParams(requestContext, ORDER_BY_HEADER, ORDER_BY_PARAM);

        if (orderBy != null) {
            final List<TwoValueObject<String, OrderDirection>> orders = new LinkedList<>();
            for (String o : orderBy) {
                final String[] components = o.split(DELIMITER, 2);
                final String fieldName = components[0];
                final String entityField = allowedFields.get(fieldName);

                if (entityField == null) {
                    throw new InvalidFieldException(ORDER_BY_PARAM, "invalid ordering field " + components[0]);
                }

                OrderDirection direction = OrderDirection.ASC;
                if (components.length == 2) {
                    try {
                        direction = OrderDirection.valueOf(components[1].toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new InvalidFieldException(ORDER_BY_PARAM, "invalid ordering " + components[1]);
                    }
                }
                orders.add(new TwoValueObject<>(fieldName, direction));
            }
            selectionContext.setOrderBy(orders);
        } else { // defaults
            selectionContext.setOrderBy(defaultOrders);
        }

        final Integer offset = getNumericParam(requestContext, OFFSET_HEADER, OFFSET_PARAM);
        if (offset != null) {
            selectionContext.setOffset(offset);
        } else {
            selectionContext.setOffset(0);
        }

        final Integer limit = getNumericParam(requestContext, LIMIT_HEADER, LIMIT_PARAM);
        if (limit != null) {
            if (limit > selectable.max()) {
                throw new InvalidFieldException(LIMIT_PARAM, "too big");
            }
            selectionContext.setLimit(limit);
        } else {
            selectionContext.setLimit(selectable.limit());
        }

    }

    private String getParam(ContainerRequestContext requestContext, String headerName, String paramName) {
        final MultivaluedMap<String, String> headers = requestContext.getHeaders();
        final MultivaluedMap<String, String> params = requestContext.getUriInfo().getQueryParameters();

        final String header = headers.getFirst(headerName);
        if (header != null) {
            return header;
        }

        return params.getFirst(paramName);
    }

    private List<String> getAllParams(ContainerRequestContext requestContext, String headerName, String paramName) {
        final MultivaluedMap<String, String> headers = requestContext.getHeaders();
        final MultivaluedMap<String, String> params = requestContext.getUriInfo().getQueryParameters();

        final List<String> header = headers.get(headerName);
        if (header != null) {
            return header;
        }

        return params.get(paramName);
    }

    private Integer getNumericParam(ContainerRequestContext requestContext, String headerName, String paramName) {
        try {
            final String param = getParam(requestContext, headerName, paramName);
            return param != null ? Integer.parseUnsignedInt(param) : null;
        } catch (NumberFormatException e) {
            throw new InvalidFieldException(paramName, "not a number");
        }
    }

    private String getEntityField(Field field) {
        return field.entityField() != null && !field.entityField().isEmpty() ? field.entityField() : field.value();
    }
}
