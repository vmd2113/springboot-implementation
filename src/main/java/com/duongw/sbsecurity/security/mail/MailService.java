package com.duongw.sbsecurity.security.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;


    private String sender = "vmd.2113@gmail.com";


    public void sendSimpleMessage(String recipient, String subject, String content, MultipartFile[] files) {



        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(sender);
            helper.setSubject(subject);
            helper.setText(content, true);

            if (recipient.contains(",")) {
                List<String> list = List.of(recipient.split(","));
                list.forEach(e -> {
                    try {
                        helper.addCc(e);
                    } catch (MessagingException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            } else {
                helper.addCc(recipient);
            }

            if (files != null) {
                for (MultipartFile file : files) {
                    helper.addAttachment(file.getOriginalFilename(), file);
                }
            }

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(message);
        log.info("Message sent to {}", recipient);

    }
}
