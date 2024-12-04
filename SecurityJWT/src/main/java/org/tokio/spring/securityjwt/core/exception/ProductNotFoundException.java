package org.tokio.spring.securityjwt.core.exception;

import java.text.MessageFormat;

public class ProductNotFoundException extends RuntimeException {

    public static final MessageFormat PRODUCT_NOT_FOUND_MESSAGE = new MessageFormat("Product not found: {0}");

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductNotFoundException(Throwable cause) {
        super(cause);
    }

    public ProductNotFoundException(Long id) {
        super(PRODUCT_NOT_FOUND_MESSAGE.format( id ));
    }
}
