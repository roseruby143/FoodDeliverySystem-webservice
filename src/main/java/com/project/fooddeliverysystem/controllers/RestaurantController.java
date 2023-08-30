package com.project.fooddeliverysystem.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.fooddeliverysystem.dao.admin.RestaurantDAO;
import com.project.fooddeliverysystem.dto.ResponseDto;
import com.project.fooddeliverysystem.exceptions.AlreadyExistException;
import com.project.fooddeliverysystem.exceptions.NotFoundException;
import com.project.fooddeliverysystem.exceptions.UnauthorizedUserException;
import com.project.fooddeliverysystem.model.admin.Restaurant;
import com.project.fooddeliverysystem.model.user.Users;
import com.project.fooddeliverysystem.security.SecurityService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@CrossOrigin(origins = {"http://ec2-54-166-212-49.compute-1.amazonaws.com:4200","http://ec2-54-166-212-49.compute-1.amazonaws.com:4100"}, allowCredentials = "true")
@RestController
@RequestMapping("/v1/restaurant")
public class RestaurantController {
	
	@Autowired
	private RestaurantDAO restDao;

	@Autowired private SecurityService securityService;
	
	/**
	 * Get all restaurant
	 * @param id
	 * @return
	 */
	@GetMapping("")
	public List<Restaurant> getAll() {
		List<Restaurant> restData = restDao.findAll();
		if(!restData.isEmpty()) {
			return restData;
		}
		throw new NotFoundException("Restaurant data does not exist");
	}
	
	/**
	 * Get restaurant by id
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Optional<Restaurant> getOne(@PathVariable("id") int id) {
		Optional<Restaurant> resData = restDao.findById(id);
		//System.out.println("******** get restaurant data with id : resData - "+resData);
		if(resData.isPresent()) {
			return resData;
		}
		throw new NotFoundException("Restaurant data does not exist with id '"+ id +"'");
		
		
	}
	
	/**
	 * Create restaurant data.
	 * @param restaurantReq
	 * @return
	 */
	@PostMapping("")
	public Restaurant save(@RequestBody Restaurant restaurantReq, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			boolean exists = restDao.existsByEmail(restaurantReq.getEmail());
			if (!exists) {
				restaurantReq.setAdded_on(new Date());
				return restDao.save(restaurantReq);
			}
			throw new AlreadyExistException("Restaurant already exist with email '"+restaurantReq.getEmail() +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	

	/**
	 * Update Restaurant
	 * @param Restaurant
	 * @return
	 */
	@PutMapping("/{id}")
	public Restaurant udpate(@RequestBody Restaurant restaurant, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			boolean exists = restDao.existsById(restaurant.getId());
			if (exists) {
				return restDao.save(restaurant);
			}
			throw new NotFoundException("Restaurant does not exist with id '"+ restaurant.getId() +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}

	/**
	 * Delete one restaurant by id
	 * @param productId
	 * @return 
	 */
	@DeleteMapping("/{id}")
	public ResponseDto deleteOne(@PathVariable("id") int id, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			boolean exists = restDao.existsById(id);
			if (exists) {
				restDao.deleteById(id);
				return new ResponseDto("Success","Restaurant deleted", new Date(), null);
			}
			throw new NotFoundException("Restaurant does not exist with id '"+ id +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Get categories
	 * @param id
	 * @return
	 */
	@GetMapping("/categories")
	public List<String> getDistinctCategories() {
		List<String> resData = restDao.getDistinctCategories();
		//System.out.println("******** get restaurant data with id : resData - "+resData);
		if(!resData.isEmpty()) {
			return resData;
		}
		throw new NotFoundException("Categories does not exist");
		
	}

}
