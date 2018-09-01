package com.carcomehome.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.carcomehome.domain.BillingAddress;
import com.carcomehome.domain.Car;
import com.carcomehome.domain.CartItem;
import com.carcomehome.domain.Order;
import com.carcomehome.domain.Payment;
import com.carcomehome.domain.Reservation;
import com.carcomehome.domain.ShippingAddress;
import com.carcomehome.domain.ShoppingCart;
import com.carcomehome.domain.User;
import com.carcomehome.domain.UserBilling;
import com.carcomehome.domain.UserPayment;
import com.carcomehome.domain.UserShipping;
import com.carcomehome.domain.security.PasswordResetToken;
import com.carcomehome.service.BillingAddressService;
import com.carcomehome.service.CarService;
import com.carcomehome.service.CartItemService;
import com.carcomehome.service.OrderService;
import com.carcomehome.service.PaymentService;
import com.carcomehome.service.ReservationService;
import com.carcomehome.service.ShippingAddressService;
import com.carcomehome.service.ShoppingCartService;
import com.carcomehome.service.UserPaymentService;
import com.carcomehome.service.UserService;
import com.carcomehome.service.UserShippingService;
import com.carcomehome.utility.MailConstructor;
import com.carcomehome.utility.OrderConfirmUtils;
import com.carcomehome.utility.SearchDates;
import com.carcomehome.utility.USConstants;

@Controller
public class CheckoutController {

	private ShippingAddress shippingAddress = new ShippingAddress();
	private BillingAddress billingAddress = new BillingAddress();
	private Payment payment = new Payment();

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailConstructor mailConstructor;

	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private ShippingAddressService shippingAddressService;

	@Autowired
	private BillingAddressService billingAddressService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private UserShippingService userShippingService;

	@Autowired
	private UserPaymentService userPaymentService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CarService carService;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private OrderConfirmUtils orderConfirmUtils;

	@RequestMapping("/checkout")
	public String checkout(@RequestParam("id") Long cartId, @RequestParam(value="barterActive", required = false) boolean barterActive,
			@RequestParam(value = "missingRequiredField", required = false) boolean missingRequiredField, Model model,
			Principal principal) {
		User user = userService.findByUsername(principal.getName());

		CartItem cartItem = cartItemService.findById(cartId);
		Car car = carService.findOne(cartItem.getCar().getId());

		if (cartId != cartItem.getId()) {
			return "badRequestPage";
		}

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

		if (cartItemList.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "forward:/shoppintCart/cart";
		}

		/*
		 * for (CartItem cartItemOne : cartItemList) { if
		 * (cartItem.getCar().getInStockNumber() < cartItem.getQty()) {
		 * model.addAttribute("notEnoughStock", true); return
		 * "forward:/shoppingCart/cart"; }
		 * 
		 * }
		 */

		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();

		model.addAttribute("userShippingList", userShippingList);
		model.addAttribute("userPaymentList", userPaymentList);

		if (userPaymentList.size() == 0) {
			model.addAttribute("emptyPaymentList", true);
		} else {
			model.addAttribute("emptyPaymentList", false);
		}

		if (userShippingList.size() == 0) {
			model.addAttribute("emptyShippingList", true);
		} else {
			model.addAttribute("emptyShippingList", false);
		}

		// ShoppingCart shoppingCart = user.getShoppingCart();

		for (UserShipping userShipping : userShippingList) {
			if (userShipping.isUserShippingDefault()) {
				shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			}
		}

		for (UserPayment userPayment : userPaymentList) {
			if (userPayment.isDefaultPayment()) {
				paymentService.setByUserPayment(userPayment, payment);
				billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
			}
		}

		model.addAttribute("barterActive", barterActive);
		model.addAttribute("car", car);
		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("payment", payment);
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("cartItem", cartItem);
		model.addAttribute("shoppingCart", user.getShoppingCart());

		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);

		model.addAttribute("classActiveShipping", true);

		if (missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}

