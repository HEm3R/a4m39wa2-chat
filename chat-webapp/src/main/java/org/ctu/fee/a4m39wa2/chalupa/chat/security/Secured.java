package org.ctu.fee.a4m39wa2.chalupa.chat.security;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Secured {

    @Nonbinding BusinessRole[] value() default {};
}
