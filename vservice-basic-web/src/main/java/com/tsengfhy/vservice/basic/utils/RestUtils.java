package com.tsengfhy.vservice.basic.utils;

import com.tsengfhy.vservice.basic.web.core.Rest;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public final class RestUtils {

    public static Rest save(boolean result, @Nullable String message, @Nullable Object data) {
        return operate(
                result ? HttpStatus.OK.value() : HttpStatus.INTERNAL_SERVER_ERROR.value(),
                result ? MessageSourceUtils.getMessage("Rest.saveSuccess", "Successful save") : Optional.ofNullable(message).filter(msg -> StringUtils.isNotBlank(msg)).orElseGet(() -> MessageSourceUtils.getMessage("Rest.saveFailure", "Save failed")),
                null,
                data
        );
    }

    public static Rest delete(boolean result, @Nullable String message) {
        return operate(
                result ? HttpStatus.OK.value() : HttpStatus.INTERNAL_SERVER_ERROR.value(),
                result ? MessageSourceUtils.getMessage("Rest.deleteSuccess", "Successful delete") : Optional.ofNullable(message).filter(msg -> StringUtils.isNotBlank(msg)).orElseGet(() -> MessageSourceUtils.getMessage("Rest.deleteFailure", "Delete failed")),
                null,
                null
        );
    }

    public static Rest update(boolean result, @Nullable String message, @Nullable Object data) {
        return operate(
                result ? HttpStatus.OK.value() : HttpStatus.INTERNAL_SERVER_ERROR.value(),
                result ? MessageSourceUtils.getMessage("Rest.updateSuccess", "Successful update") : Optional.ofNullable(message).filter(msg -> StringUtils.isNotBlank(msg)).orElseGet(() -> MessageSourceUtils.getMessage("Rest.updateFailure", "Update failed")),
                null,
                data
        );
    }

    public static Rest select(@Nullable Object data) {
        return operate(
                Optional.ofNullable(data).map(d -> HttpStatus.OK.value()).orElseGet(HttpStatus.INTERNAL_SERVER_ERROR::value),
                Optional.ofNullable(data).map(d -> MessageSourceUtils.getMessage("Rest.selectSuccess", "Successful query")).orElseGet(() -> MessageSourceUtils.getMessage("Rest.selectFailure", "Query failed")),
                null,
                data
        );
    }

    public static Rest operate(int status, String message) {
        return operate(status, message, null, null);
    }

    public static Rest operate(int status, String message, Object data) {
        return operate(status, message, null, data);
    }

    public static Rest operate(int status, String message, Map<String, String> errors, Object data) {

        Rest<Object> rest = new Rest<>();

        rest.setStatus(status);
        rest.setMessage(message);
        rest.setPath(WebUtils.getRequest().map(HttpServletRequest::getServletPath).orElse(null));
        rest.setTimestamp(LocalDateTime.now());
        rest.setErrors(errors);
        rest.setData(data);

        return rest;
    }
}
