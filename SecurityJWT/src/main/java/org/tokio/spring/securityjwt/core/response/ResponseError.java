package org.tokio.spring.securityjwt.core.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.tokio.spring.securityjwt.core.constans.ErrorCode;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseError {

    private Long errorCode;
    private String errorMessage;

    public static ResponseError of(final Long errorCode, final String errorMessage) {
        return new ResponseError(errorCode, errorMessage);
    }

    public static ResponseError noError() {
        return new ResponseError( (long) ErrorCode.NO_ERROR,ErrorCode.NO_MESSAGE);
    }

    public static ResponseError internalServerError() {
        return new ResponseError( (long) ErrorCode.INTERNAL_ERROR,ErrorCode.INTERNAL_ERROR_MESSAGE);
    }
}
