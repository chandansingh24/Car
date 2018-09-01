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
	 
	List<Car> findAllCarsZipCode(Date pickUpDate, Date returnDate, String inputZip);	
	
	List<Car> findAllCarsCityAndState(Date pickUpDate, Date returnDate, String inputCity, String inputState);	
	
	Car findOne(Long id);
	
	List<Car> findBySegment(String segment);
	
	List<Car> blurrySearch(String title);
	
	void removeOne(Long id);
}
