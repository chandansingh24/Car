package com.carcomehome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.carcomehome.domain.Order;
import com.carcomehome.domain.Reservation;

@Transactional(readOnly = true)
public interface ReservationRepository extends JpaRepository<Reservation, Long> {	
	
	Reservation findByOrder(Order order);	
	
	@Modifying
	@Transactional
	@Query("delete from Reservation r where r.id = ?1")
	void deleteReservation(Long id);
}
