package com.carcomehome.repository;

import org.springframework.data.repository.CrudRepository;

import com.carcomehome.domain.Reservation;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

}
