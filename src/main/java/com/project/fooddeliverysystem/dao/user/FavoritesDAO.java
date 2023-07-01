package com.project.fooddeliverysystem.dao.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.fooddeliverysystem.model.user.Favorites;

import jakarta.transaction.Transactional;

@Repository
public interface FavoritesDAO extends JpaRepository<Favorites, Integer>{

	List<Favorites> findByUserId(int userId);
	
	boolean existsByUserIdAndDishesId(int userId, int dishId);
	
	@Transactional
	void deleteByUserId(int userId);
	
}