		return "checkout";

	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkoutPost(@ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
			@ModelAttribute("billingAddress") BillingAddress billingAddress, @ModelAttribute("payment") Payment payment,
			@ModelAttribute("billingSameAsShipping") String billingSameAsShipping,
			@ModelAttribute("shippingMethod") String shippingMethod, @RequestParam("cartId") Long cartId,
			@RequestParam(value="barterActive", required=false) boolean barterActive, Principal principal, Model model,
			HttpServletRequest request) {

		ShoppingCart shoppingCart = userService.findByUsername(principal.getName()).getShoppingCart();

		/*
		 * List<CartItem> cartItemList =
		 * cartItemService.findByShoppingCart(shoppingCart);
		 * model.addAttribute("cartItemList", cartItemList);
		 */

		CartItem cartItem = cartItemService.findById(cartId);
		model.addAttribute("cartItem", cartItem);

		if (billingSameAsShipping.equals("true")) {
			billingAddress.setBillingAddressName(shippingAddress.getShippingAddressName());
			billingAddress.setBillingAddressStreet1(shippingAddress.getShippingAddressStreet1());
			billingAddress.setBillingAddressStreet2(shippingAddress.getShippingAddressStreet2());
			billingAddress.setBillingAddressCity(shippingAddress.getShippingAddressCity());
			billingAddress.setBillingAddressState(shippingAddress.getShippingAddressState());
			billingAddress.setBillingAddressCountry(shippingAddress.getShippingAddressCountry());
			billingAddress.setBillingAddressZipcode(shippingAddress.getShippingAddressZipcode());
		}

		if (shippingAddress.getShippingAddressStreet1().isEmpty() || shippingAddress.getShippingAddressCity().isEmpty()
				|| shippingAddress.getShippingAddressState().isEmpty()
				|| shippingAddress.getShippingAddressName().isEmpty()
				|| shippingAddress.getShippingAddressZipcode().isEmpty() || payment.getCardNumber().isEmpty()
				|| payment.getCvc() == 0 || billingAddress.getBillingAddressStreet1().isEmpty()
				|| billingAddress.getBillingAddressCity().isEmpty() || billingAddress.getBillingAddressState().isEmpty()
				|| billingAddress.getBillingAddressName().isEmpty()
				|| billingAddress.getBillingAddressZipcode().isEmpty()) {

			if (barterActive) {

				return "redirect:/checkout?id=" + cartItem.getId() + "&missingRequiredField=true" + "&barterActive="
						+ barterActive;

			} else {

				return "redirect:/checkout?id=" + cartItem.getId() + "&missingRequiredField=true";

			}

		}

		User user = userService.findByUsername(principal.getName());

		Order order = orderService.createOrder(shoppingCart, shippingAddress, billingAddress, payment, shippingMethod,
				user);

		// Complete the reservation

		reservationService.completeReservation(SearchDates.getPickUpDate(), SearchDates.getReturnBackDate(),
				cartItem.getCar(), cartItem.getOrder());

		// Car Owner details..
		User carOwner = cartItem.getCar().getUser();

		String token = UUID.randomUUID().toString();

		userService.createPasswordResetTokenForUser(user, token);

		String appUrlCancel = "http://" + request.getServerName() + ":" + request.getServerPort()
				+ "/reservationCancel?token=" + token + "&orderId=" + order.getId();

		if (barterActive) {
			String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
					+ "/reservationConfirmationBarter?token=" + token + "&orderId=" + order.getId();
			mailSender.send(mailConstructor.constructOrderConfirmationEmail(order, user, carOwner, appUrl, appUrlCancel,
					SearchDates.getPickUpDate(), SearchDates.getReturnBackDate(),
					"orderConfirmationEmailTemplateBarter", Locale.ENGLISH));
		} else {
			String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
					+ "/reservationConfirmation?token=" + token + "&orderId=" + order.getId();
			mailSender.send(mailConstructor.constructOrderConfirmationEmail(order, user, carOwner, appUrl, appUrlCancel,
					SearchDates.getPickUpDate(), SearchDates.getReturnBackDate(), "orderConfirmationEmailTemplate",
					Locale.ENGLISH));
		}

		mailSender.send(
				orderConfirmUtils.sendCustomerConfirmationEmail(order, user, carOwner, SearchDates.getPickUpDate(),
						SearchDates.getReturnBackDate(), "sendCustomerConfirmationEmailTemplate", Locale.ENGLISH));

		shoppingCartService.clearShoppingCart(shoppingCart);

		return "orderSubmittedPage";
	}

