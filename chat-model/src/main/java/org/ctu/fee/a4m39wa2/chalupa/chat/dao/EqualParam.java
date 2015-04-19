package org.ctu.fee.a4m39wa2.chalupa.chat.dao;

import org.ctu.fee.a4m39wa2.chalupa.chat.model.BaseEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.List;

public class EqualParam<T extends BaseEntity> extends Param<T> {

    public EqualParam(String name, String value) {
        super(name, value);
    }

    @Override
    public void accept(List<Predicate> predicates, CriteriaBuilder cb, Root<T> root) {
        final Path<?> field = root.get(name);
        final Object fieldValue = resolveValue(field, value);

        predicates.add(cb.equal(field, fieldValue));
    }
}
