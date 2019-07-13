package com.tsengfhy.vservice.basic.mail.support;

import com.tsengfhy.vservice.basic.mail.Mailbox;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CompositeMailboxFactory implements MailboxFactory {

    private final List<AbstractMailboxFactory> mailboxFactories;

    private Map<String, Mailbox> map = new ConcurrentHashMap<>();

    public CompositeMailboxFactory(List<AbstractMailboxFactory> mailboxFactories) {

        this.mailboxFactories = Optional.ofNullable(mailboxFactories)
                .orElseThrow(() -> new IllegalArgumentException("Mailbox factory list must not be null!"))
                .stream()
                .peek(mailboxFactory -> Assert.notNull(mailboxFactory, "Mailbox factory list cannot contain null entries. Got " + mailboxFactories))
                .collect(Collectors.toList());
    }

    @Override
    public Mailbox create() {
        throw new UnsupportedOperationException(MessageSourceUtils.getMessage("Common.methodNotSupport", new Object[]{CompositeMailboxFactory.class.getName() + ".create()"}, "Method " + CompositeMailboxFactory.class.getName() + ".create() not support"));
    }

    @Override
    public Mailbox create(String username) throws MailException {

        return map.computeIfAbsent(username, (key) ->
                this.mailboxFactories
                        .stream()
                        .filter(mailboxFactory -> mailboxFactory.supports(key))
                        .findAny()
                        .map(MailboxFactory::create)
                        .orElseThrow(() -> new MailPreparationException(MessageSourceUtils.getMessage("Mail.noMailbox", new Object[]{key}, "No support mailbox to " + key)))
        );
    }
}
