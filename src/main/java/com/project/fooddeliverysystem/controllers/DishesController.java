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

import com.project.fooddeliverysystem.dao.admin.DishesDAO;
import com.project.fooddeliverysystem.dao.admin.RestaurantDAO;
import com.project.fooddeliverysystem.dto.ResponseDto;
import com.project.fooddeliverysystem.exceptions.NotFoundException;
import com.project.fooddeliverysystem.exceptions.UnauthorizedUserException;
import com.project.fooddeliverysystem.model.admin.Dishes;
import com.project.fooddeliverysystem.model.user.Users;
import com.project.fooddeliverysystem.security.SecurityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = {"http://ec2-54-82-234-235.compute-1.amazonaws.com:4200","http://ec2-54-82-234-235.compute-1.amazonaws.com:4100"}, allowCredentials = "true")
//@CrossOrigin(origins = {"http://localhost:4200","http://localhost:4100"}, allowCredentials = "true")
@RestController
@RequestMapping("/v1")
public class DishesController {
	
	@Autowired
	private DishesDAO dishesDao;
	
	@Autowired
	private RestaurantDAO resDao;
	@Autowired private SecurityService securityService;
	
	/**
	 * Get all dishes
	 * @param id
	 * @return
	 */
	@GetMapping("/dishes")
	public List<Dishes> getAll() {
		List<Dishes> restData = dishesDao.findAll();
		if(!restData.isEmpty()) {
			return restData;
		}
		throw new NotFoundException("Dishes does not exist");
	}
	
	/**
	 * Get dishes by id
	 * @param id
	 * @return
	 */
	@GetMapping("/dishes/{id}")
	public Optional<Dishes> getOne(@PathVariable("id") int id) {
		Optional<Dishes> resData = dishesDao.findById(id);
		if(resData.isPresent()) {
			return resData;
		}
		throw new NotFoundException("Dish does not exist with id '"+ id +"'. Create a new Dish");
	}
	
	/**
	 * Get dishes by restaurant id
	 * @param id
	 * @return
	 */
	@GetMapping("/restaurant/{id}/dishes")
	public List<Dishes> getAllDishesByRestaurantId(@PathVariable("id") int id) {
		if(resDao.findById(id).isEmpty())
			throw new NotFoundException("Restaurant with id '"+ id +"' dosenot exist.");
		List<Dishes> dishesInResttaurantData = dishesDao.findByRestaurantId(id);
		if(!dishesInResttaurantData.isEmpty()) {
			return dishesInResttaurantData;
		}
		throw new NotFoundException("Restaurant with id '"+ id +"' doesnot have any dishes");
	}
	
	/**
	 * Create dish data.
	 * @param dishReq
	 * @return
	 */
	@PostMapping("/restaurant/{restaurantId}/dishes")
	public Dishes save(@RequestBody Dishes dishesReq, @PathVariable("restaurantId") int restaurantId, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			if (resDao.existsById(restaurantId)) {
				dishesReq.setAdded_on(new Date());
				return dishesDao.save(dishesReq);
			}
			throw new NotFoundException("Restaurant with id '"+ restaurantId +"' dosenot exist.");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	

	/**
	 * Update Dishes
	 * @param Dishes
	 * @return
	 */
	@PutMapping("/dishes/{id}")
	public Dishes udpate(@RequestBody Dishes dish, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			//System.out.println("**************** dish : "+dish.toString());
			boolean exists = dishesDao.existsById(dish.getDishId());
			if (exists) {
				return dishesDao.save(dish);
			}
			throw new NotFoundException("Dishes does not exist with id '"+ dish.getDishId() +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}

	/**
	 * Delete one dish by id
	 * @param dishId
	 * @return 
	 */
	@DeleteMapping("/dishes/{id}")
	public ResponseDto deleteOne(@PathVariable("id") int id, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			boolean exists = dishesDao.existsById(id);
			if (exists) {
				dishesDao.deleteById(id);
				return new ResponseDto("Success","Dish deleted", new Date(), null);
			}
			throw new NotFoundException("Dish does not exist with id '"+ id +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}

	/**
	 * Delete all dishes by restaurant id
	 * @param restaurantId
	 * @return 
	 */
	@DeleteMapping("/restaurant/{restaurantId}/dishes")
	public ResponseDto deleteAllDishesByRestaurantId(@PathVariable("restaurantId") int restaurantId, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			//boolean exists = dishesDao.existsById(id);
			if (resDao.existsById(restaurantId)) {
				dishesDao.deleteByRestaurantId(restaurantId);
				return new ResponseDto("Success","All Dishes deleted for restaurant id "+restaurantId, new Date(), null);
			}
			throw new NotFoundException("Restaurant with id '"+ restaurantId +"' dosenot exist.");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Get categories
	 * @param id
	 * @return
	 */
	@GetMapping("/restaurant/{restaurantId}/dishes/categories")
	public List<String> getDistinctCategories(@PathVariable("restaurantId") int restaurantId) {
		List<String> resData = dishesDao.getDistinctCategories(restaurantId);
		System.out.println("******** get restaurant data with id : resData - "+resData);
		if(!resData.isEmpty()) {
			return resData;
		}
		throw new NotFoundException("Categories does not exist");
	}

}
