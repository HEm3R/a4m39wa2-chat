package org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DuplicateKeyValueErrorDto extends ApiErrorDto {

    public DuplicateKeyValueErrorDto(String field) {
        super(ErrorStatusCode.DUPLICATE_FIELD, "Field '" + field + "' already exists");
    }
}