	@RequestMapping(value = "/reservationConfirmation", method = RequestMethod.POST)
	public String reservationConfirmation(@RequestParam("token") String token, @RequestParam("orderId") Long orderId,
			Model model, HttpServletRequest request) {

		PasswordResetToken passToken = userService.getPasswordResetToken(token);

		if (passToken == null) {
			String message = "Invalid Token.";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}

		if (reservationService.findByOrder(orderService.findOne(orderId)) != null) {

			Reservation reservation = reservationService.findByOrder(orderService.findOne(orderId));
			reservation.setStatus("confirmed");
			reservationService.save(reservation);

			LocalDateTime ldt = LocalDateTime.ofInstant(SearchDates.getPickUpDate().toInstant(),
					ZoneId.systemDefault());
			String pickupDate = ldt.getMonthValue() + "/" + ldt.getDayOfMonth();

			LocalDateTime ldtRT = LocalDateTime.ofInstant(SearchDates.getReturnBackDate().toInstant(),
					ZoneId.systemDefault());
			String returnBackDate = ldtRT.getMonthValue() + "/" + ldtRT.getDayOfMonth();

			Car car = reservation.getCar();
			String busniessAddress = car.getBusinessAddressStreet1() + ", " + car.getBusinessAddressStreet2() + ", "
					+ car.getBusinessAddressCity() + " " + car.getBusinessAddressZipcode();

			String bodyMessage = "Thanks for choosing ComeHomeCar, your request is acknowledged and accepted, for the dates "
					+ pickupDate + " to " + returnBackDate + ". " + "Following is the address and contact details of "
					+ car.getUser().getFirstName()+ " " + busniessAddress + ". " + "Contact phone no. " 
					+ car.getUser().getPhone()+", " + "and Cc'ed in email";

			User user = orderService.findOne(orderId).getUser();
			
			User carOwner = car.getUser();

			SimpleMailMessage newEmail = orderConfirmUtils.carOwnerConfirmationEmail(user, carOwner,
					"ComeHomeCar Rent Request Confirmed", bodyMessage, request.getLocale());

			mailSender.send(newEmail);

		} else {
			return "redirect:/badRequest";
		}

		userService.deleteAllExpiredSince(orderConfirmUtils.convertDateTimeToDate());

		return "mailConfirmSuccess";
	}

	@RequestMapping(value = "/reservationConfirmationBarter", method = RequestMethod.POST)
	public String reservationConfirmationBarter(@RequestParam("token") String token,
			@RequestParam("orderId") Long orderId, Model model, HttpServletRequest request) {

		PasswordResetToken passToken = userService.getPasswordResetToken(token);

		if (passToken == null) {
			String message = "Invalid Token.";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}

		if (reservationService.findByOrder(orderService.findOne(orderId)) != null) {

			Reservation reservation = reservationService.findByOrder(orderService.findOne(orderId));
			reservation.setBarterStatus("confirmed");
			reservationService.save(reservation);

			LocalDateTime ldt = LocalDateTime.ofInstant(SearchDates.getPickUpDate().toInstant(),
					ZoneId.systemDefault());
			String pickupDate = ldt.getMonthValue() + "/" + ldt.getDayOfMonth();

			LocalDateTime ldtRT = LocalDateTime.ofInstant(SearchDates.getReturnBackDate().toInstant(),
					ZoneId.systemDefault());
			String returnBackDate = ldtRT.getMonthValue() + "/" + ldtRT.getDayOfMonth();

			Car car = reservation.getCar();
			String busniessAddress = car.getBusinessAddressStreet1() + ", " + car.getBusinessAddressStreet2() + ", "
					+ car.getBusinessAddressCity() + " " + car.getBusinessAddressZipcode();

			String bodyMessage = "Thanks for choosing ComeHomeCar, your barter request is acknowledged and accepted, for the dates "
					+ pickupDate + " to " + returnBackDate + ". " + "Following is the address and contact details of "
					+ car.getUser().getFirstName()+ " " + busniessAddress + ". " + "Contact phone no. "
					+ car.getUser().getPhone()+", " + "and Cc'ed in email";

			User user = orderService.findOne(orderId).getUser();
			
			User carOwner = car.getUser();

			SimpleMailMessage newEmail = orderConfirmUtils.carOwnerConfirmationEmail(user, carOwner,
					"ComeHomeCar Barter Request Confirmed", bodyMessage, request.getLocale());

			mailSender.send(newEmail);

		} else {
			return "redirect:/badRequest";
		}

		userService.deleteAllExpiredSince(orderConfirmUtils.convertDateTimeToDate());

		return "mailConfirmSuccess";
	}

