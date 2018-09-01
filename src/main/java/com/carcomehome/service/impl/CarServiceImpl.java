package com.carcomehome.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	public Car save(final Car car) {		
		return carRepository.save(car);		
	}
	
	@Override
	public void removeOne(Long id) {
		carRepository.deleteById(id);		
	}	
	
	
	@Override
	public List<Car> findAllCars() {
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
	public List<Car> findAll(Long userId) {
		
	List<Car> carList = carRepository.findAllByUserId(userId);	
	
	 /*List<Car> activeCarList = new ArrayList<>(); 
	 
	 for (Car car: carList) {
			if(car.isActive()) {
				activeCarList.add(car);
			}
		}
	 return activeCarList;*/
	 return carList;	 
	}	
	
	
    @Override
	public Car findOne(Long id) {
    	   return carRepository.findById(id).get();
    }

	@Override
	public List<Car> findBySegment(String segment) {
       
		List<Car> carList = carRepository.findBySegment(segment);
		
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

	@Override
	public List<Car> findAllCarsZipCode(Date pickUpDate, Date returnDate, String inputZip) {
		
			 /*pickUpDate = "2018-02-01";
			 returnDate = "2018-02-14";*/
		List<Car> carList = carRepository.findNonBookedCarsZipCode(pickUpDate, returnDate, inputZip);  
		 
		 List<Car> activeCarList = new ArrayList<>(); 
		 
		 for (Car car: carList) {
				if(car.isActive()) {
					activeCarList.add(car);
				}
			}
		 return activeCarList;
	}

	@Override
	public List<Car> findAllCarsCityAndState(Date pickUpDate, Date returnDate, String inputCity, String inputState) {
		
		 List<Car> carList = carRepository.findNonBookedCarsCityAndState(pickUpDate, returnDate, inputCity, inputState);
		 
		 List<Car> activeCarList = new ArrayList<>(); 
		 
		 for (Car car: carList) {
				if(car.isActive()) {
					activeCarList.add(car);
				}
			}
		 return activeCarList;
	}
	
  	
	
}
