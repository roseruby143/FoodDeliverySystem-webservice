package com.project.fooddeliverysystem.dao.admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.fooddeliverysystem.model.admin.Drivers;

@Repository
public interface DriversDAO  extends JpaRepository<Drivers, Integer>{
	
	public boolean existsByEmail(String email);
	
	@Query("select d.id from Drivers d where d.status = 'active'")
	List<Integer> getAllIdsByStatus(String status);
	
	@Query("select d.id from Drivers d")
	List<Integer> getAllIds();
 
}
