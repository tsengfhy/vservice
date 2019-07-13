package com.tsengfhy.vservice.basic.properties;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class AuthenticationProperties {

    private String loginUrl = "/login";
    private String logoutUrl = "/logout";

    private String usernameParameter = "username";
    private String passwordParameter = "password";

    private MobileProperties mobile = new MobileProperties();

    private TokenProperties token = new TokenProperties();

    private String limitMethod = "POST";
    private List<String> deleteCookies = new ArrayList<>();
    private List<String> publicUrls = Arrays.asList("/public/**");
}
