package vn.dathocjava.dathocjava_sample.service.implement.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine springTemplateEngine;
    @Value("${spring.mail.from}")
    private String emailFrom;

    public String sendEmail(String recipient,
                            String subject,
                            String content,
                            MultipartFile[] files) throws MessagingException, UnsupportedEncodingException {
        log.info("sending...");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(emailFrom, "Le Vu Thanh Dat");
        if (recipient.contains(",")) {
            helper.setTo(InternetAddress.parse(recipient));
        } else {
            helper.setTo(recipient);
        }
        if (files != null) {
            for (MultipartFile file : files) {
                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
            }
        }
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
        log.info("Successfully!");
        return "sent";
    }

    public void sendConfirmLink(String emailTo, Long userId, String secretcode) throws MessagingException, UnsupportedEncodingException {
        log.info("sending email confirm account");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
                );
        Context context = new Context();
        String linkConfirm=String.format("http://localhost:8081/user/confirm/%s?verifyCode=%s", userId, secretcode);
        log.info(linkConfirm);
        Map<String, Object> properties = new HashMap<>();
        properties.put("linkConfirm",linkConfirm);
        context.setVariables(properties);

        helper.setFrom(emailFrom,"Thanh Dat Le Vu");
        helper.setTo(emailTo);
        helper.setSubject("Please confirm your Account");

        String html = springTemplateEngine.process("confirm-email.html",context);
        helper.setText(html,true);
        mailSender.send(message);
        log.info("email sent");

    }
    @KafkaListener(topics = "confirm-account-topic", groupId = "confirm-account-group")
    public void sendConfirmLinkByKafka(String message) throws MessagingException, UnsupportedEncodingException {
        log.info("sending email confirm account");
        String[] arr = message.split(",");
        String emailTo = arr[0];
        String userId = arr[1];
        String secretcode = arr[2];
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );
        Context context = new Context();
        String linkConfirm=String.format("http://localhost:8081/user/confirm/%s?verifyCode=%s", userId, secretcode);
        log.info(linkConfirm);
        Map<String, Object> properties = new HashMap<>();
        properties.put("linkConfirm",linkConfirm);
        context.setVariables(properties);

        helper.setFrom(emailFrom,"Thanh Dat Le Vu");
        helper.setTo(emailTo);
        helper.setSubject("Please confirm your Account");

        String html = springTemplateEngine.process("confirm-email.html",context);
        helper.setText(html,true);
        mailSender.send(mimeMessage);
        log.info("email sent");

    }
}
