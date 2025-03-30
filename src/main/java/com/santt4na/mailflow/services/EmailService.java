package com.santt4na.mailflow.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.santt4na.mailflow.dtos.EmailDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


@Log4j2
@Service
public class EmailService {
  
  private final JavaMailSender javaMailSender;
  
  public EmailService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }
  
  public void sendEmail(EmailDto emailDto) {
    
    log.info("Starting email sending attempt to {}", emailDto.emailTarget());
    try {
      var message = new SimpleMailMessage();
      message.setFrom("noreply@email.com");
      message.setTo(emailDto.emailTarget());
      message.setSubject(emailDto.title());
      message.setText(emailDto.message());
      javaMailSender.send(message);
      log.info("Email successfully sent to {}", emailDto.emailTarget());
    
    } catch (Exception e) {
      log.error("Failed to send email to {}: {}", emailDto.emailTarget(), e.getMessage(), e);
    }
  }
  
  public void sendEmailHtml(EmailDto emailDto) {
    
    log.info("Starting HTML email sending attempt to {}", emailDto.emailTarget());
    try {
      MimeMessage message = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      
      helper.setFrom("noreply@email.com");
      helper.setSubject(emailDto.title());
      helper.setTo(emailDto.emailTarget());
      
      String template = loadTemplate();
      template = template.replace("#[Nome]", emailDto.name());
      helper.setText(template, true);
      
      javaMailSender.send(message);
      log.info("HTML email successfully sent to {}", emailDto.emailTarget());
    
    } catch (MessagingException | IOException e) {
      log.error("Failed to send HTML email to {}: {}", emailDto.emailTarget(), e.getMessage(), e);
    }
  }
  
  public void sendEmailWithAttachment(EmailDto emailDto, MultipartFile anexo ) {
    
    log.info("Starting email with attachment sending attempt to {}", emailDto.emailTarget());
    try {
      MimeMessage message = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      
      helper.setFrom("noreply@email.com");
      helper.setSubject(emailDto.title());
      helper.setTo(emailDto.emailTarget());
      helper.setText(emailDto.message());
      
      if (anexo != null && !anexo.isEmpty()) {
        String fileName = Objects.requireNonNull(anexo.getOriginalFilename());
        helper.addAttachment(fileName, anexo);
        log.info("Attachment '{}' added to email", fileName);
      }
      
      javaMailSender.send(message);
      log.info("Email with attachment successfully sent to {}", emailDto.emailTarget());
    
    } catch (MessagingException e) {
      log.error("Failed to send email with attachment to {}: {}", emailDto.emailTarget(), e.getMessage(), e);
    }
  }
  
  private String loadTemplate() throws IOException {
    
    ClassPathResource resource = new ClassPathResource("templateEmail.html");
    return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
  }
}