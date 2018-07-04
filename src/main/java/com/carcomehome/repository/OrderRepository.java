package com.carcomehome.repository;

import org.springframework.data.repository.CrudRepository;

import com.carcomehome.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Long>{

}
