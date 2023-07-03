package com.example.musicmate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String toEmail,
                          String body,
                          String subject) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("soundhubkg@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        emailSender.send(message);
    }
}


