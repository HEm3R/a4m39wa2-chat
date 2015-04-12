package org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ErrorStatusCode {

    // subtypes for 400
    INVALID_FIELDS(40000),
    UNKNOWN_FIELD(40001),
    JSON_NOT_PARSEABLE(40002),

    // subtypes for 403
    LICENSE_EXCEEDED(40301),
    NOT_PATROLMAN(40302),
    NOT_ENDED_PATROL(40303),

    // subtypes for 404
    NOT_FOUND(40401),
    UNKNOWN_HOST(40402),

    // subtypes for 409

    /** Some resource depends on the resource being deleted. */
    DELETED_HAS_DEPENDENTS(40901),
    DUPLICATE_FIELD(40902),
    CONCURRENT_MODIFICATION(40903),

    // subtypes for 500
    INTERNAL_ERROR(50000);

    @Getter
    private final int code;
}
