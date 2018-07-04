package com.carcomehome.repository;

import org.springframework.data.repository.CrudRepository;

import com.carcomehome.domain.UserPayment;

public interface UserPaymentRepository extends CrudRepository<UserPayment, Long> {

}
