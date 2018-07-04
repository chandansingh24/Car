package com.carcomehome.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carcomehome.domain.Car;
import com.carcomehome.domain.CarToCartItem;
import com.carcomehome.domain.CartItem;
import com.carcomehome.domain.Order;
import com.carcomehome.domain.ShoppingCart;
import com.carcomehome.domain.User;
import com.carcomehome.repository.CarToCartItemRepository;
import com.carcomehome.repository.CartItemRepository;
import com.carcomehome.service.CartItemService;



@Service
public class CartItemServiceImpl implements CartItemService {
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private CarToCartItemRepository carToCartItemRepository;
	

	@Override
	public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {
		return cartItemRepository.findByShoppingCart(shoppingCart);
	}


	@Override
	public CartItem updateCartItem(CartItem cartItem) {
		BigDecimal bigDecimal = new BigDecimal(cartItem.getCar().getOurPrice()).multiply(new BigDecimal(cartItem.getQty()));
		
		bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		cartItem.setSubtotal(bigDecimal);
		
		cartItemRepository.save(cartItem);
		
		return cartItem;
	}


	@Override
	public CartItem addcarToCartItem(Car car, User user, int qty) {
		List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());
		
		for (CartItem cartItem : cartItemList) {
			if(car.getId() == cartItem.getCar().getId()) {
				cartItem.setQty(cartItem.getQty() + qty);
				cartItem.setSubtotal(new BigDecimal(car.getOurPrice()).multiply(new BigDecimal(qty)));
				cartItemRepository.save(cartItem);
				return cartItem;
			}
		}
		
		CartItem cartItem = new CartItem();
		cartItem.setShoppingCart(user.getShoppingCart());
		cartItem.setCar(car);
		
		cartItem.setQty(qty);
		cartItem.setSubtotal(new BigDecimal(car.getOurPrice()).multiply(new BigDecimal(qty)));
		cartItem = cartItemRepository.save(cartItem);
		
		CarToCartItem carToCartItem = new CarToCartItem();
		carToCartItem.setCar(car);
		carToCartItem.setCartItem(cartItem);
		carToCartItemRepository.save(carToCartItem);
		
		return cartItem;			
		
	}


	@Override
	public CartItem findById(Long id) {
		return cartItemRepository.findById(id).get();
	}


	@Override
	public void removeCartItem(CartItem cartItem) {
		carToCartItemRepository.deleteByCartItem(cartItem);
		cartItemRepository.delete(cartItem);		
	}
	
	@Override
	public CartItem save(CartItem cartItem) {
		return cartItemRepository.save(cartItem);
	}


	@Override
	public List<CartItem> findByOrder(Order order) {
		return cartItemRepository.findByOrder(order);
	}
		
	
}
