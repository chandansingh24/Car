package com.carcomehome.service;

import com.carcomehome.domain.UserPayment;

public interface UserPaymentService {
	
	UserPayment findById(Long id);
	
	void removeById(Long id);

}
