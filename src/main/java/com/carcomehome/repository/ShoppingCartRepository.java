package com.carcomehome.repository;

import org.springframework.data.repository.CrudRepository;

import com.carcomehome.domain.ShoppingCart;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {

}
