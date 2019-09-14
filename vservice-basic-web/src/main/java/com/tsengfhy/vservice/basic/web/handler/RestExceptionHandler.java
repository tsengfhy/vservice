package com.tsengfhy.vservice.basic.web.handler;

import com.tsengfhy.vservice.basic.properties.Properties;
import com.tsengfhy.vservice.basic.utils.RestUtils;
import com.tsengfhy.vservice.basic.web.core.Rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Order(200)
@ConditionalOnWebApplication
@ControllerAdvice
public class RestExceptionHandler extends AbstractExceptionHandler<ResponseEntity<Object>> {

    @Autowired
    private Properties properties;

    private static final Pattern CONSTRAINT_VIOLATION_PATTERN = Pattern.compile("^.+\\.(.+)$");

    @Override
    public boolean supportsMediaType(MediaType mediaType) {
        return MediaType.APPLICATION_JSON_UTF8.isCompatibleWith(mediaType);
    }

    @Override
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception e, WebRequest webRequest) throws Exception {

        ResponseStatus responseStatus = super.handleExceptionInternal(e, webRequest);

        HttpStatus status = responseStatus.code();
        String reason = responseStatus.reason();

        Rest rest;
        if (e instanceof MethodArgumentNotValidException) {
            Map<String, String> errors = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors()
                    .stream()
                    .map(objectError -> (FieldError) objectError)
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

            rest = RestUtils.operate(
                    status.value(),
                    properties.getWeb().isExceptionFollowed() ? "Validation failed for argument" : status.getReasonPhrase(),
                    errors,
                    null
            );
        } else if (e instanceof ConstraintViolationException) {
            Map<String, String> errors = ((ConstraintViolationException) e).getConstraintViolations()
                    .stream()
                    .collect(Collectors.toMap(
                            constraintViolation -> Optional.of(constraintViolation.getPropertyPath().toString()).map(CONSTRAINT_VIOLATION_PATTERN::matcher).filter(Matcher::find).map(matcher -> matcher.group(1)).orElseGet(() -> constraintViolation.getPropertyPath().toString()),
                            ConstraintViolation::getMessage
                    ));

            rest = RestUtils.operate(
                    status.value(),
                    properties.getWeb().isExceptionFollowed() ? (errors.isEmpty() ? reason : "Validation failed for argument") : status.getReasonPhrase(),
                    errors.isEmpty() ? null : errors,
                    null
            );
        } else {
            rest = RestUtils.operate(status.value(), properties.getWeb().isExceptionFollowed() ? reason : status.getReasonPhrase());
        }

        return new ResponseEntity<>(rest, properties.getWeb().isHttpStatusFollowed() ? status : HttpStatus.OK);
    }
}
