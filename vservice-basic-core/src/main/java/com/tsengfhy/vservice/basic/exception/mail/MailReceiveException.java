package com.tsengfhy.vservice.basic.exception.mail;

import org.springframework.lang.Nullable;
import org.springframework.mail.MailException;
import org.springframework.util.ObjectUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public class MailReceiveException extends MailException {

    private static final long serialVersionUID = 0L;

    private final transient Map<Object, Exception> failedMessages;

    @Nullable
    private Exception[] messageExceptions;

    public MailReceiveException(String message) {
        this(message, (Throwable) null);
    }

    public MailReceiveException(Throwable cause) {
        this(null, cause);
    }

    public MailReceiveException(String message, @Nullable Throwable cause) {
        super(message, cause);
        this.failedMessages = new LinkedHashMap();
    }

    public MailReceiveException(@Nullable String message, @Nullable Throwable cause, Map<Object, Exception> failedMessages) {
        super(message, cause);
        this.failedMessages = new LinkedHashMap(failedMessages);
        this.messageExceptions = (Exception[]) failedMessages.values().toArray(new Exception[0]);
    }

    public MailReceiveException(Map<Object, Exception> failedMessages) {
        this((String) null, (Throwable) null, failedMessages);
    }

    public final Map<Object, Exception> getFailedMessages() {
        return this.failedMessages;
    }

    public final Exception[] getMessageExceptions() {
        return this.messageExceptions != null ? this.messageExceptions : new Exception[0];
    }

    @Override
    @Nullable
    public String getMessage() {
        if (ObjectUtils.isEmpty(this.messageExceptions)) {
            return super.getMessage();
        } else {
            StringBuilder sb = new StringBuilder();
            String baseMessage = super.getMessage();
            if (baseMessage != null) {
                sb.append(baseMessage).append(". ");
            }

            sb.append("Failed messages: ");

            for (int i = 0; i < this.messageExceptions.length; ++i) {
                Exception subEx = this.messageExceptions[i];
                sb.append(subEx.toString());
                if (i < this.messageExceptions.length - 1) {
                    sb.append("; ");
                }
            }

            return sb.toString();
        }
    }

    @Override
    public String toString() {
        if (ObjectUtils.isEmpty(this.messageExceptions)) {
            return super.toString();
        } else {
            StringBuilder sb = new StringBuilder(super.toString());
            sb.append("; message exceptions (").append(this.messageExceptions.length).append(") are:");

            for (int i = 0; i < this.messageExceptions.length; ++i) {
                Exception subEx = this.messageExceptions[i];
                sb.append('\n').append("Failed message ").append(i + 1).append(": ");
                sb.append(subEx);
            }

            return sb.toString();
        }
    }

    @Override
    public void printStackTrace(PrintStream ps) {
        if (ObjectUtils.isEmpty(this.messageExceptions)) {
            super.printStackTrace(ps);
        } else {
            ps.println(super.toString() + "; message exception details (" + this.messageExceptions.length + ") are:");

            for (int i = 0; i < this.messageExceptions.length; ++i) {
                Exception subEx = this.messageExceptions[i];
                ps.println("Failed message " + (i + 1) + ":");
                subEx.printStackTrace(ps);
            }
        }

    }

    @Override
    public void printStackTrace(PrintWriter pw) {
        if (ObjectUtils.isEmpty(this.messageExceptions)) {
            super.printStackTrace(pw);
        } else {
            pw.println(super.toString() + "; message exception details (" + this.messageExceptions.length + ") are:");

            for (int i = 0; i < this.messageExceptions.length; ++i) {
                Exception subEx = this.messageExceptions[i];
                pw.println("Failed message " + (i + 1) + ":");
                subEx.printStackTrace(pw);
            }
        }

    }
}
