package com.tsengfhy.vservice.basic.exception.security;

public class NoSuchUserException extends SecurityPersistenceException {

    private static final long serialVersionUID = 0L;

    public NoSuchUserException(String message) {
        super(message);
    }

    public NoSuchUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
