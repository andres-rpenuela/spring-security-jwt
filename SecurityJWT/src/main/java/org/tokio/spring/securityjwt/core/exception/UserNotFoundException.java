package org.tokio.spring.securityjwt.core.exception;

import java.text.MessageFormat;

public class UserNotFoundException extends RuntimeException {
  public static final MessageFormat USER_NOT_FOUND_MESSAGE = new MessageFormat("User not found: {0}");

  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserNotFoundException(Throwable cause) {
    super(cause);
  }

  public UserNotFoundException(Long id) {
    super(USER_NOT_FOUND_MESSAGE.format( id ));
  }

}
