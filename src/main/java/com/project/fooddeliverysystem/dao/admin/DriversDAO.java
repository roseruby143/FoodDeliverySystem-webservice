package com.project.fooddeliverysystem.dao.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.fooddeliverysystem.model.admin.Drivers;

@Repository
public interface DriversDAO  extends JpaRepository<Drivers, Integer>{
	
	public boolean existsByEmail(String email);

}
