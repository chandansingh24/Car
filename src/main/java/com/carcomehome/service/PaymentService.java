package com.carcomehome.service;

import com.carcomehome.domain.Payment;
import com.carcomehome.domain.UserPayment;

public interface PaymentService {
	Payment setByUserPayment(UserPayment userPayment, Payment payment);
}
