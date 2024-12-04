package org.tokio.spring.securityjwt.core.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.tokio.spring.securityjwt.core.constans.ErrorCode;
import org.tokio.spring.securityjwt.core.response.ResponseError;

import java.text.MessageFormat;

@RestControllerAdvice
@Slf4j
public class EndpointRestAdviceController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseError> handleException(final Exception ex, final HttpServletRequest request) {
        log.error(ex.getMessage(), ex);

        final MessageFormat message = new MessageFormat("Internal error occurred while processing request: {0}, because: {1}");
        final Object[] args = new Object[]{request.getRequestURI(), ex.getMessage()};

        final ResponseError responseError = ResponseError.of( (long) ErrorCode.INTERNAL_ERROR, message.format(args));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError);
    }
}
