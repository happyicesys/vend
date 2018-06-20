package com.brian.sendmail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
public class SendMail {
    private static final String ALIDM_SMTP_HOST = "smtp.webfaction.com";
    
    public static void Send(String subject, String content) {
        final Properties props = new Properties();
        
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", ALIDM_SMTP_HOST);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.port", "465");
        props.put("mail.user", "system_happyice");
        props.put("mail.password", "1234574");
        
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };

        Session mailSession = Session.getInstance(props, authenticator);
        MimeMessage message = new MimeMessage(mailSession);
        try {
        InternetAddress from = new InternetAddress("system@happyice.com.sg");
        message.setFrom(from);
       // InternetAddress to = new InternetAddress("leehongjie91@gmail.com");
        String to = "brianlee@happyice.com.my, daniel.ma@happyice.com.sg, kent@happyice.com.sg, jiahaur@happyice.com.sg";
       // message.setRecipient(MimeMessage.RecipientType.TO, to);
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject(subject);
        message.setContent(content, "text/html;charset=UTF-8");

        Transport.send(message);
        }
        catch (MessagingException e) {
            String err = e.getMessage();
            System.out.println(err);
        }
    }    
}