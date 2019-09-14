package com.tsengfhy.vservice.basic.security.oauth2;

import com.tsengfhy.vservice.basic.properties.Properties;
import com.tsengfhy.vservice.basic.utils.RestUtils;
import com.tsengfhy.vservice.basic.web.core.Rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.provider.error.OAuth2ExceptionRenderer;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Oauth2ExceptionRendererImpl implements OAuth2ExceptionRenderer {

    @Autowired
    private Properties properties;

    @Autowired
    private HttpMessageConverters httpMessageConverters;

    @Override
    public void handleHttpEntityResponse(HttpEntity<?> responseEntity, ServletWebRequest webRequest) throws Exception {
        if (responseEntity == null) {
            return;
        }

        HttpInputMessage inputMessage = createHttpInputMessage(webRequest);
        HttpOutputMessage outputMessage = createHttpOutputMessage(webRequest);

        HttpStatus status = (responseEntity instanceof ResponseEntity) ? ((ResponseEntity<?>) responseEntity).getStatusCode() : HttpStatus.OK;
        ((ServerHttpResponse) outputMessage).setStatusCode(properties.getWeb().isHttpStatusFollowed() ? status : HttpStatus.OK);

        HttpHeaders entityHeaders = responseEntity.getHeaders();
        if (!entityHeaders.isEmpty()) {
            outputMessage.getHeaders().putAll(entityHeaders);
        }

        Object body = responseEntity.getBody();
        if (body != null) {
            Rest rest = RestUtils.operate(status.value(), properties.getWeb().isExceptionFollowed() ? ((body instanceof Exception) ? ((Exception) body).getMessage() : body.toString()) : status.getReasonPhrase());

            List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
            if (acceptedMediaTypes.isEmpty()) {
                acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
            }
            MediaType.sortByQualityValue(acceptedMediaTypes);

            for (MediaType acceptedMediaType : acceptedMediaTypes) {
                for (HttpMessageConverter httpMessageConverter : httpMessageConverters.getConverters()) {
                    if (httpMessageConverter.canWrite(Rest.class, acceptedMediaType)) {
                        httpMessageConverter.write(rest, acceptedMediaType, outputMessage);
                        return;
                    }
                }
            }

            List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
            for (HttpMessageConverter httpMessageConverter : httpMessageConverters.getConverters()) {
                allSupportedMediaTypes.addAll(httpMessageConverter.getSupportedMediaTypes());
            }
            throw new HttpMediaTypeNotAcceptableException(allSupportedMediaTypes);
        } else {
            // flush headers
            outputMessage.getBody();
        }
    }

    private HttpInputMessage createHttpInputMessage(NativeWebRequest webRequest) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        return new ServletServerHttpRequest(servletRequest);
    }

    private HttpOutputMessage createHttpOutputMessage(NativeWebRequest webRequest) throws Exception {
        HttpServletResponse servletResponse = (HttpServletResponse) webRequest.getNativeResponse();
        return new ServletServerHttpResponse(servletResponse);
    }
}
