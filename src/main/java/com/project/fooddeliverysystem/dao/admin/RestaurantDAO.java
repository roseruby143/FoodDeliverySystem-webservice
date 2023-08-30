package com.project.fooddeliverysystem.dao.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.fooddeliverysystem.model.admin.Restaurant;

@Repository
public interface RestaurantDAO extends JpaRepository<Restaurant, Integer>{
	
	Page<Restaurant> findAll(Pageable pageable);

	boolean existsByEmail(String email);
	
	@Query("SELECT DISTINCT r.category from Restaurant r")
	List<String> getDistinctCategories();

}
