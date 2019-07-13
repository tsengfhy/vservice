package com.tsengfhy.vservice.basic.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.thymeleaf.util.ArrayUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class AbstractExceptionHandler<T> implements ExceptionHandler<T> {

    protected ResponseStatus handleExceptionInternal(Exception e, WebRequest webRequest) {

        ResponseStatus responseStatus = this.handleResponseStatus(e, webRequest);
        if (responseStatus != null) {
            return responseStatus;
        }

        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        HttpServletResponse response = ((ServletWebRequest) webRequest).getResponse();

        if (e instanceof ResponseStatusException) {
            return this.createResponseStatus(((ResponseStatusException) e).getStatus(), ((ResponseStatusException) e).getReason());
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            String[] supportedMethods = ((HttpRequestMethodNotSupportedException) e).getSupportedMethods();
            if (!ArrayUtils.isEmpty(supportedMethods)) {
                response.setHeader("Allow", StringUtils.arrayToDelimitedString(supportedMethods, ", "));
            }

            return this.createResponseStatus(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
        } else if (e instanceof HttpMediaTypeNotSupportedException) {
            List<MediaType> mediaTypes = ((HttpMediaTypeNotSupportedException) e).getSupportedMediaTypes();
            if (!CollectionUtils.isEmpty(mediaTypes)) {
                response.setHeader("Accept", MediaType.toString(mediaTypes));
            }

            return this.createResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage());
        } else if (e instanceof HttpMediaTypeNotAcceptableException) {
            return this.createResponseStatus(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        } else if (e instanceof MissingPathVariableException) {
            return this.handleServerError(e, webRequest);
        } else if (e instanceof MissingServletRequestParameterException) {
            return this.createResponseStatus(HttpStatus.BAD_REQUEST, e.getMessage());
        } else if (e instanceof ServletRequestBindingException) {
            return this.createResponseStatus(HttpStatus.BAD_REQUEST, e.getMessage());
        } else if (e instanceof ConversionNotSupportedException) {
            return this.handleServerError(e, webRequest);
        } else if (e instanceof TypeMismatchException) {
            return this.createResponseStatus(HttpStatus.BAD_REQUEST, e.getMessage());
        } else if (e instanceof HttpMessageNotReadableException) {
            return this.createResponseStatus(HttpStatus.BAD_REQUEST, e.getMessage());
        } else if (e instanceof HttpMessageNotWritableException) {
            return this.handleServerError(e, webRequest);
        } else if (e instanceof MethodArgumentNotValidException) {
            return this.createResponseStatus(HttpStatus.BAD_REQUEST, e.getMessage());
        } else if (e instanceof ConstraintViolationException) {
            return this.createResponseStatus(HttpStatus.BAD_REQUEST, e.getMessage());
        } else if (e instanceof MissingServletRequestPartException) {
            return this.createResponseStatus(HttpStatus.BAD_REQUEST, e.getMessage());
        } else if (e instanceof BindException) {
            return this.createResponseStatus(HttpStatus.BAD_REQUEST, e.getMessage());
        } else if (e instanceof NoHandlerFoundException) {
            return this.createResponseStatus(HttpStatus.NOT_FOUND, e.getMessage());
        } else if (e instanceof AsyncRequestTimeoutException) {
            if (response.isCommitted()) {
                log.warn("Async timeout for {} [{}]", request.getMethod(), request.getRequestURI());
            }

            return this.createResponseStatus(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        } else if (e instanceof IllegalArgumentException) {
            return this.createResponseStatus(HttpStatus.BAD_REQUEST, e.getMessage());
        } else if (e instanceof NullPointerException) {
            return this.createResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR, "Null pointer");
        } else {
            return this.handleServerError(e, webRequest);
        }
    }

    private ResponseStatus handleResponseStatus(Exception e, WebRequest webRequest) {

        return Optional.ofNullable(AnnotatedElementUtils.findMergedAnnotation(e.getClass(), ResponseStatus.class))
                .orElseGet(() -> {
                    if (e.getCause() instanceof Exception) {
                        return this.handleResponseStatus((Exception) e.getCause(), webRequest);
                    }
                    return null;
                });
    }

    private ResponseStatus handleServerError(Exception e, WebRequest webRequest) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        request.setAttribute("javax.servlet.error.exception", e);

        return this.createResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private ResponseStatus createResponseStatus(HttpStatus status, String reason) {
        return new ResponseStatus() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return ResponseStatus.class;
            }

            @Override
            public HttpStatus value() {
                return status;
            }

            @Override
            public HttpStatus code() {
                return status;
            }

            @Override
            public String reason() {
                return reason;
            }
        };
    }
}
