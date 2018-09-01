package com.carcomehome.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carcomehome.domain.Car;
import com.carcomehome.domain.Order;
import com.carcomehome.domain.Reservation;
import com.carcomehome.repository.ReservationRepository;
import com.carcomehome.service.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService {
    
	@Autowired
	private ReservationRepository reservationRepository;	
	
	@Override
	public synchronized void completeReservation(Date pickUpDate, Date returnDate, Car car, Order order) {
		Reservation reservation = new Reservation();
		reservation.setPickUpDate(pickUpDate);
		reservation.setReturnDate(returnDate);
		reservation.setCar(car);
		reservation.setOrder(order);
		reservationRepository.save(reservation);			
	}
	

	@Override
	public Reservation findByOrder(Order order) {
		
	  return reservationRepository.findByOrder(order);		
	}
	

	@Override
	public Reservation save(Reservation reservation) {		
		return reservationRepository.save(reservation);
	}


	@Override
	public void deleteReservation(Long id) {
		reservationRepository.deleteReservation(id);
		
	}


	

	
}
