package com.duongw.sbsecurity.security.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;


    private String sender = "vmd.2113@gmail.com";
    private final SpringTemplateEngine templateEngine;
    @Value("${server.name}")
    private String serverName;


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

    public void sendConfirmLink(String emailTo, String resetToken) throws MessagingException, UnsupportedEncodingException {
        log.info("Sending confirming link to user, email={}", emailTo);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();

        String linkConfirm = String.format("%s/auth/confirm-email?code=%s", serverName, resetToken);

        Map<String, Object> properties = new HashMap<>();
        properties.put("linkConfirm", linkConfirm);
        context.setVariables(properties);

        helper.setFrom(sender, "Van Minh Duong");
        helper.setTo(emailTo);
        helper.setSubject("Please confirm your account");
        String html = templateEngine.process("/confirm-email.html", context);
        helper.setText(html, true);

        javaMailSender.send(message);
        log.info("Confirming link has sent to user, email={}, linkConfirm={}", emailTo, linkConfirm);
    }
}
