package com.carcomehome.controller;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.carcomehome.domain.User;
import com.carcomehome.domain.security.PasswordResetToken;
import com.carcomehome.domain.security.Role;
import com.carcomehome.domain.security.UserRole;
import com.carcomehome.service.UserService;
import com.carcomehome.service.impl.UserSecurityService;
import com.carcomehome.utility.MailConstructor;
import com.carcomehome.utility.SecurityUtility;

@Controller
public class HomeController {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailConstructor mailConstructor;

	@Autowired
	private UserService userService;

	@Autowired
	private UserSecurityService userSecurityService;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/myAccount")
	public String myAccount() {
		return "myAccount";
	}

	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "myAccount";
	}

	@RequestMapping(value="/forgetPassword", method=RequestMethod.POST)
	public String forgetPassword(HttpServletRequest request, @RequestParam("email") String email, ModelMap model) {

		model.addAttribute("classActiveForgetPassword", true);

		User user = userService.findByEmail(email);

		if (user == null) {
			model.addAttribute("emailNotExist", true);
			return "myAccount";
		}

		String password = SecurityUtility.randomPassword();

		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);

		userService.save(user);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user,
				password);

		mailSender.send(newEmail);

		model.addAttribute("forgetPasswordEmailSent", "true");

		return "myAccount";
	}

	@RequestMapping(value = "/signUp", method = RequestMethod.GET)
	public String signUpGet(@RequestParam("planId") int planId) {

		if (planId != 1 && planId != 2) {
			throw new IllegalArgumentException("Plan id is not valid");
		}

		return "signUp";
	}

	@RequestMapping(value = "/signUp", method = RequestMethod.POST)
	public String signUpPost(@RequestParam(name = "planId", required = true) int planId, HttpServletRequest request,
			@ModelAttribute("username") String username, @ModelAttribute("password") String password,
			@ModelAttribute("fistName") String firstName, @ModelAttribute("lastName") String lastName,
			@ModelAttribute("email") String email, @ModelAttribute("phone") String phone, ModelMap model)
			throws Exception {

		if (planId != 1 && planId != 2) {
			model.addAttribute("signedUp", "false");
			model.addAttribute("message", "Plan id does not exist");
			return "signUp";
		}

		if (userService.findByUsername(username) != null) {
			model.addAttribute("duplicatedUsername", true);
			return "signUp";
		}

		if (userService.findByEmail(email) != null) {
			model.addAttribute("duplicatedEmail", true);
			return "signUp";
		}

		User user = new User();
		user.setUsername(username);

		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPhone(phone);
		user.setEnabled(true);

		Role role = new Role();
		User registeredUser = null;

		Set<UserRole> userRoles = new HashSet<>();

		if (planId == 1) {
			role.setRoleId(1);
			role.setName("ROLE_USER");
			userRoles.add(new UserRole(user, role));
			registeredUser = userService.createUser(user, userRoles);
		} else {
			role.setRoleId(2);
			role.setName("ROLE_OWNER");
			userRoles.add(new UserRole(user, role));
			registeredUser = userService.createUser(user, userRoles);

		}

		Authentication auth = new UsernamePasswordAuthenticationToken(registeredUser, null,
				registeredUser.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);

		model.addAttribute("signedUp", "true");

		return "signUp";

	}

	/*
	 * @RequestMapping(value="/newUser", method=RequestMethod.POST) public String
	 * newUserPost( HttpServletRequest request,
	 * 
	 * @ModelAttribute("email") String userEmail,
	 * 
	 * @ModelAttribute("username") String username, Model model ) throws Exception{
	 * model.addAttribute("classActiveNewAccount", true);
	 * model.addAttribute("email", userEmail); model.addAttribute("username",
	 * username);
	 * 
	 * if (userService.findByUsername(username) != null) {
	 * model.addAttribute("usernameExists", true);
	 * 
	 * return "myAccount"; }
	 * 
	 * if (userService.findByEmail(userEmail) != null) {
	 * model.addAttribute("emailExists", true);
	 * 
	 * return "myAccount"; }
	 * 
	 * User user = new User(); user.setUsername(username); user.setEmail(userEmail);
	 * 
	 * String password = SecurityUtility.randomPassword();
	 * 
	 * String encryptedPassword =
	 * SecurityUtility.passwordEncoder().encode(password);
	 * user.setPassword(encryptedPassword);
	 * 
	 * Role role = new Role(); role.setRoleId(1); role.setName("ROLE_USER");
	 * Set<UserRole> userRoles = new HashSet<>(); userRoles.add(new UserRole(user,
	 * role)); userService.createUser(user, userRoles);
	 * 
	 * String token = UUID.randomUUID().toString();
	 * userService.createPasswordResetTokenForUser(user, token);
	 * 
	 * String appUrl =
	 * "http://"+request.getServerName()+":"+request.getServerPort()+request.
	 * getContextPath();
	 * 
	 * SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl,
	 * request.getLocale(), token, user, password);
	 * 
	 * mailSender.send(email);
	 * 
	 * model.addAttribute("emailSent", "true"); // model.addAttribute("orderList",
	 * user.getOrderList());
	 * 
	 * return "myAccount";
	 * 
	 * }
	 */

	
	  @RequestMapping("/newUser")
	  public String newUser(
			  Locale locale,	  
	          @RequestParam("token") String token,
	          Model model
	          ) { 
		  PasswordResetToken passToken = userService.getPasswordResetToken(token);
	  
	  if (passToken == null) {
	  String message = "Invalid Token.";
	  model.addAttribute("message", message); 
	  return "redirect:/badRequest"; 
	  }
	  
	  User user = passToken.getUser();
	  String username = user.getUsername();
	  
	  UserDetails userDetails = userSecurityService.loadUserByUsername(username);
	  
	  Authentication authentication = new
	  UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
	  userDetails.getAuthorities());
	  SecurityContextHolder.getContext().setAuthentication(authentication);
	  
	  model.addAttribute("user", user);
	  
	  model.addAttribute("classActiveEdit", true); 
	  return "myProfile";
	  }
	 

}
