package com.carcomehome.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.carcomehome.domain.security.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
           Role findByname(String name);
}
