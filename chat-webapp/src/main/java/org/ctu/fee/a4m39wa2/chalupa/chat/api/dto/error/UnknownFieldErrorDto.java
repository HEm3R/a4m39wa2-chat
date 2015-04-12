package org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class UnknownFieldErrorDto extends ApiErrorDto {

    private static final ErrorStatusCode CODE = ErrorStatusCode.UNKNOWN_FIELD;
    private static final String MSG = "Unknown field";

    @Getter @Setter private String fieldName;

    public UnknownFieldErrorDto(String fieldName) {
        super(CODE, MSG + "'" + fieldName + "'");
        this.fieldName = fieldName;
    }
}
