package com.counter.controller;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailTest {

    public static void main(String[] args) {
        final String username = "hskahsdk@gmail.com";
        final String password = "orwu tond qora ashl sada dawf"; // 或者應用密碼

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
          });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("dobuyC12345@gmail.com"));
            message.setSubject("測試郵件");
            message.setText("這是一封測試郵件，如果你收到這封郵件，說明郵件發送功能正常。");

            Transport.send(message);

            System.out.println("郵件發送成功！");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("郵件發送失敗：" + e.getMessage());
        }
    }
}