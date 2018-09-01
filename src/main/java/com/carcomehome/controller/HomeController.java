package com.carcomehome.controller;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.carcomehome.domain.Car;
import com.carcomehome.domain.CartItem;
import com.carcomehome.domain.Order;
import com.carcomehome.domain.User;
import com.carcomehome.domain.UserBilling;
import com.carcomehome.domain.UserPayment;
import com.carcomehome.domain.UserShipping;
import com.carcomehome.domain.security.PasswordResetToken;
import com.carcomehome.domain.security.Plan;
import com.carcomehome.domain.security.Role;
import com.carcomehome.domain.security.UserRole;
import com.carcomehome.enums.PlansEnum;
import com.carcomehome.enums.RolesEnum;
import com.carcomehome.service.CarService;
import com.carcomehome.service.CartItemService;
import com.carcomehome.service.OrderService;
import com.carcomehome.service.UserPaymentService;
import com.carcomehome.service.UserService;
import com.carcomehome.service.UserShippingService;
import com.carcomehome.service.impl.PlanService;
import com.carcomehome.service.impl.UserSecurityService;
import com.carcomehome.utility.DateUtils;
import com.carcomehome.utility.MailConstructor;
import com.carcomehome.utility.SearchDates;
import com.carcomehome.utility.SecurityUtility;
import com.carcomehome.utility.USConstants;



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
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private UserPaymentService userPaymentService;
	
	@Autowired
	private UserShippingService userShippingService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
    private PlanService planService;
	
	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);
	
			
	@RequestMapping("/")
	public String index(Model model) {
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
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
	
	@RequestMapping("/badRequest")
	public String badRequest() {
		return "badRequestPage"; 
	}
	
	@RequestMapping("/carshelf")
	public String carshelf(Model model, Principal principal) {
		
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
				
		List<Car> carList = carService.findAllCars();
		model.addAttribute("carList", carList);
		model.addAttribute("activeAll", true);
		return "carshelf";
	}
	
	
	@RequestMapping("/carDetail")
	public String bookDetail(
			@PathParam("id") Long id, Model model, Principal principal
			) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		
		Car car = carService.findOne(id);
		
		model.addAttribute("car", car);
		
		List<Integer> qtyList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		
		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);
		
		return "carDetail";
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
	
	
	@RequestMapping("/myProfile")
	public String myProfile(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		
		UserShipping userShipping = new UserShipping();
		model.addAttribute("userShipping", userShipping);
		
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("classActiveEdit", true);
		
		return "myProfile";
		
	}
	
	
	@RequestMapping("/listOfCreditCards")
	public String listOfCreditCards(
			Model model, Principal principal, HttpServletRequest request
			) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		return "myProfile";
	}
	
	
	@RequestMapping("/listOfShippingAddresses")
	public String listOfShippingAddresses(
			Model model, Principal principal, HttpServletRequest request
			) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		return "myProfile";
	}
	
	
	@RequestMapping("/addNewCreditCard")
	public String addNewCreditCard(
			Model model, Principal principal
			){
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		
		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("classActiveBilling", true);		
		model.addAttribute("listOfShippingAddresses", true);
		
		UserBilling userBilling = new UserBilling();
		UserPayment userPayment = new UserPayment();
		
		
		model.addAttribute("userBilling", userBilling);
		model.addAttribute("userPayment", userPayment);
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
	}
	
	
	@RequestMapping("/addNewShippingAddress")
	public String addNewShippingAddress(
			Model model, Principal principal
			){
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		
		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);
		
		UserShipping userShipping = new UserShipping();
		
		model.addAttribute("userShipping", userShipping);
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
	}
	
	
	@RequestMapping(value="/addNewCreditCard", method=RequestMethod.POST)
	public String addNewCreditCard(
			@ModelAttribute("userPayment") UserPayment userPayment,
			@ModelAttribute("userBilling") UserBilling userBilling,
			Principal principal, Model model
			){
		User user = userService.findByUsername(principal.getName());
		userService.updateUserBilling(userBilling, userPayment, user);
		
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
	}
	
	
	@RequestMapping("/updateCreditCard")
	public String updateCreditCard(
			@ModelAttribute("id") Long creditCardId, Principal principal, Model model
			) {
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(creditCardId);
		
		if(user.getId() != userPayment.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);
			UserBilling userBilling = userPayment.getUserBilling();
			model.addAttribute("userPayment", userPayment);
			model.addAttribute("userBilling", userBilling);
			
			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			
			model.addAttribute("addNewCreditCard", true);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("listOfShippingAddresses", true);
			
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());
			
			return "myProfile";
		}
	}
	
	
	@RequestMapping("/updateUserShipping")
	public String updateUserShipping(
			@ModelAttribute("id") Long shippingAddressId, Principal principal, Model model
			) {
		User user = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(shippingAddressId);
		
		if(user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);
			
			model.addAttribute("userShipping", userShipping);
			
			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			
			model.addAttribute("addNewShippingAddress", true);
			model.addAttribute("classActiveShipping", true);
			model.addAttribute("listOfCreditCards", true);
			
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());
			
			return "myProfile";
		}
	}
	
	
	@RequestMapping(value="/setDefaultShippingAddress", method=RequestMethod.POST)
	public String setDefaultShippingAddress(
			@ModelAttribute("defaultShippingAddressId") Long defaultShippingId, Principal principal, Model model
			) {
		User user = userService.findByUsername(principal.getName());
		userService.setUserDefaultShipping(defaultShippingId, user);
		
		model.addAttribute("user", user);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
	}
	
	
	@RequestMapping("/removeUserShipping")
	public String removeUserShipping(
			@ModelAttribute("id") Long userShippingId, Principal principal, Model model
			){
		User user = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId);
		
		if(user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);
			
			userShippingService.removeById(userShippingId);
			
			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveShipping", true);
			
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());
			
			return "myProfile";
		}
	}
	
		
	@RequestMapping(value="/setDefaultPayment", method=RequestMethod.POST)
	public String setDefaultPayment(
			@ModelAttribute("defaultUserPaymentId") Long defaultPaymentId, Principal principal, Model model
			) {
		User user = userService.findByUsername(principal.getName());
		userService.setUserDefaultPayment(defaultPaymentId, user);
		
		model.addAttribute("user", user);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
	}
	
	
	@RequestMapping("/removeCreditCard")
	public String removeCreditCard(
			@ModelAttribute("id") Long creditCardId, Principal principal, Model model
			){
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(creditCardId);
		
		if(user.getId() != userPayment.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);
			userPaymentService.removeById(creditCardId);
			
			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("listOfShippingAddresses", true);
			
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());
			
			return "myProfile";
		}
	}
	
	
	@RequestMapping(value="/addNewShippingAddress", method=RequestMethod.POST)
	public String addNewShippingAddressPost(
			@ModelAttribute("userShipping") UserShipping userShipping,
			Principal principal, Model model
			){
		User user = userService.findByUsername(principal.getName());
		userService.updateUserShipping(userShipping, user);
		
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
	}
	
	
	@RequestMapping(value = "/signUp", method = RequestMethod.GET)
	public String signUpGet(@RequestParam("planId") int planId) {		
		
		if (planId != PlansEnum.SERUSER.getId() && planId != PlansEnum.SERPROVIDER.getId()) {
            throw new IllegalArgumentException("Plan id is not valid");
        }				
		return "signUp";
	}
	

	@RequestMapping(value = "/signUp", method = RequestMethod.POST)
	public String signUpPost(@RequestParam(name = "planId", required = true) int planId, HttpServletRequest request,
			@ModelAttribute("username") String username, @ModelAttribute("password") String password,
			@ModelAttribute("firstName") String firstName, @ModelAttribute("lastName") String lastName,
			@ModelAttribute("email") String email, @ModelAttribute("phone") String phone, ModelMap model)
			throws Exception {

		if (planId != PlansEnum.SERUSER.getId() && planId != PlansEnum.SERPROVIDER.getId()) {
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
		
		
		 // Sets the Plan and the Roles (depending on the chosen plan)
        LOG.debug("Retrieving plan from the database");
        Plan selectedPlan = planService.findPlanById(planId);
        if (null == selectedPlan) {
            LOG.error("The plan id {} could not be found. Throwing exception.", planId);
            model.addAttribute("signedUp", false);
            model.addAttribute("message", "Plan id not found");
            return "signUp";
        }
       

		User user = new User();
		user.setUsername(username);
		user.setPlan(selectedPlan);

		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPhone(phone);
		user.setEnabled(true);

	//	Role role = new Role();
		User registeredUser = null;

	//	Set<UserRole> userRoles = new HashSet<>();
		
	// Assigning roles
           List<UserRole> userRoles = new ArrayList<>();
           List<Role> roles = new ArrayList<>(); 
           List<User> users = new ArrayList<>();
           users.add(user);
        
        if (planId == PlansEnum.SERUSER.getId()) {
        	roles.add(new Role(RolesEnum.BASICUSER));
        	userRoles.add(new UserRole(users,  roles));
            registeredUser = userService.createUser(user, PlansEnum.SERUSER, userRoles);
        } else {
        	roles.add(new Role(RolesEnum.BASICUSER));
        	roles.add(new Role(RolesEnum.OWNER));
        	userRoles.add(new UserRole(users,  roles));
            registeredUser = userService.createUser(user, PlansEnum.SERPROVIDER, userRoles);            
        }
		
	
	 // Auto logins the registered user
		Authentication auth = new UsernamePasswordAuthenticationToken(registeredUser, null,
				registeredUser.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		
	 LOG.info("User created successfully");
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
	  
	  
	  @RequestMapping(value="/updateUserInfo", method=RequestMethod.POST)
		public String updateUserInfo(
				@ModelAttribute("user") User user,
				@ModelAttribute("newPassword") String newPassword,
				Model model
				) throws Exception {
			User currentUser = userService.findById(user.getId());
			
			if(currentUser == null) {
				throw new Exception ("User not found");
			}
			
			/*check email already exists*/
			if (userService.findByEmail(user.getEmail())!=null) {
				if(userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
					model.addAttribute("emailExists", true);
					return "myProfile";
				}
			}
			
			/*check username already exists*/
			if (userService.findByUsername(user.getUsername())!=null) {
				if(userService.findByUsername(user.getUsername()).getId() != currentUser.getId()) {
					model.addAttribute("usernameExists", true);
					return "myProfile";
				}
			}
			
//			update password
			if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")){
				BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
				String dbPassword = currentUser.getPassword();
				if(passwordEncoder.matches(user.getPassword(), dbPassword)){
					currentUser.setPassword(passwordEncoder.encode(newPassword));
				} else {
					
				//Newly Added was not there///
					model.addAttribute("classActiveEdit", true);
					model.addAttribute("incorrectPassword", true);
					
					return "myProfile";
				}
			}
			
			currentUser.setFirstName(user.getFirstName());
			currentUser.setLastName(user.getLastName());
			currentUser.setUsername(user.getUsername());
			currentUser.setEmail(user.getEmail());
			
			userService.save(currentUser);
			
			model.addAttribute("updateSuccess", true);
			model.addAttribute("user", currentUser);
			model.addAttribute("classActiveEdit", true);
			
			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("listOfCreditCards", true);
			
			UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());

			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
					userDetails.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			model.addAttribute("orderList", user.getOrderList());
			
			return "myProfile";
		}
	  
	  
	  @RequestMapping("/orderDetail")
		public String orderDetail(
				@RequestParam("id") Long orderId,
				Principal principal, Model model
				){
			User user = userService.findByUsername(principal.getName());
			Order order = orderService.findOne(orderId);
			
			if(order.getUser().getId()!=user.getId()) {
				return "badRequestPage";
			} else {
				List<CartItem> cartItemList = cartItemService.findByOrder(order);
				model.addAttribute("cartItemList", cartItemList);
				model.addAttribute("user", user);
				model.addAttribute("order", order);
				
				model.addAttribute("userPaymentList", user.getUserPaymentList());
				model.addAttribute("userShippingList", user.getUserShippingList());
				model.addAttribute("orderList", user.getOrderList());
				
				UserShipping userShipping = new UserShipping();
				model.addAttribute("userShipping", userShipping);
				
				List<String> stateList = USConstants.listOfUSStatesCode;
				Collections.sort(stateList);
				model.addAttribute("stateList", stateList);
				
				model.addAttribute("listOfShippingAddresses", true);
				model.addAttribute("classActiveOrders", true);
				model.addAttribute("listOfCreditCards", true);
				model.addAttribute("displayOrderDetail", true);
				
				return "myProfile";
			}
		}
		
	 
	/* Pick Up Date and Return Date implementation */
	  
	  @RequestMapping("/carshelfDateImplementation")
		public String carshelfDateImplementation(
				@RequestParam(name="startDate", required=false) String startDate,
				@RequestParam(name="returnDate", required=false) String returnDate,
				@RequestParam(name="inputZip", required=false) String inputZip,
				@RequestParam(name="inputCity", required=false) String inputCity,
				@RequestParam(name="inputState", required=false) String inputState,
				Model model, Principal principal) throws ParseException {
		 
		  Date pickUpDate = null;
		  Date returnBackDate = null;
		  
		  
	     if ((startDate != "") && (returnDate != "")) {
	    	 
	    	 pickUpDate = DateUtils.parseDate(startDate);
	    	 SearchDates.setPickUpDate(pickUpDate);  	 
	    	 
	    	 returnBackDate = DateUtils.parseDate(returnDate);
	    	 SearchDates.setReturnBackDate(returnBackDate);
		      
		        
	     } else {
	    	 
	    	 model.addAttribute("invalidDateType", "Please check your input dates" );
	//    	 return "carShelf";
	     }          
	     
		  if (inputZip !="") {
			  List<Car> carList = carService.findAllCarsZipCode(pickUpDate, returnBackDate, inputZip);	
			  model.addAttribute("carList", carList);			  
		  } else if (inputCity !="") {
			  List<Car> carList = carService.findAllCarsCityAndState(pickUpDate, returnBackDate, inputCity, inputState);
			  model.addAttribute("carList", carList);
		  } else {
			  model.addAttribute("invalidZipAndCityState", "Please refine your search with either Zipcode or City");
		  }		  	
		  
		  if(principal != null) {
				String username = principal.getName();
				User user = userService.findByUsername(username);
				model.addAttribute("user", user);
			}
		  
			model.addAttribute("pickUpDate", pickUpDate);
			model.addAttribute("returnBackDate", returnBackDate);
			model.addAttribute("activeAll", true);
			return "carshelf";
		}	
   
}
