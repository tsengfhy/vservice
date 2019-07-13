package com.tsengfhy.vservice.basic.controller;

import com.tsengfhy.vservice.basic.properties.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ConditionalOnWebApplication
@Controller
public class IndexCtrl {

    @Autowired
    private Properties properties;

    @RequestMapping("${vservice.web.index:/index.html}")
    public String index(HttpServletRequest request, HttpServletResponse response) {

        return "index";
    }

    @RequestMapping("${vservice.web.bad-request:/400.html}")
    public String badRequest(HttpServletRequest request, HttpServletResponse response) {
        if (properties.getWeb().isHttpStatusFollowed()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }

        return "400";
    }

    @RequestMapping("${vservice.web.unauthorized:/401.html}")
    public String unauthorized(HttpServletRequest request, HttpServletResponse response) {
        if (properties.getWeb().isHttpStatusFollowed()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

        return "401";
    }

    @RequestMapping("${vservice.web.forbidden:/403.html}")
    public String forbidden(HttpServletRequest request, HttpServletResponse response) {
        if (properties.getWeb().isHttpStatusFollowed()) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }

        return "403";
    }

    @RequestMapping("${vservice.web.not-found:/404.html}")
    public String notFound(HttpServletRequest request, HttpServletResponse response) {
        if (properties.getWeb().isHttpStatusFollowed()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }

        return "404";
    }

    @RequestMapping("${vservice.web.method-not-allowed:/405.html}")
    public String methodNotAllowed(HttpServletRequest request, HttpServletResponse response) {
        if (properties.getWeb().isHttpStatusFollowed()) {
            response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        }

        return "405";
    }

    @RequestMapping("${vservice.web.internal-server-error:/500.html}")
    public String internalServerError(HttpServletRequest request, HttpServletResponse response) {
        if (properties.getWeb().isHttpStatusFollowed()) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return "500";
    }

    @RequestMapping("${vservice.web.service-unavailable:/503.html}")
    public String serviceUnavailable(HttpServletRequest request, HttpServletResponse response) {
        if (properties.getWeb().isHttpStatusFollowed()) {
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        }

        return "503";
    }

    @RequestMapping("${vservice.web.gateway-timeout:/504.html}")
    public String gatewayTimeout(HttpServletRequest request, HttpServletResponse response) {
        if (properties.getWeb().isHttpStatusFollowed()) {
            response.setStatus(HttpStatus.GATEWAY_TIMEOUT.value());
        }

        return "504";
    }
}
