package org.ctu.fee.a4m39wa2.chalupa.chat.logic.exceptions;

import lombok.Getter;

public class UniqueConstraintViolationException extends Exception {

    private static final long serialVersionUID = 1L;

    @Getter private final String field;

    public UniqueConstraintViolationException(String field) {
        this.field = field;
    }
}
