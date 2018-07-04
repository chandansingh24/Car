package com.carcomehome.service;

import com.carcomehome.domain.BillingAddress;
import com.carcomehome.domain.Order;
import com.carcomehome.domain.Payment;
import com.carcomehome.domain.ShippingAddress;
import com.carcomehome.domain.ShoppingCart;
import com.carcomehome.domain.User;

public interface OrderService {
	Order createOrder(ShoppingCart shoppingCart,
			ShippingAddress shippingAddress,
			BillingAddress billingAddress,
			Payment payment,
			String shippingMethod,
			User user);
	
	Order findOne(Long id);
}
