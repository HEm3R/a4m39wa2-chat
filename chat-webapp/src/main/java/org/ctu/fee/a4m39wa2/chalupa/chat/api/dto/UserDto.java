package org.ctu.fee.a4m39wa2.chalupa.chat.api.dto;

import org.ctu.fee.a4m39wa2.chalupa.chat.validation.group.Create;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import lombok.Getter;
import lombok.Setter;

public class UserDto {

    @Null
    @Getter @Setter private Long id;

    @NotNull(groups = Create.class)
    @Getter @Setter private String username;

    @NotNull(groups = Create.class)
    @Getter @Setter private String password;
}
