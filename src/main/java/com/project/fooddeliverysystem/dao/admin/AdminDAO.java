package com.project.fooddeliverysystem.dao.admin;

import org.springframework.stereotype.Repository;

import com.project.fooddeliverysystem.model.admin.Admins;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AdminDAO extends JpaRepository<Admins, Integer>{
	
	Page<Admins> findByEmailContaining(String email, Pageable pageable);

	boolean existsByEmail(String email);

	Admins findByEmail(String email);

}
