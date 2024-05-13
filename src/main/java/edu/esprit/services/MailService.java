package edu.esprit.services;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailService {

    private Properties emailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable TLS as per Mailtrap's settings
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        return props;
    }

    public void sendEmail( String toEmail, String subject, String body) {
        Properties props = emailProperties();

        // Authenticating with your Mailtrap credentials
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication("wsmtriki@gmail.com", "facmdlzfgkttudxq");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("wsmtriki@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email Sent Successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
