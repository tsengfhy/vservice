package com.tsengfhy.vservice.basic.exception.file;

public class FileIOException extends FileException {

    private static final long serialVersionUID = 0L;

    public FileIOException(String message) {
        super(message);
    }

    public FileIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
