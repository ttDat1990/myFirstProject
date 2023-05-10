package com.myfirstweb.model;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CustomerLoginDto implements Serializable {
	
	@NotEmpty (message = "Email must be entered")
	private String email;	
	@NotEmpty (message = "Password must be entered")
	private String password;
	private Boolean rememberMe = false;
}
