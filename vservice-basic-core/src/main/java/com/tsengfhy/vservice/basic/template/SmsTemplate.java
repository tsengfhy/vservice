package com.tsengfhy.vservice.basic.template;

import com.tsengfhy.vservice.basic.exception.sms.SmsException;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

public interface SmsTemplate {

    void send(@NotNull String templateCode, String... phones) throws SmsException;

    void send(@NotNull String templateCode, Map<String, String> params, String... phones) throws SmsException;

    void send(@NotNull String signName, @NotNull String templateCode, Map<String, String> params, String... phones) throws SmsException;

    int count(@NotNull String phone, LocalDateTime sendDate) throws SmsException;
}
