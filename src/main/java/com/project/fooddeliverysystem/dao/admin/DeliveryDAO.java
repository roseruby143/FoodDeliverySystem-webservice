package com.project.fooddeliverysystem.dao.admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fooddeliverysystem.model.admin.Delivery;

import jakarta.transaction.Transactional;

public interface DeliveryDAO extends JpaRepository<Delivery, Integer>{
	
	List<Delivery> findByDriverId(int id);
}
