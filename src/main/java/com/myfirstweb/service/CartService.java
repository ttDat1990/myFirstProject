package com.myfirstweb.service;

import java.util.Collection;
import com.myfirstweb.model.CartItemDto;

public interface CartService {

	int getCount();

	double getAmount();

	void update(Long productId, int quantity);

	void clear();

	Collection<CartItemDto> getCartItems();

	void remove(Long productId);

	void add(CartItemDto item);

	

}
