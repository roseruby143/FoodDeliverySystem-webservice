package com.project.fooddeliverysystem.dao.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.fooddeliverysystem.model.admin.Delivery;
import com.project.fooddeliverysystem.model.admin.Orders;

import java.util.List;

import jakarta.transaction.Transactional;



@Repository
public interface OrdersDAO extends JpaRepository<Orders, Integer> {
	
	List<Orders> findByUserId(int userId);
	
	List<Delivery> findByDeliveryId(int deliveryId);
	
	@Transactional
	void deleteByUserId(int userId);
	
	@Transactional
	void deleteByDeliveryId(int deliveryId);
}
