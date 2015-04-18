package org.ctu.fee.a4m39wa2.chalupa.chat.api.filters.selectable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Field {

    String value();

    String entityField() default "";
}
