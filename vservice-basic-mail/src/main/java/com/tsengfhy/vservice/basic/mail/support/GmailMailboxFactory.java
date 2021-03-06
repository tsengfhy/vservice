package com.tsengfhy.vservice.basic.mail.support;

import java.util.Properties;
import java.util.regex.Pattern;

public class GmailMailboxFactory extends AbstractMailboxFactory {

    private static final Pattern GMAIL_MAILBOX_USERNAME = Pattern.compile("^\\w+@gmail\\.com$");

    @Override
    public boolean supports(String username) {
        return GMAIL_MAILBOX_USERNAME.matcher(username).find();
    }

    @Override
    protected Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.starttls.required", "true");

        properties.setProperty("mail.store.protocol", "imap");
        properties.setProperty("mail.imap.host", "imap.gmail.com");
        properties.setProperty("mail.imap.port", "993");
        properties.setProperty("mail.imap.ssl.enable", "true");

        return properties;
    }
}
