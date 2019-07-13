package com.tsengfhy.vservice.basic.web.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

public class MutableHttpServletRequest extends HttpServletRequestWrapper {

    private final Map<String, String> headers;

    public MutableHttpServletRequest(HttpServletRequest request) {
        super(request);
        this.headers = new HashMap<>();
    }

    public void setHeader(String name, String value) {
        this.headers.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        return Optional.ofNullable(headers.get(name)).orElseGet(() -> ((HttpServletRequest) getRequest()).getHeader(name));
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> set = new HashSet<>(headers.keySet());

        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            String n = e.nextElement();
            set.add(n);
        }

        return Collections.enumeration(set);
    }

}
