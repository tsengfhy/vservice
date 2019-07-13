package com.tsengfhy.vservice.basic.mail.support;


import com.tsengfhy.vservice.basic.mail.Mailbox;
import org.springframework.mail.MailException;

public interface MailboxFactory {

    Mailbox create();

    Mailbox create(String username) throws MailException;
}
