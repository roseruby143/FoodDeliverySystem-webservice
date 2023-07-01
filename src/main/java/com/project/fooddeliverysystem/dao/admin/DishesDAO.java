package com.project.fooddeliverysystem.dao.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.fooddeliverysystem.model.admin.Dishes;
import com.project.fooddeliverysystem.model.admin.Restaurant;

import jakarta.transaction.Transactional;

import java.util.List;


@Repository
public interface DishesDAO  extends JpaRepository<Dishes, Integer>{

	List<Dishes> findByRestaurantId(int restaurantId);
	
	@Transactional
	void deleteByRestaurantId(int restaurantId);
}
