package com.tsengfhy.vservice.basic.mail.support;

import com.mysema.commons.lang.Assert;
import com.tsengfhy.vservice.basic.mail.Mailbox;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;

import javax.mail.Session;
import java.util.Properties;

public abstract class AbstractMailboxFactory implements MailboxFactory {

    protected AbstractMailboxFactory() {
        Assert.notNull(this.getProperties(), "Properties must not be null");
    }

    @Override
    public Mailbox create() {
        return new Mailbox(Session.getInstance(this.getProperties()));
    }

    @Override
    public Mailbox create(String username) throws MailException {
        if (!this.supports(username)) {
            throw new MailPreparationException(MessageSourceUtils.getMessage("Mail.notSupport", new Object[]{username}, "Current mailbox not support to " + username));
        }

        return this.create();
    }

    public abstract boolean supports(String username);

    protected abstract Properties getProperties();
}
