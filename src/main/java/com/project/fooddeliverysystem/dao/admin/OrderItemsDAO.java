package com.project.fooddeliverysystem.dao.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.fooddeliverysystem.model.admin.OrderItems;

import jakarta.transaction.Transactional;

import java.util.List;


@Repository
public interface OrderItemsDAO  extends JpaRepository<OrderItems, Integer>{

	List<OrderItems> findByOrderId(int orderId);
	
	@Transactional
	void deleteByOrderId(int orderId);

	
	@Query("SELECT oi FROM OrderItems oi " +
	           "JOIN oi.order o " +
	           "JOIN o.user u " +
	           "WHERE u.id = :userId")
	List<OrderItems> findByUserId(int userId);
}
