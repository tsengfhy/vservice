package com.tsengfhy.vservice.basic.security.oauth2;

import com.tsengfhy.vservice.basic.properties.Properties;
import com.tsengfhy.vservice.basic.web.http.MutableHttpServletRequest;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

public class ClientTokenEndpointAuthenticationFilter extends OncePerRequestFilter {

    private final Properties properties;

    private final RequestMatcher requestMatcher;

    @Setter
    private String charset = "UTF-8";

    public ClientTokenEndpointAuthenticationFilter(Properties properties) {
        this.requestMatcher = new AntPathRequestMatcher("/oauth/token");

        Assert.notNull(properties, "Properties must not be null!");
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        if (!requestMatcher.matches(request)) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (StringUtils.isNotBlank(header) && header.startsWith("Basic ")) {
            chain.doFilter(request, response);
        } else {
            String clientId = Optional.ofNullable(request.getParameter(properties.getSecurity().getOauth2().getAuthorization().getClientIdParameter())).orElse("");
            String clientSecret = Optional.ofNullable(request.getParameter(properties.getSecurity().getOauth2().getAuthorization().getClientSecretParameter())).orElse("");

            StringBuilder sb = new StringBuilder();
            sb.append(clientId).append(":").append(clientSecret);
            MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
            mutableRequest.setHeader("Authorization", "Basic" + " " + new String(Base64.getEncoder().encode(sb.toString().getBytes(this.charset)), this.charset));

            chain.doFilter(mutableRequest, response);
        }
    }
}
