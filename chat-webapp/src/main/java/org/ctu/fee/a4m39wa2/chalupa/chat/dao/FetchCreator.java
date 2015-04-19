package org.ctu.fee.a4m39wa2.chalupa.chat.dao;

import org.ctu.fee.a4m39wa2.chalupa.chat.model.BaseEntity;

import javax.persistence.criteria.Root;

@FunctionalInterface
public interface FetchCreator<T extends BaseEntity> {

    void accept(Root<T> root);
}
