package com.tsengfhy.vservice.basic.web.handler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Order(100)
@ConditionalOnWebApplication
@ControllerAdvice
public class PageExceptionHandler extends AbstractExceptionHandler<ModelAndView> {

    @Override
    public boolean supportsMediaType(MediaType mediaType) {
        return MediaType.TEXT_HTML.isCompatibleWith(mediaType);
    }

    @Override
    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleException(Exception e, WebRequest webRequest) throws Exception {

        ResponseStatus responseStatus = super.handleExceptionInternal(e, webRequest);

        HttpStatus status = responseStatus.code();
        String reason = responseStatus.reason();

        if (StringUtils.hasLength(reason)) {
            ((ServletWebRequest) webRequest).getResponse().sendError(status.value(), reason);
        } else {
            ((ServletWebRequest) webRequest).getResponse().sendError(status.value());
        }

        return new ModelAndView();
    }
}
