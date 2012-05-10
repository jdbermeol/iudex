package org.xtremeware.iudex.businesslogic.service;

import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.xtremeware.iudex.businesslogic.InvalidVoException;
import org.xtremeware.iudex.helper.ExternalServiceConnectionException;
import org.xtremeware.iudex.helper.ValidityHelper;
import org.xtremeware.iudex.vo.MailingConfigVo;

public class MailingService {

    private Properties props;
    private MailingConfigVo config;

    public MailingService(MailingConfigVo config) {
        props = System.getProperties();
        props.put("mail.smtp.host", config.getSmtpServer());
        props.put("mail.smtp.port", config.getSmtpServerPort());
        //props.put("mail.smtp.user", config.getSmtpUser());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        /*
         * props.put("mail.smtp.socketFactory.port", "465");
         * props.put("mail.smtp.socketFactory.class",
         * "javax.net.ssl.SSLSocketFactory");
         * props.put("mail.smtp.socketFactory.fallback", "false");
         */

        this.config = config;

    }

    private void checkConfig(MailingConfigVo config) throws InvalidVoException {
        if (config == null) {
            throw new IllegalArgumentException(
                    "MailConfigVo config cannot be null");
        }
        if (!ValidityHelper.isValidDomain(config.getSmtpServer())) {
            throw new InvalidVoException(
                    "String smtpServer in the MailingConfigVo provided must be a valid domian.");
        }
        if (!ValidityHelper.isValidEmail(config.getSender())) {
            throw new InvalidVoException(
                    "String sender in the MailingConfigVo provided must be a valid email address");
        }
        if (config.getSmtpServerPort() > 65535 || config.getSmtpServerPort() < 0) {
            throw new InvalidVoException(
                    "int smtpServerPort in the MailingConfigVo provided must be in between 0 and 65535");
        }
        if (config.getSmtpUser() == null) {
            throw new InvalidVoException(
                    "String smtpServerUser in the MailingConfigVo provided cannot be null");
        }
        if (config.getSmtpPassword() == null) {
            throw new InvalidVoException(
                    "String smtpServerPassword in the MailingConfigVo provided cannot be null");
        }
    }

    public void sendMessage(String message, String subject, String receiver)
            throws ExternalServiceConnectionException {
        try {
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {

                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    config.getSmtpUser(),
                                    config.getSmtpPassword());
                        }
                    });
            // Create a new message
            Message msg = new MimeMessage(session);
            try {
                // Set the FROM field
                msg.setFrom(new InternetAddress(config.getSender()));
            } catch (AddressException ex) {
                throw new IllegalArgumentException(
                        "There was a problem while translating sender email address",
                        ex);
            }
            try {
                // Set the TO field
                msg.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(receiver, false));
            } catch (AddressException ex) {
                throw new IllegalArgumentException(
                        "There was a problem while translating receiver email address",
                        ex);
            }
            // Set the subject and content
            msg.setSubject(subject);
            msg.setContent(message, "text/html");
            msg.setSentDate(new Date());
            // Send the message
            msg.saveChanges();

            // Use Transport to deliver the message
            Transport.send(msg);
        } catch (MessagingException ex) {
            throw new ExternalServiceConnectionException(
                    "There was a problem while sending the email", ex);
        }
    }
}
