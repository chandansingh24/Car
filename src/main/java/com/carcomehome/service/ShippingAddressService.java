package com.carcomehome.service;

import com.carcomehome.domain.ShippingAddress;
import com.carcomehome.domain.UserShipping;

public interface ShippingAddressService {
	
	ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);

}
