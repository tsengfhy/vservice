package com.tsengfhy.vservice.basic.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ConditionalOnWebApplication
@Controller
public class SecurityCtrl {

    @RequestMapping("${vservice.web.login:/login.html}")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return "login";
    }
}
