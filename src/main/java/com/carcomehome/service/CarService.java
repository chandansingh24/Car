package com.carcomehome.service;

import java.util.List;
import java.util.Optional;

import com.carcomehome.domain.Car;

public interface CarService {
	
    Car save(Car car);  
   	
	List<Car> findAll();
	
	Car findOne(Long id);
	
	List<Car> findByCategory(String category);
	
	List<Car> blurrySearch(String title);
	
	void removeOne(Long id);
}
