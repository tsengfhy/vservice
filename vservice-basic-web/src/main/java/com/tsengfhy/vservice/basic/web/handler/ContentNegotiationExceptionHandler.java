package com.tsengfhy.vservice.basic.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnWebApplication
@ControllerAdvice
public class ContentNegotiationExceptionHandler implements com.tsengfhy.vservice.basic.web.handler.ExceptionHandler<Object> {

    @Autowired
    private ContentNegotiationManager contentNegotiationManager;

    @Autowired(required = false)
    private List<com.tsengfhy.vservice.basic.web.handler.ExceptionHandler> exceptionHandlers = Collections.emptyList();

    @Override
    @ExceptionHandler(value = Exception.class)
    public Object handleException(Exception e, WebRequest webRequest) throws Exception {

        ExceptionUtils.printRootCauseStackTrace(e);
        List<MediaType> requestedMediaTypes;
        try {
            requestedMediaTypes = contentNegotiationManager.resolveMediaTypes(new ServletWebRequest(((ServletWebRequest) webRequest).getRequest()));
        } catch (HttpMediaTypeNotAcceptableException exception) {
            ExceptionUtils.printRootCauseStackTrace(exception);
            requestedMediaTypes = Collections.emptyList();
        }

        return requestedMediaTypes
                .stream()
                .map(mediaType ->
                        exceptionHandlers
                                .stream()
                                .filter(exceptionHandler -> exceptionHandler.supportsMediaType(mediaType))
                                .map(exceptionHandler -> {
                                    try {
                                        Object response = exceptionHandler.handleException(e, webRequest);
                                        log.debug("{} worked", exceptionHandler.getClass().getSimpleName());
                                        return response;
                                    } catch (Exception ex) {
                                        log.error("{} worked and encounter exception", exceptionHandler.getClass().getSimpleName(), ex);
                                        return null;
                                    }
                                })
                                .filter(Objects::nonNull)
                                .findFirst()
                                .orElse(null)
                )
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> e);
    }
}
