package com.tsengfhy.vservice.basic.security.oauth2;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class Rest extends OAuth2Exception {

    private static final long serialVersionUID = 0L;
    private Integer status;
    private String message;
    private String path;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private String token;
    private Map<String, String> errors;
    private Object data;

    public Rest() {
        super("");
    }

    @Override
    public String getOAuth2ErrorCode() {
        return null;
    }

    @Override
    public int getHttpErrorCode() {
        return getStatus();
    }

    @Override
    public String getSummary() {
        return null;
    }

    @Override
    public String getLocalizedMessage() {
        return null;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return null;
    }
}
