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
public class AdminLoginDto implements Serializable {
	@NotEmpty
	@Length(min = 6)
	private String username;
	@NotEmpty
	@Length(min = 6)
	private String password;
	
	private Boolean rememberMe = false;
}
