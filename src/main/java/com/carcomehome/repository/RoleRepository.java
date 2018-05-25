package com.carcomehome.repository;

import org.springframework.data.repository.CrudRepository;

import com.carcomehome.domain.security.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
           Role findByname(String name);
}
