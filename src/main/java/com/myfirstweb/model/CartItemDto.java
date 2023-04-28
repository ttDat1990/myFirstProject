package com.myfirstweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
	private Long productId;
	private String name;
	private int quantity;
	private double unitPrice;
	private String image;
	private double discount;


}
