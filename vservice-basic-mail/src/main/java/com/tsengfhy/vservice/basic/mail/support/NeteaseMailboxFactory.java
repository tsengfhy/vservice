package com.tsengfhy.vservice.basic.mail.support;

import java.util.Properties;
import java.util.regex.Pattern;

public class NeteaseMailboxFactory extends AbstractMailboxFactory {

    private static final Pattern NETEASE_MAILBOX_USERNAME = Pattern.compile("^\\w+@163\\.com$");

    @Override
    public boolean supports(String username) {
        return NETEASE_MAILBOX_USERNAME.matcher(username).find();
    }

    @Override
    protected Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.host", "smtp.163.com");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.starttls.required", "true");

        properties.setProperty("mail.store.protocol", "imap");
        properties.setProperty("mail.imap.host", "imap.163.com");
        properties.setProperty("mail.imap.port", "993");
        properties.setProperty("mail.imap.ssl.enable", "true");

        return properties;
    }
}
