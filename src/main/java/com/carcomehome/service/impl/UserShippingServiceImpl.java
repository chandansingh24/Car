package com.carcomehome.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carcomehome.domain.UserShipping;
import com.carcomehome.repository.UserShippingRepository;
import com.carcomehome.service.UserShippingService;



@Service
public class UserShippingServiceImpl implements UserShippingService {

	@Autowired
	private UserShippingRepository userShippingRepository;
	
	public UserShipping findById(Long id) {
		return userShippingRepository.findById(id).get();
	}	

	@Override
	public void removeById(Long id) {
		userShippingRepository.deleteById(id);		
	}	
	
}
