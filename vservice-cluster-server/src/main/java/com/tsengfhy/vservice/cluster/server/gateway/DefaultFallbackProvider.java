package com.tsengfhy.vservice.cluster.server.gateway;

import com.tsengfhy.vservice.basic.properties.Properties;
import com.tsengfhy.vservice.basic.utils.DataUtils;
import com.tsengfhy.vservice.basic.utils.RestUtils;
import com.tsengfhy.vservice.basic.utils.WebUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;

public class DefaultFallbackProvider implements FallbackProvider {

    @Autowired
    private Properties properties;

    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String serviceId, Throwable cause) {

        if (!WebUtils.isRest()) {
            ReflectionUtils.rethrowRuntimeException(cause);
        }

        Throwable exception = ExceptionUtils.getRootCause(cause);
        HttpStatus status;
        if (exception instanceof ConnectTimeoutException) {
            status = HttpStatus.GATEWAY_TIMEOUT;
        } else if (exception instanceof SocketTimeoutException) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return properties.getWeb().isHttpStatusFollowed() ? status : HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return properties.getWeb().isHttpStatusFollowed() ? status.value() : HttpStatus.OK.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return properties.getWeb().isExceptionFollowed() ? exception.getMessage() : status.getReasonPhrase();
            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream(DataUtils.toJSON(RestUtils.operate(status.value(), properties.getWeb().isExceptionFollowed() ? exception.getMessage() : status.getReasonPhrase())).getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return headers;
            }

            @Override
            public void close() {
            }
        };
    }
}
