package com.myfirstweb.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Order implements Serializable{
	
	private int orderId;	
	private Date orderDate;	
	private int customerId;	
	private double amount;
	private short status;
}
