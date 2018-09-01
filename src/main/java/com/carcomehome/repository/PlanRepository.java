package com.carcomehome.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.carcomehome.domain.security.Plan;

/**
 * Created by ComeHomeCar
 */
@Repository
public interface PlanRepository extends CrudRepository<Plan, Integer> {
}
