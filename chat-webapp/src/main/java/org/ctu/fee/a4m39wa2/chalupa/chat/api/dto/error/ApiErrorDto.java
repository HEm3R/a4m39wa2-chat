package org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
public class ApiErrorDto {

    @Getter @Setter private int code;

    @Getter @Setter private String description;

    protected ApiErrorDto(ErrorStatusCode errorType, String description) {
        this.code = errorType.getCode();
        this.description = description;
    }
}
