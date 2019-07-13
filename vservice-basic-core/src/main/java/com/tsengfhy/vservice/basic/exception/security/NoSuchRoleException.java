package com.tsengfhy.vservice.basic.exception.security;

public class NoSuchRoleException extends SecurityPersistenceException {

    private static final long serialVersionUID = 0L;

    public NoSuchRoleException(String message) {
        super(message);
    }

    public NoSuchRoleException(String message, Throwable cause) {
        super(message, cause);
    }
}
