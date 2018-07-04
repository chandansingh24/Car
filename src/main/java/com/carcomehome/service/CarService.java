package com.carcomehome.service;

import java.util.List;
import java.util.Optional;

import com.carcomehome.domain.Car;

public interface CarService {
	
    Car save(final Car car);  
   	
	List<Car> findAll(Long userId);
	
	List<Car> findAllCars();
	
//	 List<Car> findNonBookedOnes(); // Added as an POC
	
	Car findOne(Long id);
	
	List<Car> findByCategory(String category);
	
	List<Car> blurrySearch(String title);
	
	void removeOne(Long id);
}
