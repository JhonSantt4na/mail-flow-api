package com.santt4na.mailflow.services;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.santt4na.mailflow.dtos.EmailDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

  private final JavaMailSender javaMailSender;

  public EmailService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  public void sendEmail(EmailDto emailDto) {
    var message = new SimpleMailMessage();
    message.setFrom("noreply@email.com");
    message.setTo(emailDto.emailTarget());
    message.setSubject(emailDto.title());
    message.setText(emailDto.message());
    javaMailSender.send(message);
  }
  
  public void sendEmailHtml(EmailDto emailDto){
    try {
      MimeMessage message = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      
      helper.setFrom("noreply@email.com");
      helper.setSubject(emailDto.title());
      helper.setTo(emailDto.emailTarget());
      
      String template = loadTemplate();
      template = template.replace("#[Nome]", emailDto.name());
      
      helper.setText(template,true);
      
      javaMailSender.send(message);
    } catch (Exception e) {
      System.out.println("Fail to send Email");
    }
  }
  
  public String loadTemplate() throws IOException {
    ClassPathResource resource = new ClassPathResource("templateEmail.html");
    return new String(resource.getInputStream().readAllBytes(),StandardCharsets.UTF_8);
  }
  
}
