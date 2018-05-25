package com.carcomehome;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.carcomehome.domain.User;
import com.carcomehome.domain.security.Role;
import com.carcomehome.domain.security.UserRole;
import com.carcomehome.service.UserService;
import com.carcomehome.utility.SecurityUtility;

@SpringBootApplication
public class CarcomehomeApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;
	
	public static void main(String[] args) {
		SpringApplication.run(CarcomehomeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user1 = new User();
		user1.setFirstName("Pranshi");
		user1.setLastName("Aadya");
		user1.setUsername("j");
		user1.setEnabled(true);
		user1.setPassword(SecurityUtility.passwordEncoder().encode("p"));
		user1.setEmail("chandansingh24@outlook.com");
		Set<UserRole> userRoles = new HashSet<>();
		Role role1 = new Role();
		role1.setRoleId(1);
		role1.setName("ROLE_USER");
		userRoles.add(new UserRole(user1, role1));
		
		userService.createUser(user1, userRoles);
	}
	
	
}
