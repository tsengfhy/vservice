package com.tsengfhy.vservice.basic.exception.sms;

import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public class SmsSendException extends SmsException {

    private static final long serialVersionUID = 0L;

    private final transient Map<String, Exception> failedPhones;

    @Nullable
    private Exception[] smsExceptions;

    public SmsSendException(String message) {
        this(message, (Throwable) null);
    }

    public SmsSendException(Throwable cause) {
        this(null, cause);
    }

    public SmsSendException(String message, @Nullable Throwable cause) {
        super(message, cause);
        this.failedPhones = new LinkedHashMap();
    }

    public SmsSendException(@Nullable String message, @Nullable Throwable cause, Map<String, Exception> failedMessages) {
        super(message, cause);
        this.failedPhones = new LinkedHashMap(failedMessages);
        this.smsExceptions = (Exception[]) failedMessages.values().toArray(new Exception[0]);
    }

    public SmsSendException(Map<String, Exception> failedMessages) {
        this((String) null, (Throwable) null, failedMessages);
    }

    public final Map<String, Exception> getFailedPhones() {
        return this.failedPhones;
    }

    public final Exception[] getSmsExceptions() {
        return this.smsExceptions != null ? this.smsExceptions : new Exception[0];
    }

    @Override
    @Nullable
    public String getMessage() {
        if (ObjectUtils.isEmpty(this.smsExceptions)) {
            return super.getMessage();
        } else {
            StringBuilder sb = new StringBuilder();
            String baseMessage = super.getMessage();
            if (baseMessage != null) {
                sb.append(baseMessage).append(". ");
            }

            sb.append("Failed sms: ");

            for (int i = 0; i < this.smsExceptions.length; ++i) {
                Exception subEx = this.smsExceptions[i];
                sb.append(subEx.toString());
                if (i < this.smsExceptions.length - 1) {
                    sb.append("; ");
                }
            }

            return sb.toString();
        }
    }

    @Override
    public String toString() {
        if (ObjectUtils.isEmpty(this.smsExceptions)) {
            return super.toString();
        } else {
            StringBuilder sb = new StringBuilder(super.toString());
            sb.append("; sms exceptions (").append(this.smsExceptions.length).append(") are:");

            for (int i = 0; i < this.smsExceptions.length; ++i) {
                Exception subEx = this.smsExceptions[i];
                sb.append('\n').append("Failed sms ").append(i + 1).append(": ");
                sb.append(subEx);
            }

            return sb.toString();
        }
    }

    @Override
    public void printStackTrace(PrintStream ps) {
        if (ObjectUtils.isEmpty(this.smsExceptions)) {
            super.printStackTrace(ps);
        } else {
            ps.println(super.toString() + "; sms exception details (" + this.smsExceptions.length + ") are:");

            for (int i = 0; i < this.smsExceptions.length; ++i) {
                Exception subEx = this.smsExceptions[i];
                ps.println("Failed sms " + (i + 1) + ":");
                subEx.printStackTrace(ps);
            }
        }

    }

    @Override
    public void printStackTrace(PrintWriter pw) {
        if (ObjectUtils.isEmpty(this.smsExceptions)) {
            super.printStackTrace(pw);
        } else {
            pw.println(super.toString() + "; sms exception details (" + this.smsExceptions.length + ") are:");

            for (int i = 0; i < this.smsExceptions.length; ++i) {
                Exception subEx = this.smsExceptions[i];
                pw.println("Failed sms " + (i + 1) + ":");
                subEx.printStackTrace(pw);
            }
        }

    }
}
