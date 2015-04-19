package org.ctu.fee.a4m39wa2.chalupa.chat.model;

import javax.persistence.MappedSuperclass;

import java.io.Serializable;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = -1;

    public abstract Long getId();

    public abstract void setId(Long id);

    @Override
    public String toString() {
        return getClass() + "(id=" + getId() + ')';
    }
}
