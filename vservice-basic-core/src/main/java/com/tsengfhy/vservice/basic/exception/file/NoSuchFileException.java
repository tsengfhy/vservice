package com.tsengfhy.vservice.basic.exception.file;

public class NoSuchFileException extends FileException {

    private static final long serialVersionUID = 0L;

    public NoSuchFileException(String message) {
        super(message);
    }

    public NoSuchFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
