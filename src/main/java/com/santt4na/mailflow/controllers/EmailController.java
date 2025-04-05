package com.santt4na.mailflow.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.santt4na.mailflow.dtos.EmailDto;
import com.santt4na.mailflow.services.EmailService;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class EmailController {

  private final EmailService emailService;

  public EmailController(EmailService emailService) {
    this.emailService = emailService;
  }

  @PostMapping("/sendEmail")
  @ResponseStatus(HttpStatus.CREATED)
  public void sendEmail(@RequestBody @Valid EmailDto emailDto) {
    emailService.sendEmail(emailDto);
  }
  
  @PostMapping("/sendHtml")
  @ResponseStatus(HttpStatus.CREATED)
  public void sendEmailHtml(@RequestBody @Valid EmailDto emailDto) {
    emailService.sendEmailHtml(emailDto);
  }
  
  @PostMapping("/sendEmailAnexo")
  @ResponseStatus(HttpStatus.CREATED)
  public void sendEmailAnexo(@RequestPart @Valid EmailDto emailDto,
                             @RequestParam(value = "anexo", required = false) MultipartFile anexo) {
    emailService.sendEmailWithAttachment(emailDto, anexo);
  }
}
