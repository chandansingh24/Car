package com.carcomehome.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carcomehome.domain.UserPayment;
import com.carcomehome.repository.UserPaymentRepository;
import com.carcomehome.service.UserPaymentService;

@Service
public class UserPaymentServiceImpl implements UserPaymentService {
   
	@Autowired
	private UserPaymentRepository userPaymentRepository;
	
	@Override
	public UserPayment findById(Long id) {
		
		return userPaymentRepository.findById(id).get();
	}
	
	@Override
	public void removeById(Long id) {
		userPaymentRepository.deleteById(id);
	}


}