	@RequestMapping(value = "/reservationCancel", method = RequestMethod.POST)
	public String reservationCancel(@RequestParam("token") String token, @RequestParam("orderId") Long orderId,
			Model model, HttpServletRequest request) {

		PasswordResetToken passToken = userService.getPasswordResetToken(token);

		if (passToken == null) {
			String message = "Invalid Token.";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}

		if (reservationService.findByOrder(orderService.findOne(orderId)) != null) {

			Reservation reservation = reservationService.findByOrder(orderService.findOne(orderId));
			Car car = reservation.getCar();
			User carOwner = car.getUser();
			reservationService.deleteReservation(reservation.getId());

			String bodyMessage = "Sorry your request isn't accepted by the owner, we hope you find a good match in next search. Thanks for using ComeHomeCar!";

			User user = orderService.findOne(orderId).getUser();			

			SimpleMailMessage newEmail = orderConfirmUtils.carOwnerConfirmationEmail(user, carOwner,
					"Rent Request Cancelled ComeHomeCar", bodyMessage, request.getLocale());

			mailSender.send(newEmail);

		} else {
			return "redirect:/badRequest";
		}

		userService.deleteAllExpiredSince(orderConfirmUtils.convertDateTimeToDate());

		return "mailDeleteSuccess";
	}

	@RequestMapping("/setShippingAddress")
	public String setShippingAddress(@RequestParam("userShippingId") Long userShippingId,
			@RequestParam("cartId") Long cartId, Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId);

		if (userShipping.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} else {
			shippingAddressService.setByUserShipping(userShipping, shippingAddress);

			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

			CartItem cartItem = cartItemService.findById(cartId);
			model.addAttribute("cartItem", cartItem);

			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());

			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList = user.getUserPaymentList();

			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);

			model.addAttribute("shippingAddress", shippingAddress);

			model.addAttribute("classActiveShipping", true);

			if (userPaymentList.size() == 0) {
				model.addAttribute("emptyPaymentList", true);
			} else {
				model.addAttribute("emptyPaymentList", false);
			}

			model.addAttribute("emptyShippingList", false);

			return "checkout";
		}
	}

	@RequestMapping("/setPaymentMethod")
	public String setPaymentMethod(@RequestParam("userPaymentId") Long userPaymentId,
			@RequestParam("cartId") Long cartId, Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(userPaymentId);
		UserBilling userBilling = userPayment.getUserBilling();

		if (userPayment.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} else {
			paymentService.setByUserPayment(userPayment, payment);

			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

			billingAddressService.setByUserBilling(userBilling, billingAddress);

			CartItem cartItem = cartItemService.findById(cartId);
			model.addAttribute("cartItem", cartItem);

			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());

			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList = user.getUserPaymentList();

			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);

			model.addAttribute("shippingAddress", shippingAddress);

			model.addAttribute("classActivePayment", true);

			model.addAttribute("emptyPaymentList", false);

			if (userShippingList.size() == 0) {
				model.addAttribute("emptyShippingList", true);
			} else {
				model.addAttribute("emptyShippingList", false);
			}

			return "checkout";
		}
	}

}
