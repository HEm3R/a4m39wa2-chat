package org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error;

public class JsonParseErrorDto extends ApiErrorDto {

    private static final ErrorStatusCode CODE = ErrorStatusCode.JSON_NOT_PARSEABLE;
    private static final String MSG = "Error parsing json";

    public JsonParseErrorDto() {
        super(CODE, MSG);
    }
}
