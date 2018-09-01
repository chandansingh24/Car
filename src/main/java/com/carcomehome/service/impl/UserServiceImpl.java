package com.carcomehome.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carcomehome.domain.ShoppingCart;
import com.carcomehome.domain.User;
import com.carcomehome.domain.UserBilling;
import com.carcomehome.domain.UserPayment;
import com.carcomehome.domain.UserShipping;
import com.carcomehome.domain.security.PasswordResetToken;
import com.carcomehome.domain.security.Plan;
import com.carcomehome.domain.security.Role;
import com.carcomehome.domain.security.UserRole;
import com.carcomehome.enums.PlansEnum;
import com.carcomehome.repository.PasswordResetTokenRepository;
import com.carcomehome.repository.PlanRepository;
import com.carcomehome.repository.RoleRepository;
import com.carcomehome.repository.UserPaymentRepository;
import com.carcomehome.repository.UserRepository;
import com.carcomehome.repository.UserShippingRepository;
import com.carcomehome.service.UserService;

@Service
// @Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PlanRepository planRepository;

	@Autowired
	private UserPaymentRepository userPaymentRepository;

	@Autowired
	private UserShippingRepository userShippingRepository;

	@Override
	public PasswordResetToken getPasswordResetToken(final String token) {
		return passwordResetTokenRepository.findByToken(token);
	}	
     
	
	@Override
	public void deleteAllExpiredSince(Date now) {
		passwordResetTokenRepository.deleteAllExpiredSince(now);		
	}


	@Override
	// @Transactional
	public void createPasswordResetTokenForUser(final User user, final String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id).get();
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional
	public User createUser(User user, PlansEnum plansEnum, List<UserRole> userRoles) throws Exception {

		Plan plan = new Plan(plansEnum);
		// It makes sure the plans exist in the database
		if (!planRepository.existsById(plansEnum.getId())) {
			plan = planRepository.save(plan);
		}

		User localUser = userRepository.findByUsername(user.getUsername());

		if (localUser != null) {
			LOG.info("user {} already exists. Nothing will be done", user.getUsername());
		} else {
			
			for (UserRole urs : userRoles) {
				 for (Role ur: urs.getRoles()) {
					 roleRepository.save(ur);					 
				 }				
			}
			
			user.getUserRoles().addAll(userRoles);

			ShoppingCart shoppingCart = new ShoppingCart();
			shoppingCart.setUser(user);
			user.setShoppingCart(shoppingCart); // Mutually binding together

			user.setUserShippingList(new ArrayList<UserShipping>());
			user.setUserPaymentList(new ArrayList<UserPayment>());
			
			localUser = userRepository.save(user);
			
			
		}
		return localUser;
	}
   
	
	/* @Override
	 @Transactional
	    public User createUser(User user, PlansEnum plansEnum, Set<UserRole> userRoles) {
        
	        Plan plan = new Plan(plansEnum);
	        // It makes sure the plans exist in the database
	        if (!planRepository.existsById(plansEnum.getId())) {
	            plan = planRepository.save(plan);
	        }

	        user.setPlan(plan);

	        for (UserRole ur : userRoles) {
	            roleRepository.save(ur.getRole());
	        }

	        user.getUserRoles().addAll(userRoles);
	        

			ShoppingCart shoppingCart = new ShoppingCart();
			shoppingCart.setUser(user);
			user.setShoppingCart(shoppingCart); // Mutually binding together

			user.setUserShippingList(new ArrayList<UserShipping>());
			user.setUserPaymentList(new ArrayList<UserPayment>());

	        user = userRepository.save(user);
	        
	         for (UserRole ur : userRoles) {
            roleRepository.save(ur.getRole());
            }

	        return user;
	    }*/
	
	
	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {
		userPayment.setUser(user);
		userPayment.setUserBilling(userBilling);
		userPayment.setDefaultPayment(true);
		userBilling.setUserPayment(userPayment);
		user.getUserPaymentList().add(userPayment);
		save(user);
	}

	@Override
	public void updateUserShipping(UserShipping userShipping, User user) {
		userShipping.setUser(user);
		userShipping.setUserShippingDefault(true);
		user.getUserShippingList().add(userShipping);
		save(user);
	}

	@Override
	public void setUserDefaultPayment(Long userPaymentId, User user) {
		List<UserPayment> userPaymentList = (List<UserPayment>) userPaymentRepository.findAll();

		for (UserPayment userPayment : userPaymentList) {
			if (userPayment.getId() == userPaymentId) {
				userPayment.setDefaultPayment(true);
				userPaymentRepository.save(userPayment);
			} else {
				userPayment.setDefaultPayment(false);
				userPaymentRepository.save(userPayment);
			}
		}

	}

	@Override
	public void setUserDefaultShipping(Long userShippingId, User user) {
		List<UserShipping> userShippingList = (List<UserShipping>) userShippingRepository.findAll();

		for (UserShipping userShipping : userShippingList) {
			if (userShipping.getId() == userShippingId) {
				userShipping.setUserShippingDefault(true);
				userShippingRepository.save(userShipping);
			} else {
				userShipping.setUserShippingDefault(false);
				userShippingRepository.save(userShipping);
			}
		}

	}

}
