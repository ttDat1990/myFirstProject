package com.myfirstweb.model;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CustomerDto implements Serializable {
	
	private int customerId;	
	@NotEmpty (message = "Name must be entered")
	@Length(min = 5, message = "Length of name must be greater than 5 characters")
	private String name;	
	@NotEmpty
	@Email
	private String email;	
	@NotEmpty (message = "Password must be entered")
	@Length(min = 6, message = "Length of password must be greater than 6 characters")
	private String password;
	@NotEmpty (message = "Phone number must be entered")
	@Size(min=9, max=10, message = "Quantity of phone's number is 9 or 10 numbers")
	private String phone;	
	private Date registeredDate;
	private short status;
}
