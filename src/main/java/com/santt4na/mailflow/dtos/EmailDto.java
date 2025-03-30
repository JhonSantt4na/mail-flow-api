package com.santt4na.mailflow.dtos;

public record EmailDto(String emailTarget, String title, String message, String name) {
	
	public EmailDto(String emailTarget, String title, String message) {
		this(emailTarget, title, message, "");
	}
}
