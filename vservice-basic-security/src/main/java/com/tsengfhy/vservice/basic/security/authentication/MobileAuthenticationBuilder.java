package com.tsengfhy.vservice.basic.security.authentication;

import com.tsengfhy.vservice.basic.properties.Properties;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

public class MobileAuthenticationBuilder implements AuthenticationBuilder {

    private final Properties properties;

    public MobileAuthenticationBuilder(Properties properties) {
        Assert.notNull(properties, "Properties must not be null!");
        this.properties = properties;
    }

    @Override
    public AbstractAuthenticationToken build(HttpServletRequest request) throws AuthenticationServiceException {
        String mobile = request.getParameter(this.properties.getSecurity().getAuthentication().getMobile().getMobileParameter());
        if (StringUtils.isNotBlank(mobile)) {
            String code = request.getParameter(this.properties.getSecurity().getAuthentication().getMobile().getCodeParameter());
            return new MobileAuthenticationToken(mobile.trim(), code);
        }

        throw new AuthenticationServiceException(MessageSourceUtils.getMessage("Security.refuseLogin", "Authentication type not support"));
    }

    @Override
    public AbstractAuthenticationToken build(TokenRequest request) throws InvalidRequestException {
        Map<String, String> parameters = new LinkedHashMap<>(request.getRequestParameters());

        String mobile = parameters.get(this.properties.getSecurity().getAuthentication().getMobile().getMobileParameter());
        if (StringUtils.isNotBlank(mobile)) {
            String code = parameters.get(this.properties.getSecurity().getAuthentication().getMobile().getCodeParameter());
            return new MobileAuthenticationToken(mobile.trim(), code);
        }

        throw new InvalidRequestException(MessageSourceUtils.getMessage("Security.refuseLogin", "Authentication type not support"));
    }
}
