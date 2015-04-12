package org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error;

public class NotFoundErrorDto extends ApiErrorDto {

    public NotFoundErrorDto(String uri) {
        super(ErrorStatusCode.NOT_FOUND, "Could not find resource for: " + uri);
    }

    public NotFoundErrorDto(ErrorStatusCode errorType, String description) {
        super(errorType, description);
    }
}
