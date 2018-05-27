package com.carcomehome.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.carcomehome.domain.Car;
import com.carcomehome.domain.User;
import com.carcomehome.service.CarService;
import com.carcomehome.service.UserService;
import com.carcomehome.service.impl.S3Service;




@Controller
@RequestMapping("/car")
public class CarController {

	 /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(CarController.class);	
	
	
    @Autowired
    private UserService userService;
    
    
    @Autowired
	private CarService carService;
	
	@Autowired
    private S3Service s3Service;
	
	
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String addCar(Model model) {
		Car car = new Car();
		model.addAttribute("car", car);
		
		return "addCar";				
	}
	
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public String addCarPost(
			@ModelAttribute("car") Car car,
			@RequestParam(name="file", required=false) MultipartFile file,
			Principal principal
			) throws IOException {		
		
// Stores the profile image on Amazon S3 and stores the URL in the user's record
        if (file != null && !file.isEmpty()) {
            
            String profileImageUrl = s3Service.storeProfileImage(file, principal.getName());  
            if (profileImageUrl != null) {
                car.setProfileImageUrl(profileImageUrl);
            } else {
                LOG.warn("There was a problem uploading the profile image to S3. The user's profile will" +
                        " be created without the image");
            }

        }		
        
        User user = userService.findByUsername(principal.getName());
        car.setUser(user);        
        carService.save(car);				
	
	  return "redirect:/car/carList";	
		
		/*MultipartFile carImage = car.getCarImage();

		try {
			byte[] bytes = carImage.getBytes();
			String name = car.getId() + ".jpg";
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File("src/main/resources/static/image/car/" + name)));
			stream.write(bytes);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:carList";	*/				
	}

	
	
//	@RequestMapping("/carInfo")
//	public String carInfo(@RequestParam("id") Long id, Model model) {
//		Car car = carService.findOne(id);
//		model.addAttribute("car", car);
//		
//		return "carInfo";
//	}
//	
//	@RequestMapping("/updateCar")
//	public String updateCar(@RequestParam("id") Long id, Model model) {
//		Car car = carService.findOne(id);
//		model.addAttribute("car", car);
//		
//		return "updateCar";
//	}
//	
//	
//	@RequestMapping(value="/updateCar", method=RequestMethod.POST)
//	public String updatecarPost(@ModelAttribute("car") Car car, HttpServletRequest request) {
//		carService.save(car);
//		
//		MultipartFile carImage = car.getCarImage();
//		
//		if(!carImage.isEmpty()) {
//			try {
//				byte[] bytes = carImage.getBytes();
//				String name = car.getId() + ".jpg";
//				
//				Files.delete(Paths.get("src/main/resources/static/image/car/"+name));
//				
//				BufferedOutputStream stream = new BufferedOutputStream(
//						new FileOutputStream(new File("src/main/resources/static/image/car/" + name)));
//				stream.write(bytes);
//				stream.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		return "redirect:/car/carInfo?id="+car.getId();
//	}	
	
		
	@RequestMapping("/carList")
	public String carList(Model model, Principal principal) {
		
	User user = userService.findByUsername(principal.getName());		
	List<Car> carList = carService.findAll(user.getId());
	model.addAttribute("carList", carList);
		
		return "carList";
	}
	
	
//	@RequestMapping(value="/remove", method=RequestMethod.POST)
//	public String remove(
//			@ModelAttribute("id") String id, Model model
//			) {
//		carService.removeOne(Long.parseLong(id.substring(7)));
//		List<Car> carList = carService.findAll();
//		model.addAttribute("carList", carList);
//		
//		return "redirect:/car/carList";
//	}

}
