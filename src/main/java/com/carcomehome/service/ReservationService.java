package com.carcomehome.service;

import java.util.Date;

import com.carcomehome.domain.Car;

public interface ReservationService {
	
	void completeReservation(Date pickUpDate, Date returnDate, Car car);

}
