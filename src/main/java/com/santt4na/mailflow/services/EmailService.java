package com.santt4na.mailflow.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
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
    String emailTarget = emailDto.getEmailTarget();
    log.info("Starting email sending attempt to {}", emailTarget);
    
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom("noreply@email.com");
      message.setTo(emailTarget);
      message.setSubject(emailDto.getTitle());
      message.setText(emailDto.getMessage());
      
      javaMailSender.send(message);
      log.info("Email successfully sent to {}", emailTarget);
    } catch (MailException e) {
      log.error("Failed to send email to {}: {}", emailTarget, e.getMessage(), e);
    }
  }
  
  public void sendEmailHtml(EmailDto emailDto) {
    String emailTarget = emailDto.getEmailTarget();
    log.info("Starting HTML email sending attempt to {}", emailTarget);
    
    try {
      MimeMessage message = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
      
      helper.setFrom("noreply@email.com");
      helper.setTo(emailTarget);
      helper.setSubject(emailDto.getTitle());
      
      String template = loadTemplate().replace("#[Nome]", emailDto.getName());
      helper.setText(template, true);
      
      javaMailSender.send(message);
      log.info("HTML email successfully sent to {}", emailTarget);
    } catch (MessagingException | IOException e) {
      log.error("Failed to send HTML email to {}: {}", emailTarget, e.getMessage(), e);
    }
  }
  
  public void sendEmailWithAttachment(EmailDto emailDto, MultipartFile anexo) {
    String emailTarget = emailDto.getEmailTarget();
    log.info("Starting email with attachment sending attempt to {}", emailTarget);
    
    try {
      MimeMessage message = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      
      helper.setFrom("noreply@email.com");
      helper.setTo(emailTarget);
      helper.setSubject(emailDto.getTitle());
      helper.setText(emailDto.getMessage());
      
      if (anexo != null && !anexo.isEmpty()) {
        String fileName = Objects.requireNonNull(anexo.getOriginalFilename());
        helper.addAttachment(fileName, anexo);
        log.info("Attachment '{}' added to email", fileName);
      }
      
      javaMailSender.send(message);
      log.info("Email with attachment successfully sent to {}", emailTarget);
    } catch (MessagingException e) {
      log.error("Failed to send email with attachment to {}: {}", emailTarget, e.getMessage(), e);
    }
  }
  
  public String loadTemplate() throws IOException {
    ClassPathResource resource = new ClassPathResource("templateEmail.html");
    return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
  }
}