package com.tsengfhy.vservice.basic.properties;

import lombok.Data;

@Data
public class WebProperties {

    private String login = "/login.html";
    private String logout = "/login.html";
    private String index = "/index.html";
    private String error = "";
    private String failure = "/login.html?failure";
    private String expired = "/login.html?expired";
    private String timeout = "/login.html?timeout";
    private String badRequest = "/400.html";
    private String unauthorized = "/401.html";
    private String forbidden = "/403.html";
    private String notFound = "/404.html";
    private String methodNotAllowed = "/405.html";
    private String internalServerError = "/500.html";
    private String serviceUnavailable = "/503.html";
    private String gatewayTimeout = "/504.html";

    private boolean sslOnly = false;
    private boolean httpStatusFollowed = false;
    private boolean exceptionFollowed = true;

    private OpenApiProperties openApi = new OpenApiProperties();
}
