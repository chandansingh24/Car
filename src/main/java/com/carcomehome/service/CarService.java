package com.carcomehome.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.query.Param;

import com.carcomehome.domain.Car;

public interface CarService {
	
    Car save(final Car car);  
   	
	List<Car> findAll(Long userId);
	
	List<Car> findAllCars();
	
//	 List<Car> findNonBookedOnes(); // Added as an POC
	 
	List<Car> findAllCarsZipCode(Date pickUpDate, Date returnDate);	
	
	List<Car> findAllCarsCityAndState(Date pickUpDate, Date returnDate);	
	
	Car findOne(Long id);
	
	List<Car> findByCategory(String category);
	
	List<Car> blurrySearch(String title);
	
	void removeOne(Long id);
}
