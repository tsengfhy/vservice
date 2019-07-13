package com.tsengfhy.vservice.basic.config;

import com.tsengfhy.vservice.basic.mail.support.AbstractMailboxFactory;
import com.tsengfhy.vservice.basic.mail.support.CompositeMailboxFactory;
import com.tsengfhy.vservice.basic.mail.support.MailboxFactory;
import com.tsengfhy.vservice.basic.repository.SysMailAccountRepository;
import com.tsengfhy.vservice.basic.template.MailTemplate;
import com.tsengfhy.vservice.basic.template.impl.JavaMailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.activation.MimeType;
import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Slf4j
@Configuration
@ConditionalOnClass({MimeMessage.class, MimeType.class})
@Import(MailboxConfig.class)
public class MailConfig {

    @PostConstruct
    public void init() {
        log.info("VService module [Mail] is loaded");
    }

    @Bean
    public MailboxFactory mailboxFactory(List<AbstractMailboxFactory> mailboxFactoryList) throws Exception {
        return new CompositeMailboxFactory(mailboxFactoryList);
    }

    @Configuration
    @ConditionalOnClass({JpaRepository.class})
    @AutoConfigureAfter({JPAConfig.class})
    static class TemplateConfig {

        @Bean
        public MailTemplate mailTemplate(MailboxFactory mailboxFactory, SysMailAccountRepository sysMailAccountRepository) throws Exception {

            JavaMailTemplate mailTemplate = new JavaMailTemplate();

            mailTemplate.setMailboxFactory(mailboxFactory);
            mailTemplate.setSysMailAccountRepository(sysMailAccountRepository);

            return mailTemplate;
        }
    }
}
