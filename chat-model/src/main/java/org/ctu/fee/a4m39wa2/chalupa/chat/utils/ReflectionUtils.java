package org.ctu.fee.a4m39wa2.chalupa.chat.utils;

import java.lang.reflect.ParameterizedType;

public final class ReflectionUtils {

    public static <T> Class<T> getGenericClass(final Class<?> parametrizedClass, int pos) {
        return (Class<T>) ((ParameterizedType) parametrizedClass.getGenericSuperclass()).getActualTypeArguments()[pos];
    }

    private ReflectionUtils() {}
}
