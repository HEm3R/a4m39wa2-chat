package org.ctu.fee.a4m39wa2.chalupa.chat.dao;

import org.ctu.fee.a4m39wa2.chalupa.chat.model.BaseEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public abstract class Param<T extends BaseEntity> {

    protected String name;
    protected String value;

    public abstract void accept(List<Predicate> predicates, CriteriaBuilder cb, Root<T> root);

    @SneakyThrows({InstantiationException.class, NoSuchMethodException.class, IllegalAccessException.class, InvocationTargetException.class})
    protected Object resolveValue(Path<?> field, String value) {
        final Class<?> type = field.getModel().getBindableJavaType();
        if (type.isEnum()) {
            for (Object e : type.getEnumConstants()) {
                if (e.toString().equals(value)) {
                    return e;
                }
            }
            throw new RuntimeException("enum constant not found, this should not happen as ValidationVisitor should have checked it");
        } else if (type == Timestamp.class) {
            return new Timestamp(Long.valueOf(value));
        } else if (BaseEntity.class.isAssignableFrom(type)) {
            final BaseEntity entity = (BaseEntity) type.getConstructor().newInstance();
            entity.setId(Long.valueOf(value));
            return entity;
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.valueOf(value);
        } else {
            return type.getConstructor(String.class).newInstance(value);
        }
    }
}
