package com.santt4na.mailflow.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.santt4na.mailflow.dtos.EmailDto;
import com.santt4na.mailflow.services.EmailService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

  private final EmailService emailService;

  public EmailController(EmailService emailService) {
    this.emailService = emailService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void sendEmail(@RequestBody EmailDto emailDto) {
    emailService.sendEmail(emailDto);
  }

}
