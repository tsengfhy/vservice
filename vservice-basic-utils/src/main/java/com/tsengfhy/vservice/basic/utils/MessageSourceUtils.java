package com.tsengfhy.vservice.basic.utils;

import lombok.Setter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;

import java.util.Optional;

@Slf4j
@UtilityClass
public final class MessageSourceUtils {

    @Setter
    private static MessageSource messageSource;

    public static String getMessage(String code) throws UnsupportedOperationException {
        return getMessage(code, new Object[]{});
    }

    public static String getMessage(String code, Object[] args) throws UnsupportedOperationException {

        return getMessageInternal(code, args, null)
                .orElseThrow(() -> new UnsupportedOperationException("Method MessageSourceUtils.getMessage() not support"));
    }

    public static String getMessage(String code, String defaultMessage) {
        return getMessage(code, new Object[]{}, defaultMessage);
    }

    public static String getMessage(String code, Object[] args, String defaultMessage) {

        return getMessageInternal(code, args, defaultMessage)
                .orElseGet(() -> {
                    log.warn("MessageSourceUtils initialization failed, and use default as result");

                    return defaultMessage;
                });
    }

    private static Optional<String> getMessageInternal(String code, @Nullable Object[] args, @Nullable String defaultMessage) {
        return Optional.ofNullable(messageSource).map(messageSource -> messageSource.getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale()));
    }
}
