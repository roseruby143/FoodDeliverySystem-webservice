package com.project.fooddeliverysystem.dao.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.fooddeliverysystem.model.admin.Dishes;

import jakarta.transaction.Transactional;

import java.util.List;


@Repository
public interface DishesDAO  extends JpaRepository<Dishes, Integer>{

	List<Dishes> findByRestaurantId(int restaurantId);
	
	@Transactional
	void deleteByRestaurantId(int restaurantId);
	
	@Query("SELECT DISTINCT d.category from Dishes d inner join restaurant r on d.restaurant.id = r.id where r.id = ?1")
	List<String> getDistinctCategories(int id);
}
