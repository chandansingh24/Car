package com.carcomehome.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.carcomehome.domain.Car;
import com.carcomehome.domain.CartItem;
import com.carcomehome.domain.ShoppingCart;
import com.carcomehome.domain.User;
import com.carcomehome.service.CarService;
import com.carcomehome.service.CartItemService;
import com.carcomehome.service.ShoppingCartService;
import com.carcomehome.service.UserService;
import com.carcomehome.utility.SearchDates;




@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	/*@Autowired
	private SearchDates searchDates;*/
	
	@RequestMapping("/cart")
	public String shoppingCart(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		ShoppingCart shoppingCart = user.getShoppingCart();
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		shoppingCartService.updateShoppingCart(shoppingCart);
		
		model.addAttribute("pickUpDate", SearchDates.getPickUpDate());
		model.addAttribute("returnBackDate", SearchDates.getReturnBackDate());
		
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);
		
		return "shoppingCart";
	}

	@RequestMapping("/addItem")
	public String addItem(
			@ModelAttribute("car") Car car,
			@ModelAttribute("qty") String qty,
			Model model, Principal principal
			) {
		User user = userService.findByUsername(principal.getName());
		car = carService.findOne(car.getId());
		
		/*if (Integer.parseInt(qty) > car.getInStockNumber()) {
			model.addAttribute("notEnoughStock", true);
		return "forward:/carDetail?id="+car.getId();
	}*/
	
	CartItem cartItem = cartItemService.addcarToCartItem(car, user, Integer.parseInt(qty));
	model.addAttribute("addcarSuccess", true);
	
	return "forward:/carDetail?id="+car.getId();
	}

		
	@RequestMapping("/updateCartItem")
	public String updateShoppingCart(
			@ModelAttribute("id") Long cartItemId,
			@ModelAttribute("qty") int qty
			) {
		CartItem cartItem = cartItemService.findById(cartItemId);
		cartItem.setQty(qty);
		cartItemService.updateCartItem(cartItem);
		
		return "forward:/shoppingCart/cart";
	}
	
	@RequestMapping("/removeItem")
	public String removeItem(@RequestParam("id") Long id) {
		cartItemService.removeCartItem(cartItemService.findById(id));
		
		return "redirect:/shoppingCart/cart";
	}


}
