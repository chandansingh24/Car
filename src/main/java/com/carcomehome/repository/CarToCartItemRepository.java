package com.carcomehome.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.carcomehome.domain.CarToCartItem;
import com.carcomehome.domain.CartItem;

@Transactional
public interface CarToCartItemRepository extends CrudRepository<CarToCartItem, Long> {
     void deleteByCartItem(CartItem cartItem);
}
