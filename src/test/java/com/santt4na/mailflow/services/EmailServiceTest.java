package com.santt4na.mailflow.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.santt4na.mailflow.dtos.EmailDto;
import jakarta.mail.BodyPart;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

import jakarta.mail.internet.MimeMultipart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
	
	@Mock
	private JavaMailSender javaMailSender;
	
	@Spy
	@InjectMocks
	private EmailService emailService;
	
	private EmailDto emailDto;
	
	@BeforeEach
	void setUp() {
		emailDto = new EmailDto(
			"dest@example.com",
			"Test Subject",
			"Test Message",
			"John Doe"
		);
	}
	
	@Test
	void sendEmail_ShouldCallJavaMailSenderWithCorrectParameters() {
		emailService.sendEmail(emailDto);
		
		ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(javaMailSender).send(messageCaptor.capture());
		
		SimpleMailMessage sentMessage = messageCaptor.getValue();
		assertEquals("dest@example.com", sentMessage.getTo()[0]);
		assertEquals("Test Subject", sentMessage.getSubject());
		assertEquals("Test Message", sentMessage.getText());
	}
	
	@Test
	void sendEmail_ShouldLogErrorWhenMailExceptionOccurs() {
		MailException mockException = mock(MailException.class);
		doThrow(mockException).when(javaMailSender).send(any(SimpleMailMessage.class));
		
		emailService.sendEmail(emailDto);
		
		verify(javaMailSender).send(any(SimpleMailMessage.class));
	}
	
	@Test
	void sendEmailHtml_ShouldReplaceNamePlaceholderInTemplate() throws Exception {
		Properties props = new Properties();
		Session session = Session.getInstance(props);
		MimeMessage mimeMessage = new MimeMessage(session);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		
		doReturn("<html><body>Olá, #[Nome]!</body></html>").when(emailService).loadTemplate();
		
		emailService.sendEmailHtml(emailDto);
		
		String htmlContent = extrairHtml(mimeMessage.getContent());
		assertTrue(htmlContent.contains("John Doe"), "O conteúdo deve conter o nome 'John Doe'");
	}
	
	private String extrairHtml(Object content) throws Exception {
		if (content instanceof String) {
			return (String) content;
		} else if (content instanceof MimeMultipart) {
			MimeMultipart multipart = (MimeMultipart) content;
			for (int i = 0; i < multipart.getCount(); i++) {
				String result = extrairHtml(multipart.getBodyPart(i).getContent());
				if (result != null && !result.isEmpty()) {
					return result;
				}
			}
		}
		return "";
	}
	
	@Test
	void sendEmailHtml_ShouldNotSendEmailWhenIOExceptionOccurs() throws Exception {
		MimeMessage mimeMessage = mock(MimeMessage.class);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		
		doThrow(new IOException("Simulated error")).when(emailService).loadTemplate();
		
		emailService.sendEmailHtml(emailDto);
		
		verify(javaMailSender, never()).send(any(MimeMessage.class));
	}
	
	@Test
	void sendEmailWithAttachment_ShouldAddAttachmentWhenFileIsProvided() throws Exception {
		MultipartFile anexo = mock(MultipartFile.class);
		when(anexo.isEmpty()).thenReturn(false);
		when(anexo.getOriginalFilename()).thenReturn("file.txt");
		
		MimeMessage mimeMessage = mock(MimeMessage.class);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		
		emailService.sendEmailWithAttachment(emailDto, anexo);
		
		verify(anexo, times(1)).isEmpty();
		verify(anexo, times(1)).getOriginalFilename();
		verify(javaMailSender).send(mimeMessage);
	}
	
	@Test
	void sendEmailWithAttachment_ShouldNotAddAttachmentWhenFileIsEmpty() throws Exception {
		MultipartFile anexo = mock(MultipartFile.class);
		when(anexo.isEmpty()).thenReturn(true);
		
		MimeMessage mimeMessage = mock(MimeMessage.class);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		
		emailService.sendEmailWithAttachment(emailDto, anexo);
		
		verify(anexo, times(1)).isEmpty();
		verify(anexo, never()).getOriginalFilename();
		verify(javaMailSender).send(mimeMessage);
	}
	
	@Test
	void loadTemplate_ShouldReturnTemplateContent() throws IOException {
		String template = emailService.loadTemplate();
		assertNotNull(template);
		assertFalse(template.isEmpty());
	}
}
