package com.tsengfhy.vservice.basic.mail;

import com.sun.mail.smtp.SMTPMessage;
import com.tsengfhy.vservice.basic.exception.mail.MailReceiveException;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Mailbox {

    private final Session session;

    public Mailbox(Session session) {
        this.session = session;
    }

    public MimeMessage create() {
        return new SMTPMessage(this.session);
    }

    public void send(PasswordAuthentication authentication, MimeMessage... mimeMessages) throws MailException {

        Map<Object, Exception> failedMessages = new LinkedHashMap<>();
        try (Transport transport = session.getTransport()) {
            transport.connect(authentication.getUserName(), authentication.getPassword());

            for (MimeMessage mimeMessage : mimeMessages) {
                try {
                    if (mimeMessage.getSentDate() == null) {
                        mimeMessage.setSentDate(new Date());
                    }

                    String messageId = mimeMessage.getMessageID();
                    mimeMessage.saveChanges();
                    if (messageId != null) {
                        mimeMessage.setHeader("Message-ID", messageId);
                    }

                    transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
                } catch (MessagingException e) {
                    failedMessages.put(mimeMessage, e);
                }
            }
        } catch (AuthenticationFailedException e) {
            throw new MailAuthenticationException(MessageSourceUtils.getMessage("Mail.authenticateFailure", "Mail server authenticate failed"), e);
        } catch (MessagingException e) {
            throw new MailPreparationException(MessageSourceUtils.getMessage("Mail.connectFailure", "Mail server connect failed"), e);
        }

        if (!failedMessages.isEmpty()) {
            throw new MailSendException(failedMessages);
        }
    }

    public void receive(PasswordAuthentication authentication, Consumer<Folder> consumer) throws MailException {

        try (Store store = session.getStore()) {
            store.connect(authentication.getUserName(), authentication.getPassword());

            try (Folder folder = store.getFolder("INBOX")) {
                folder.open(Folder.READ_WRITE);

                consumer.accept(folder);
            } catch (MessagingException e) {
                throw new MailReceiveException(e);
            }
        } catch (AuthenticationFailedException e) {
            throw new MailAuthenticationException(MessageSourceUtils.getMessage("Mail.authenticateFailure", "Mail server authenticate failed"), e);
        } catch (MessagingException e) {
            throw new MailPreparationException(MessageSourceUtils.getMessage("Mail.connectFailure", "Mail server connect failed"), e);
        }
    }
}
