package org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error;

public class InternalServerErrorDto extends ApiErrorDto {

    public InternalServerErrorDto() {
        super(ErrorStatusCode.INTERNAL_ERROR, "Something went wrong during request processing");
    }
}
