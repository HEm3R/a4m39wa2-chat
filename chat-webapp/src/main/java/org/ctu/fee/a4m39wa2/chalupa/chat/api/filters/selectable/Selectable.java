package org.ctu.fee.a4m39wa2.chalupa.chat.api.filters.selectable;

import javax.ws.rs.NameBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Selectable {

    /**
     * Default limit if not specified.
     */
    int limit() default 10;

    /**
     * Maximum limit that can be specified.
     */
    int max() default 100; //

    Field[] fields() default {};

    /**
     * Default orderBy if not specified.
     */
    Order[] orderBy() default @Order(@Field("id"));
}

