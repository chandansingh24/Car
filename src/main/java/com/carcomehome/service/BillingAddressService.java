package com.carcomehome.service;

import com.carcomehome.domain.BillingAddress;
import com.carcomehome.domain.UserBilling;

public interface BillingAddressService {
	BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);
}
