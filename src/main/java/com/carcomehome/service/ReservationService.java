package com.carcomehome.service;

import java.util.Date;

import com.carcomehome.domain.Car;
import com.carcomehome.domain.Order;
import com.carcomehome.domain.Reservation;

public interface ReservationService {
	
	void completeReservation(Date pickUpDate, Date returnDate, Car car, Order order);
	
	Reservation findByOrder(Order order);
	
	Reservation save(Reservation reservation);
	
	void deleteReservation(Long id);	
}
