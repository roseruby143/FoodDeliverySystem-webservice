package com.project.fooddeliverysystem.dao.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.fooddeliverysystem.model.user.Users;

@Repository
public interface UserDAO extends JpaRepository<Users, Integer> {
	
	Page<Users> findByEmailContaining(String email, Pageable pageable);

	boolean existsByEmail(String email);

	Users findByEmail(String email);

}
