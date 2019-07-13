package com.tsengfhy.vservice.basic.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.tsengfhy.vservice.basic.properties.Properties;
import com.tsengfhy.vservice.basic.template.SmsTemplate;
import com.tsengfhy.vservice.basic.template.impl.AliSmsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
public class SmsConfig {

    @PostConstruct
    public void init() {
        log.info("VService module [Sms] is loaded");
    }

    @Configuration
    @ConditionalOnClass({IAcsClient.class})
    @ConditionalOnProperty(value = "vservice.sms.type", havingValue = "ali")
    static class AliConfig {

        @Autowired
        private Properties properties;

        @Bean
        public IAcsClient acsClient() throws Exception {

            IClientProfile profile = DefaultProfile.getProfile("default", properties.getSms().getAccessKey(), properties.getSms().getAccessSecret());
            return new DefaultAcsClient(profile);
        }

        @Bean
        public SmsTemplate smsTemplate() throws Exception {

            AliSmsTemplate smsTemplate = new AliSmsTemplate();
            smsTemplate.setAcsClient(acsClient());
            smsTemplate.setDefaultSignName(properties.getSms().getSignName());

            return smsTemplate;
        }
    }
}
