package com.tsengfhy.vservice.basic.template.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.tsengfhy.vservice.basic.exception.sms.SmsPreparationException;
import com.tsengfhy.vservice.basic.exception.sms.SmsReceiveException;
import com.tsengfhy.vservice.basic.exception.sms.SmsSendException;
import com.tsengfhy.vservice.basic.template.SmsTemplate;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Setter
public class AliSmsTemplate implements SmsTemplate, InitializingBean {

    private IAcsClient acsClient;

    private String defaultSignName;

    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    private static final String VERSION = "2017-05-25";

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.acsClient, "IAcsClient must not be null!");
        Assert.notNull(this.defaultSignName, "Default signName must not be null!");
    }

    @Override
    public void send(String templateCode, String... phones) {
        this.send(templateCode, null, phones);
    }

    @Override
    public void send(String templateCode, Map<String, String> params, String... phones) {
        this.send(this.defaultSignName, templateCode, params, phones);
    }

    @Override
    public void send(String signName, String templateCode, Map<String, String> params, String... phones) {

        Set<String> set = Optional.ofNullable(phones).map(var -> Arrays.stream(var).collect(Collectors.toSet())).orElseGet(Collections::emptySet);
        CommonRequest request = null;
        if (set.size() == 1) {
            request = this.generateRequest("SendSms");
            request.putQueryParameter("PhoneNumbers", set.stream().findFirst().get());
            request.putQueryParameter("SignName", signName);
            request.putQueryParameter("TemplateCode", templateCode);
            request.putQueryParameter("TemplateParam", Optional.ofNullable(params).map(JSON::toJSONString).orElse(null));
        } else if (set.size() > 1) {
            request = this.generateRequest("SendBatchSms");
            request.putQueryParameter("PhoneNumberJson", JSON.toJSONString(set));
            request.putQueryParameter("SignNameJson", JSON.toJSONString(Collections.nCopies(set.size(), signName)));
            request.putQueryParameter("TemplateCode", templateCode);
            request.putQueryParameter("TemplateParamJson", Optional.ofNullable(params).map(var -> JSON.toJSONString(Collections.nCopies(set.size(), var))).orElse(null));
        }

        if (Optional.ofNullable(request).isPresent()) {
            try {
                CommonResponse response = acsClient.getCommonResponse(request);
                JSONObject data = JSON.parseObject(response.getData());
                if (!"OK".equals(data.getString("Code"))) {
                    throw new SmsSendException(data.getString("Message"));
                }
            } catch (ClientException e) {
                throw new SmsPreparationException(MessageSourceUtils.getMessage("Sms.connectFailure", "Sms server connect failed"), e);
            }
        }
    }

    @Override
    public int count(String phone, LocalDateTime sendDate) {

        CommonRequest request = this.generateRequest("QuerySendDetails");
        request.putQueryParameter("PhoneNumber", phone);
        request.putQueryParameter("SendDate", Optional.ofNullable(sendDate).orElseGet(LocalDateTime::now).format(DateTimeFormatter.ISO_LOCAL_DATE));
        request.putQueryParameter("CurrentPage", String.valueOf(1));
        request.putQueryParameter("PageSize", String.valueOf(Integer.MAX_VALUE));

        try {
            CommonResponse response = acsClient.getCommonResponse(request);
            JSONObject data = JSON.parseObject(response.getData());
            if ("OK".equals(data.getString("Code"))) {
                return data.getInteger("TotalCount");
            } else {
                throw new SmsReceiveException(data.getString("Message"));
            }
        } catch (ClientException e) {
            throw new SmsPreparationException(MessageSourceUtils.getMessage("Sms.connectFailure", "Sms server connect failed"), e);
        }
    }

    private CommonRequest generateRequest(String action) {

        CommonRequest request = new CommonRequest();
        request.setDomain(DOMAIN);
        request.setVersion(VERSION);
        request.setAction(action);

        return request;
    }
}
