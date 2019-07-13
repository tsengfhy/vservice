package com.tsengfhy.vservice.basic.web.handler;

import org.springframework.http.MediaType;
import org.springframework.web.context.request.WebRequest;

public interface ExceptionHandler<T> {

    default boolean supportsMediaType(MediaType mediaType) {
        return MediaType.ALL.isCompatibleWith(mediaType);
    }

    T handleException(Exception e, WebRequest webRequest) throws Exception;
}
