package com.carcomehome.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.carcomehome.service.CarService;

@RestController
public class ResourceController {


	@Autowired
	private CarService carService;
	
	@RequestMapping(value="/car/removeList", method=RequestMethod.POST)
	public String removeList(
			@RequestBody ArrayList<String> carIdList, Model model
			){
		
		for (String id : carIdList) {
			String carId =id.substring(8);
			carService.removeOne(Long.parseLong(carId));
		}
		
		return "delete success";
	}
	
}
