package com.carcomehome.service;

import java.util.Date;
import java.util.List;

import com.carcomehome.domain.User;
import com.carcomehome.domain.UserBilling;
import com.carcomehome.domain.UserPayment;
import com.carcomehome.domain.UserShipping;
import com.carcomehome.domain.security.PasswordResetToken;
import com.carcomehome.domain.security.UserRole;
import com.carcomehome.enums.PlansEnum;

public interface UserService {
	
	PasswordResetToken getPasswordResetToken(final String token);
	
	void deleteAllExpiredSince(Date now);
	
	void createPasswordResetTokenForUser(final User user, final String token);

	User findByUsername(String username);
	
	User findByEmail(String email);
	
	User findById(Long id);
	
	User createUser(User user, PlansEnum plansEnum, List<UserRole> userRoles) throws Exception;
	
	User save(User user);
	
	void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);
	
	void updateUserShipping(UserShipping userShipping, User user);
	
	void  setUserDefaultPayment(Long userPaymentId, User user);
	
	void  setUserDefaultShipping(Long userShippingId, User user);
}
