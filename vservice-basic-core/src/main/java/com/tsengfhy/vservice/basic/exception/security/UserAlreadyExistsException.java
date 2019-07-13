package com.tsengfhy.vservice.basic.exception.security;

public class UserAlreadyExistsException extends SecurityPersistenceException {

    private static final long serialVersionUID = 0L;

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
