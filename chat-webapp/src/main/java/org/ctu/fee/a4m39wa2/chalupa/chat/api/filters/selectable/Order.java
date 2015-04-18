package org.ctu.fee.a4m39wa2.chalupa.chat.api.filters.selectable;

import org.ctu.fee.a4m39wa2.chalupa.chat.dao.OrderDirection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Order {

    Field value();

    OrderDirection direction() default OrderDirection.ASC;
}
