package com.sda.auction.repository;

import com.sda.auction.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    // Optional container for Role, method findByName finds the role by name
    Optional<Role> findByName(String name);

}
