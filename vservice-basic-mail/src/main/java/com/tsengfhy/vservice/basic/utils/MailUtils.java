package com.tsengfhy.vservice.basic.utils;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;
import com.sun.xml.internal.ws.encoding.DataHandlerDataSource;
import com.tsengfhy.vservice.basic.dto.mail.MailMessageDto;
import com.tsengfhy.vservice.basic.mail.Mailbox;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.lang.Nullable;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@UtilityClass
public final class MailUtils {

    public static MailMessageDto toDto(Message message) throws MailParseException {

        try {
            MailMessageDto dto = new MailMessageDto();

            dto.setUid(getUid(message));
            Optional.ofNullable(getFrom(message)).ifPresent(pair -> dto.setFrom(pair.getKey(), pair.getValue()));
            Optional.ofNullable(getReplyTo(message)).ifPresent(pair -> dto.setReplyTo(pair.getKey(), pair.getValue()));
            getRecipients(message, Message.RecipientType.TO).forEach(dto::addTo);
            getRecipients(message, Message.RecipientType.CC).forEach(dto::addCc);
            getRecipients(message, Message.RecipientType.BCC).forEach(dto::addBcc);
            dto.setNeedReceipt(isNeedReceipt(message));
            dto.setSubject(message.getSubject());
            dto.setText(getText(message));
            dto.setSentDate(message.getSentDate());
            getDataSource(message, Part.INLINE).forEach(dto::addInline);
            getDataSource(message, Part.ATTACHMENT).forEach(dto::addAttachment);

            return dto;
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
    }

    public static Message toMessage(MailMessageDto dto, Mailbox mailbox) throws MailParseException {

        try {
            MimeMessage message = mailbox.create();
            boolean isMultipart = !dto.getInline().isEmpty() || !dto.getAttachment().isEmpty();
            MimeMessageHelper helper = new MimeMessageHelper(message, isMultipart);

            helper.setFrom(dto.getFrom().getKey(), dto.getFrom().getValue());
            Map.Entry<String, String> replyTo = dto.getReplyTo();
            if (replyTo != null) {
                helper.setReplyTo(replyTo.getKey(), replyTo.getValue());
            }
            for (Map.Entry<String, String> entry : dto.getTo().entrySet()) {
                helper.addTo(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, String> entry : dto.getCc().entrySet()) {
                helper.addCc(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, String> entry : dto.getBcc().entrySet()) {
                helper.addBcc(entry.getKey(), entry.getValue());
            }
            if (dto.isNeedReceipt()) {
                String notificationTo = Arrays.stream(Optional.ofNullable(message.getFrom()).orElseGet(() -> new Address[0]))
                        .findFirst()
                        .map(address -> new StringBuilder(Optional.ofNullable(((InternetAddress) address).getPersonal()).orElse(""))
                                .append("<")
                                .append(Optional.ofNullable(((InternetAddress) address).getAddress()).orElse(""))
                                .append(">")
                                .toString())
                        .orElse(null);
                if (StringUtils.isNotBlank(notificationTo)) {
                    message.setHeader("Disposition-Notification-To", notificationTo);
                }
            }
            helper.setSubject(dto.getSubject());
            helper.setText(dto.getText(), DataUtils.isHtml(dto.getText()));
            Date sendDate = dto.getSentDate();
            if (sendDate != null) {
                helper.setSentDate(sendDate);
            }
            for (Map.Entry<String, DataSource> entry : dto.getInline().entrySet()) {
                helper.addInline(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, DataSource> entry : dto.getAttachment().entrySet()) {
                helper.addAttachment(entry.getKey(), entry.getValue());
            }

            return message;
        } catch (MessagingException | IOException e) {
            throw new MailParseException(e);
        }
    }

    @Nullable
    public static String getUid(Message message) {

        try {
            Folder folder = message.getFolder();
            if (folder instanceof IMAPFolder) {
                IMAPFolder imapFolder = (IMAPFolder) folder;
                return String.valueOf(imapFolder.getUID(message));
            } else if (folder instanceof POP3Folder) {
                POP3Folder pop3Folder = (POP3Folder) folder;
                return pop3Folder.getUID(message);
            }

            return null;
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
    }

    @Nullable
    public static Pair<String, String> getFrom(Message message) throws MailParseException {

        try {
            return Arrays.stream(Optional.ofNullable(message.getFrom()).orElseGet(() -> new Address[0]))
                    .findFirst()
                    .map(address -> Pair.of(((InternetAddress) address).getAddress(), ((InternetAddress) address).getPersonal()))
                    .orElse(null);
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
    }

    @Nullable
    public static Pair<String, String> getReplyTo(Message message) throws MailParseException {

        try {
            return Arrays.stream(Optional.ofNullable(message.getReplyTo()).orElseGet(() -> new Address[0]))
                    .findFirst()
                    .map(address -> Pair.of(((InternetAddress) address).getAddress(), ((InternetAddress) address).getPersonal()))
                    .orElse(null);
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
    }

    public static Map<String, String> getRecipients(Message message, Message.RecipientType recipientType) throws MailParseException {

        try {
            return Arrays.stream(Optional.ofNullable(message.getRecipients(recipientType)).orElseGet(() -> new Address[0]))
                    .collect(Collectors.toMap(address -> ((InternetAddress) address).getAddress(), address -> ((InternetAddress) address).getPersonal()));
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
    }

    public static boolean isNeedReceipt(Message message) throws MailParseException {

        try {
            return Optional.ofNullable(message.getHeader("Disposition-Notification-To")).isPresent();
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
    }

    public static String getText(Part part) throws MailParseException {

        try {
            StringBuilder sb = new StringBuilder();
            boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;

            if (part.isMimeType("multipart/*")) {
                Multipart multipart = (Multipart) part.getContent();
                int partCount = multipart.getCount();
                for (int i = 0; i < partCount; i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    sb.append(getText(bodyPart));
                }
            } else if (part.isMimeType("text/*") && !isContainTextAttach) {
                sb.append((String) part.getContent());
            } else if (part.isMimeType("message/rfc822")) {
                sb.append(getText((Part) part.getContent()));
            }

            return sb.toString();
        } catch (MessagingException | IOException e) {
            throw new MailParseException(e);
        }
    }

    private static final Pattern CONTENT_ID_PATTERN = Pattern.compile("^<([\\s\\S]*)>$");

    public static Map<String, DataSource> getDataSource(Part part, String type) throws MailParseException {

        try {
            Map<String, DataSource> map = new HashMap<>();

            if (part.isMimeType("multipart/*")) {
                Multipart multipart = (Multipart) part.getContent();
                int partCount = multipart.getCount();
                for (int i = 0; i < partCount; i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    map.putAll(getDataSource(bodyPart, type));
                }
            } else if (part.isMimeType("text/*")) {

            } else if (part.isMimeType("message/rfc822")) {
                map.putAll(getDataSource((Part) part.getContent(), type));
            } else if (part.getHeader("Content-ID") != null) {
                if (Part.INLINE.equals(type)) {
                    map.put(
                            Arrays.stream(part.getHeader("Content-ID"))
                                    .map(CONTENT_ID_PATTERN::matcher)
                                    .filter(Matcher::find)
                                    .findAny()
                                    .map(matcher -> matcher.group(1))
                                    .orElseThrow(() -> new MessagingException("No Content-ID")),
                            new DataHandlerDataSource(part.getDataHandler())
                    );
                }
            } else {
                if (Part.ATTACHMENT.equals(type)) {
                    map.put(MimeUtility.decodeText(part.getFileName()), new DataHandlerDataSource(part.getDataHandler()));
                }
            }

            return map;
        } catch (MessagingException | IOException e) {
            throw new MailParseException(e);
        }
    }
}
