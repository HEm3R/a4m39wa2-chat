package org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class ValidationErrorDto extends ApiErrorDto {

    private static final ErrorStatusCode CODE = ErrorStatusCode.INVALID_FIELDS;
    private static final String MSG = "Please correct the following invalid fields";

    @ToString
    @NoArgsConstructor
    public static class InvalidField {

        @Getter @Setter private String name;
        @Getter @Setter private String message;

        public InvalidField(String field, String message) {
            name = field;
            this.message = message;
        }
    }

    @Getter private final List<InvalidField> invalidFields;

    public ValidationErrorDto() {
        super(CODE, MSG);
        invalidFields = new ArrayList<>();
    }

    public ValidationErrorDto(InvalidField singleError) {
        super(CODE, MSG);
        invalidFields = Collections.singletonList(singleError);
    }

    public void addInvalidField(InvalidField validationErrorItem) {
        invalidFields.add(validationErrorItem);
    }
}
