package com.tsengfhy.vservice.basic.exception.security;

public class RoleAlreadyExistsException extends SecurityPersistenceException {

    private static final long serialVersionUID = 0L;

    public RoleAlreadyExistsException(String message) {
        super(message);
    }

    public RoleAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
