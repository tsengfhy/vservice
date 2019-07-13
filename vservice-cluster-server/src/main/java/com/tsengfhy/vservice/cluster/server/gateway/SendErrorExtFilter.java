package com.tsengfhy.vservice.cluster.server.gateway;

import com.netflix.zuul.context.RequestContext;
import com.tsengfhy.vservice.basic.properties.Properties;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.net.SocketTimeoutException;

public class SendErrorExtFilter extends SendErrorFilter {

    @Autowired
    private Properties properties;

    @Override
    public Object run() {

        try {
            RequestContext ctx = RequestContext.getCurrentContext();
            Throwable exception = ExceptionUtils.getRootCause(ctx.getThrowable());

            HttpStatus status;
            String errorPath;
            if (exception instanceof ConnectTimeoutException) {
                status = HttpStatus.GATEWAY_TIMEOUT;
                errorPath = properties.getWeb().getGatewayTimeout();
            } else if (exception instanceof SocketTimeoutException) {
                status = HttpStatus.SERVICE_UNAVAILABLE;
                errorPath = properties.getWeb().getServiceUnavailable();
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                errorPath = properties.getWeb().getInternalServerError();
            }

            HttpServletRequest request = ctx.getRequest();
            request.setAttribute("javax.servlet.error.exception", exception);
            request.setAttribute("javax.servlet.error.status_code", status.value());
            request.setAttribute("javax.servlet.error.message", exception.getMessage());

            RequestDispatcher dispatcher = request.getRequestDispatcher(errorPath);
            if (dispatcher != null) {
                ctx.set("sendErrorExtFilter.ran", true);
                if (!ctx.getResponse().isCommitted()) {
                    ctx.setResponseStatusCode(properties.getWeb().isHttpStatusFollowed() ? status.value() : HttpStatus.OK.value());
                    dispatcher.forward(request, ctx.getResponse());
                }
            }
        } catch (Exception var5) {
            ReflectionUtils.rethrowRuntimeException(var5);
        }

        return null;
    }
}
