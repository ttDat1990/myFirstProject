package com.myfirstweb.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CustomerChangePassDto implements Serializable {
		
	@NotEmpty (message = "Password must be entered")
	private String password;
	
	@NotEmpty (message = "New password must be entered")
	@Length(min = 6, message = "Length of password must be greater than 6 characters")
	private String newpassword1;

	@NotEmpty (message = "New password repeat must be entered")
	@Length(min = 6, message = "Length of password must be greater than 6 characters")
	private String newpassword2;
}
