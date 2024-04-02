package org.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public String readFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
        } catch (IOException ignored) {}
        return sb.toString();
    }


    public void sendEmailFromTemplate(String recipientAddress, String filePath, String subject, String password) {

        try {
            String htmlTemplate = readFile(filePath);
            MimeMessage message = mailSender.createMimeMessage();

            message.setFrom("proiectcolectiv732@outlook.com");
            message.setRecipients(MimeMessage.RecipientType.TO, recipientAddress);
            message.setSubject(subject);

            htmlTemplate = htmlTemplate.replace("{name}", recipientAddress.substring(0, recipientAddress.indexOf("@")));
            htmlTemplate = htmlTemplate.replace("{password}", password);

            message.setContent(htmlTemplate, "text/html; charset=utf-8");

            mailSender.send(message);
        } catch (MessagingException ignored) {}
    }
}
