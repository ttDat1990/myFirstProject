package com.myfirstweb.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.myfirstweb.model.CartItemDto;
import com.myfirstweb.service.CartService;

@Service
@SessionScope
public class CartServiceImpl implements CartService{
	private Map<Long, CartItemDto> map = new HashMap<Long, CartItemDto>();
	
	@Override
	public void add(CartItemDto item) {
		CartItemDto existdItem = map.get(item.getProductId());
		if (existdItem != null) {
			existdItem.setQuantity(existdItem.getQuantity() + existdItem.getQuantity());
		} else {
			map.put(item.getProductId(), item);
		}
	}
	
	@Override
	public void remove(Long productId) {
		map.remove(productId);
	}
	
	@Override
	public Collection<CartItemDto> getCartItems() {
		return map.values();
	}
	
	@Override
	public void clear() {
		map.clear();
	}
	
	@Override
	public void update(Long productId, int quantity) {
		CartItemDto item = map.get(productId);
		item.setQuantity(quantity);
		
		if(item.getQuantity() <= 0) {
			map.remove(productId);
		}
	}
	
	@Override
	public double getAmount() {
		return map.values().stream().mapToDouble(item -> item.getQuantity() * item.getUnitPrice()).sum();
	}
	
	@Override
	public int getCount() {
		if(map.isEmpty()) return 0;
		return map.values().size();
	}
}
