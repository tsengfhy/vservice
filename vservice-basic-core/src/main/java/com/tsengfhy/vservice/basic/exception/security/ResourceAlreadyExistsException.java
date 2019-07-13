package com.tsengfhy.vservice.basic.exception.security;

public class ResourceAlreadyExistsException extends SecurityPersistenceException {

    private static final long serialVersionUID = 0L;

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
