package com.carcomehome.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.carcomehome.domain.Car;

public interface CarRepository extends CrudRepository<Car, Long>  {
	 List<Car> findByCategory(String category); 
	 
	 List<Car> findByTitleContaining(String title); 
	 
//	 Car findOne(Long id);
}
