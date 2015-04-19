package org.ctu.fee.a4m39wa2.chalupa.chat.utils;

import java.io.Serializable;

import lombok.Value;

@Value
public class TwoValueObject<U, V> implements Serializable {

    private static final long serialVersionUID = 1L;

    private U first;
    private V second;
}
