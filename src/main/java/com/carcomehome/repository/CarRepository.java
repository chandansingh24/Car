package com.carcomehome.repository;

import java.util.List;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.carcomehome.domain.Car;


public interface CarRepository extends CrudRepository<Car, Long>  {
	 List<Car> findByCategory(String category); 
	 
	 List<Car> findByTitleContaining(String title); 	 
	 
	 @Query("select cr from Car cr inner join cr.user u where cr.user.id = ?1")
	 List<Car> findAllByUserId(long userId);	

}
