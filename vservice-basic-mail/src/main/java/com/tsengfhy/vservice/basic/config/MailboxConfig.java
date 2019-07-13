package com.tsengfhy.vservice.basic.config;

import com.tsengfhy.vservice.basic.mail.support.AbstractMailboxFactory;
import com.tsengfhy.vservice.basic.mail.support.GmailMailboxFactory;
import com.tsengfhy.vservice.basic.mail.support.NeteaseMailboxFactory;
import com.tsengfhy.vservice.basic.mail.support.TencentMailboxFactory;
import org.springframework.context.annotation.Bean;

public class MailboxConfig {

    @Bean
    public AbstractMailboxFactory neteaseMailboxFactory() {
        return new NeteaseMailboxFactory();
    }

    @Bean
    public AbstractMailboxFactory tencentMailboxFactory() {
        return new TencentMailboxFactory();
    }

    @Bean
    public AbstractMailboxFactory gmailMailboxFactory() {
        return new GmailMailboxFactory();
    }
}
