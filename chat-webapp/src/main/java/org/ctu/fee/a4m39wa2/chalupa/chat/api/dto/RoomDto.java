package org.ctu.fee.a4m39wa2.chalupa.chat.api.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import lombok.Getter;
import lombok.Setter;

public class RoomDto {

    @Null
    @Getter @Setter private Long id;

    @NotNull
    @Getter @Setter private String name;
}
