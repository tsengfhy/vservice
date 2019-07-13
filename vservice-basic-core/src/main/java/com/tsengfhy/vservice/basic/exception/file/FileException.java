package com.tsengfhy.vservice.basic.exception.file;

import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.Nullable;

public class FileException extends NestedRuntimeException {

    private static final long serialVersionUID = 0L;

    public FileException(String message) {
        super(message);
    }

    public FileException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }
}
