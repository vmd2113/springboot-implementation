package com.duongw.sbsecurity.controller;


import com.duongw.sbsecurity.DTO.response.ApiResponse;
import com.duongw.sbsecurity.security.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth/utils")
public class UtilsController {

    private final MailService mailService;

    @PostMapping("/send-email")
    public ResponseEntity<ApiResponse<?>> sendEmail(@RequestParam String recipient, @RequestParam String subject, @RequestParam String content, @RequestParam(value = "files", required = false) MultipartFile[] files) {
        log.info("---------- sendEmail ----------");
        try {
            mailService.sendSimpleMessage(recipient, subject, content, files);
            ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, "Email sent successfully");
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }


}
