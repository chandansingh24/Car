package com.carcomehome;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.carcomehome.domain.User;
import com.carcomehome.domain.security.Role;
import com.carcomehome.domain.security.UserRole;
import com.carcomehome.enums.PlansEnum;
import com.carcomehome.enums.RolesEnum;
import com.carcomehome.service.UserService;
import com.carcomehome.service.impl.PlanService;
import com.carcomehome.utility.SecurityUtility;

@SpringBootApplication
public class CarcomehomeApplication implements CommandLineRunner     {    

	@Autowired
	private UserService userService;
	
	@Autowired
	private PlanService planService;
	
	public static void main(String[] args) {
		SpringApplication.run(CarcomehomeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		
		planService.createPlan(PlansEnum.SERUSER.getId());
		planService.createPlan(PlansEnum.SERPROVIDER.getId());

		User user1 = new User();
		user1.setFirstName("Pranshi");
		user1.setLastName("Aadya");
		user1.setUsername("j");
		user1.setEnabled(true);
		user1.setPassword(SecurityUtility.passwordEncoder().encode("p"));
		user1.setEmail("test@outlook.com");
		
		List<UserRole> userRoles = new ArrayList<>();
		List<Role> roles = new ArrayList<>();
		List<User> users = new ArrayList<>();
		users.add(user1);
		roles.add(new Role(RolesEnum.ADMIN));
    	userRoles.add(new UserRole(users, roles));
        userService.createUser(user1, PlansEnum.SERPROVIDER, userRoles);	
		
	}	
}
