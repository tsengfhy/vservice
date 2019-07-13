package com.tsengfhy.vservice.basic.template.impl;

import com.tsengfhy.vservice.basic.dto.mail.MailMessageDto;
import com.tsengfhy.vservice.basic.exception.mail.MailReceiveException;
import com.tsengfhy.vservice.basic.exception.mail.NoSuchMailException;
import com.tsengfhy.vservice.basic.mail.Mailbox;
import com.tsengfhy.vservice.basic.mail.support.MailboxFactory;
import com.tsengfhy.vservice.basic.repository.SysMailAccountRepository;
import com.tsengfhy.vservice.basic.template.MailTemplate;
import com.tsengfhy.vservice.basic.utils.DataUtils;
import com.tsengfhy.vservice.basic.utils.MailUtils;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.util.Assert;

import javax.mail.Flags;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SearchTerm;
import java.util.*;
import java.util.stream.Collectors;

@Setter
public class JavaMailTemplate implements MailTemplate, InitializingBean {

    private MailboxFactory mailboxFactory;

    private SysMailAccountRepository sysMailAccountRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.mailboxFactory, "MailboxFactory must not be null!");
        Assert.notNull(this.sysMailAccountRepository, "SysMailAccountRepository must not be null!");
    }

    @Override
    public void send(MailMessageDto... dtos) throws MailException {

        Arrays.stream(dtos)
                .filter(dto -> dto.getFrom() != null)
                .collect(Collectors.groupingBy(dto -> dto.getFrom().getKey()))
                .forEach((username, list) -> {
                    Mailbox mailbox = mailboxFactory.create(username);
                    mailbox.send(this.getAuthentication(username), list.stream().map(dto -> MailUtils.toMessage(dto, mailbox)).collect(Collectors.toList()).toArray(new MimeMessage[list.size()]));
                });
    }

    @Override
    public MailMessageDto findByUid(String username, String uid) throws MailException {

        List<MailMessageDto> list = new ArrayList<>();
        mailboxFactory.create(username).receive(this.getAuthentication(username), folder -> {

            try {
                Arrays.stream(folder.getMessages())
                        .filter(message -> Optional.ofNullable(MailUtils.getUid(message)).map(var -> var.equals(uid)).orElse(false))
                        .findFirst()
                        .map(MailUtils::toDto)
                        .ifPresent(list::add);
            } catch (MessagingException e) {
                throw new MailReceiveException(e);
            }
        });

        if (list.isEmpty()) {
            throw new NoSuchMailException(MessageSourceUtils.getMessage("Mail.noMail", new Object[]{uid}, "No mail with uid: " + uid));
        }
        return list.get(0);
    }

    @Override
    public List<MailMessageDto> find(String username) throws MailException {
        return this.find(username, null);
    }

    @Override
    public List<MailMessageDto> find(String username, SearchTerm searchTerm, String... exceptUids) throws MailException {

        List<MailMessageDto> list = new ArrayList<>();
        Set<String> set = Arrays.stream(exceptUids).collect(Collectors.toSet());
        mailboxFactory.create(username).receive(this.getAuthentication(username), folder -> {

            try {
                list.addAll(
                        Arrays.stream(searchTerm == null ? folder.getMessages() : folder.search(searchTerm))
                                .filter(message -> Optional.ofNullable(MailUtils.getUid(message)).map(uid -> !set.contains(uid)).orElse(true))
                                .map(MailUtils::toDto)
                                .collect(Collectors.toList())
                );
            } catch (MessagingException e) {
                throw new MailReceiveException(e);
            }
        });

        return list;
    }

    @Override
    public void delete(String username, String... uids) throws MailException {

        Set<String> set = Arrays.stream(uids).collect(Collectors.toSet());
        mailboxFactory.create(username).receive(this.getAuthentication(username), folder -> {

            Map<Object, Exception> failedMessages = new LinkedHashMap<>();
            try {
                Arrays.stream(folder.getMessages())
                        .filter(message -> Optional.ofNullable(MailUtils.getUid(message)).map(set::contains).orElse(false))
                        .forEach(message -> {
                            try {
                                message.setFlag(Flags.Flag.DELETED, true);
                            } catch (MessagingException e) {
                                failedMessages.put(message, e);
                            }
                        });
            } catch (MessagingException e) {
                throw new MailReceiveException(e);
            }

            if (!failedMessages.isEmpty()) {
                throw new MailReceiveException(failedMessages);
            }
        });
    }

    private PasswordAuthentication getAuthentication(String username) throws MailException {

        return sysMailAccountRepository.findFirstByUsername(username)
                .map(sysMailAccount -> new PasswordAuthentication(sysMailAccount.getUsername(), DataUtils.decrypt(sysMailAccount.getPassword())))
                .orElseThrow(() -> new MailAuthenticationException(MessageSourceUtils.getMessage("Mail.noAccount", new Object[]{username}, "No account with username: " + username)));
    }
}
