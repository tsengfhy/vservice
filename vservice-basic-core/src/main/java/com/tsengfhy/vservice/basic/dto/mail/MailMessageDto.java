package com.tsengfhy.vservice.basic.dto.mail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.mail.MailMessage;
import org.springframework.mail.MailParseException;

import javax.activation.DataSource;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@ApiModel("MailMessage")
public class MailMessageDto implements MailMessage, Serializable {

    private static final long serialVersionUID = 0L;

    @Setter
    @ApiModelProperty(value = "Uid", position = 1)
    private String uid;

    @ApiModelProperty(value = "From", position = 2, example = "Tsengfhy<tsengfhy@gmail.com>")
    private Pair<String, String> from;

    @ApiModelProperty(value = "ReplyTo", position = 3, allowEmptyValue = true, example = "Tsengfhy<tsengfhy@gmail.com>")
    private Pair<String, String> replyTo;

    @ApiModelProperty(value = "To", position = 4)
    private Map<String, String> to = new HashMap<>();

    @ApiModelProperty(value = "Cc", position = 5, allowEmptyValue = true)
    private Map<String, String> cc = new HashMap<>();

    @ApiModelProperty(value = "Bcc", position = 6, allowEmptyValue = true)
    private Map<String, String> bcc = new HashMap<>();

    @Setter
    @ApiModelProperty(value = "NeedReceipt", position = 7, allowEmptyValue = true, example = "false")
    private boolean needReceipt;

    @Setter
    @ApiModelProperty(value = "Subject", position = 8)
    private String subject;

    @Setter
    @ApiModelProperty(value = "Text", position = 9)
    private String text;

    @Setter
    @ApiModelProperty(value = "SentDate", position = 10, allowEmptyValue = true)
    private Date sentDate;

    @ApiModelProperty(hidden = true)
    private transient Map<String, DataSource> inline = new HashMap<>();

    @ApiModelProperty(hidden = true)
    private transient Map<String, DataSource> attachment = new HashMap<>();

    public void setFrom(String from) throws MailParseException {
        this.from = Pair.of(from, null);
    }

    public void setFrom(String from, String personal) throws MailParseException {
        this.from = Pair.of(from, personal);
    }

    public void setReplyTo(String replyTo) throws MailParseException {
        this.replyTo = Pair.of(replyTo, null);
    }

    public void setReplyTo(String replyTo, String personal) throws MailParseException {
        this.replyTo = Pair.of(replyTo, personal);
    }

    public void setTo(String to) throws MailParseException {
        this.setTo(to);
    }

    public void setTo(String... to) throws MailParseException {
        this.to.clear();
        this.to.putAll(Arrays.stream(to).collect(Collectors.toMap(item -> item, item -> null)));
    }

    public void addTo(String to, String personal) throws MailParseException {
        this.to.put(to, personal);
    }

    public void setCc(String cc) throws MailParseException {
        this.setCc(cc);
    }

    public void setCc(String... cc) throws MailParseException {
        this.cc.clear();
        this.to.putAll(Arrays.stream(cc).collect(Collectors.toMap(item -> item, item -> null)));
    }

    public void addCc(String cc, String personal) throws MailParseException {
        this.cc.put(cc, personal);
    }

    public void setBcc(String bcc) throws MailParseException {
        this.setBcc(bcc);
    }

    public void setBcc(String... bcc) throws MailParseException {
        this.bcc.clear();
        this.to.putAll(Arrays.stream(bcc).collect(Collectors.toMap(item -> item, item -> null)));
    }

    public void addBcc(String bcc, String personal) throws MailParseException {
        this.bcc.put(bcc, personal);
    }

    public void addInline(String contentId, DataSource dataSource) throws MailParseException {
        this.inline.put(contentId, dataSource);
    }

    public void addAttachment(String attachmentName, DataSource dataSource) throws MailParseException {
        this.attachment.put(attachmentName, dataSource);
    }
}
