package com.tsengfhy.vservice.basic.exception.security;

public class SecurityPersistenceException extends SecurityException {

    private static final long serialVersionUID = 0L;

    public SecurityPersistenceException(String message) {
        super(message);
    }

    public SecurityPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
