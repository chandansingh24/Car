package com.carcomehome.service;

import java.util.List;

import com.carcomehome.domain.Car;
import com.carcomehome.domain.CartItem;
import com.carcomehome.domain.Order;
import com.carcomehome.domain.ShoppingCart;
import com.carcomehome.domain.User;




public interface CartItemService {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	CartItem updateCartItem(CartItem cartItem);
	
	CartItem addcarToCartItem(Car car, User user, int qty);
	
	CartItem findById(Long id);
	
	void removeCartItem(CartItem cartItem);
	
	CartItem save(CartItem cartItem);
	
	List<CartItem> findByOrder(Order order);
}
