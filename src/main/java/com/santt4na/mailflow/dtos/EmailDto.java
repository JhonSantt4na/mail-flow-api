package com.santt4na.mailflow.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.message.Message;

@Data
public class EmailDto {

	@NotBlank
	@Email(message = "Email should be valid")
	private String emailTarget;
	
	@Size(min = 10, max = 200, message = "About Me must be between 10 and 200 characters")
	private String title;
	
	private String message;
	
	private String name;
	
	public EmailDto(String emailTarget, String title, String message, String name) {
		this.emailTarget = emailTarget;
		this.title = title;
		this.message = message;
		this.name = name;
	}
}
