package com.santt4na.mailflow.dtos;

import org.springframework.web.multipart.MultipartFile;

public record EmailDto(String emailTarget, String title, String message, String name) {
	
	public EmailDto(String emailTarget, String title, String message, String name) {
		this.emailTarget = emailTarget;
		this.title = title;
		this.message = message;
		this.name = name;
	}
}
