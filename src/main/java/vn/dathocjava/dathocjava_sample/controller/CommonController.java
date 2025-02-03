package vn.dathocjava.dathocjava_sample.controller;

import io.swagger.v3.oas.annotations.media.Content;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.dathocjava.dathocjava_sample.response.ResponseData;
import vn.dathocjava.dathocjava_sample.service.implement.MailService;

import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {
    private final MailService mailService;
    @PostMapping("/send-email")
    public ResponseData<String> sendEmail(@RequestParam String receipients,
                                          @RequestParam String subject,
                                          @RequestParam String Content,
                                          @RequestParam(required = false) MultipartFile[] files
                                          ){
        try {
            return new ResponseData<>(HttpStatus.OK.value(), mailService.sendEmail(receipients, subject, Content, files));
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Sending email fail! = {}", e.getMessage());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "fail!");
        }


    }
}
