package com.carcomehome.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carcomehome.domain.Car;
import com.carcomehome.repository.CarRepository;
import com.carcomehome.service.CarService;

@Service
public class CarServiceImpl implements CarService {
	@Autowired
	private CarRepository carRepository;
	
	@Override
	public Car save(Car car) {		
		return carRepository.save(car);		
	}
	
	@Override
	public void removeOne(Long id) {
		carRepository.deleteById(id);		
	}
	
	
	@Override
	public List<Car> findAll() {
	 List<Car> carList = (List<Car>) carRepository.findAll();
	 List<Car> activeCarList = new ArrayList<>(); 
	 
	 for (Car car: carList) {
			if(car.isActive()) {
				activeCarList.add(car);
			}
		}
	 return activeCarList;
	}
	
    @Override
	public Car findOne(Long id) {
    	   return carRepository.findById(id).get();
    }

	@Override
	public List<Car> findByCategory(String category) {
       
		List<Car> carList = carRepository.findByCategory(category);
		
		List<Car> activeCarList = new ArrayList<>();
		
		for (Car car: carList) {
			if(car.isActive()) {
				activeCarList.add(car);
			}
		}
		
		return activeCarList;
	}
	
    
	@Override
	public List<Car> blurrySearch(String title) {
		List<Car> carList = carRepository.findByTitleContaining(title);
		
        List<Car> activeCarList = new ArrayList<>();
		
		for (Car car: carList) {
			if(car.isActive()) {
				activeCarList.add(car);
			}
		}
		
		return activeCarList;
	}
}
