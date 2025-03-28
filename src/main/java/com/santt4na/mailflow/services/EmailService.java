package com.santt4na.mailflow.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.santt4na.mailflow.dtos.EmailDto;

@Service
public class EmailService {

  private final JavaMailSender javaMailSender;

  public EmailService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  public void sendEmail(EmailDto emailDto) {
    var message = new SimpleMailMessage();
    message.setFrom("noreplay@email.com");
    message.setTo(emailDto.emailTarget());
    message.setSubject("Jhonn Santt4na");
    message.setText(emailDto.message());
    javaMailSender.send(message);
  }

}
