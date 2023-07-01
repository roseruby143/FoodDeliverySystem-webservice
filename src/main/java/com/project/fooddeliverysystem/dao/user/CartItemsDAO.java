package com.project.fooddeliverysystem.dao.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.fooddeliverysystem.model.user.CartItems;

import jakarta.transaction.Transactional;

@Repository
public interface CartItemsDAO extends JpaRepository<CartItems, Integer>{
	
	List<CartItems> findByUserId(int userId);
	
	boolean existsByUserIdAndDishesId(int userId, int dishesId);
	
	@Transactional
	void deleteByUserId(int userId);

}
