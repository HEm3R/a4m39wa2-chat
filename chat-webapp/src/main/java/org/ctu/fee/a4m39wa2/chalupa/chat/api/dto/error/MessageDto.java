package org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import lombok.Getter;
import lombok.Setter;

public class MessageDto {

    @Null
    @Getter @Setter private Long id;

    @NotNull
    @Getter @Setter private String text;

    @Null
    @Getter @Setter private String author;

    @Null
    @Getter @Setter private String authorId;
}
