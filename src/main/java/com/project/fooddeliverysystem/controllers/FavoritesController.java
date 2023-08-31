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

import com.project.fooddeliverysystem.dao.user.FavoritesDAO;
import com.project.fooddeliverysystem.dao.user.UserDAO;
import com.project.fooddeliverysystem.dto.ResponseDto;
import com.project.fooddeliverysystem.exceptions.AlreadyExistException;
import com.project.fooddeliverysystem.exceptions.NotFoundException;
import com.project.fooddeliverysystem.exceptions.UnauthorizedUserException;
import com.project.fooddeliverysystem.model.user.Favorites;
import com.project.fooddeliverysystem.model.user.Users;
import com.project.fooddeliverysystem.security.SecurityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = {"http://ec2-54-82-234-235.compute-1.amazonaws.com:4200","http://ec2-54-82-234-235.compute-1.amazonaws.com:4100"}, allowCredentials = "true")
//@CrossOrigin(origins = {"http://localhost:4200","http://localhost:4100"}, allowCredentials = "true")
@RestController
@RequestMapping("/v1")
public class FavoritesController {

	@Autowired 
	private FavoritesDAO favDao;
	
	@Autowired
	private UserDAO userDao;
	@Autowired private SecurityService securityService;
	
	/**
	 * Get all favorites
	 * @param
	 * @return List<Favorites>
	 */
	@GetMapping("/favorites")
	public List<Favorites> getAll(HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			List<Favorites> restData = favDao.findAll();
			if(!restData.isEmpty()) {
				return restData;
			}
			//return new ResponseDto("Failure","Order Items data does not exist", new Date(), loginReqDto.getEmail()); 
			throw new NotFoundException("Favorite Items data does not exist");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Get favorite by id
	 * @param id
	 * @return Optional<Favorites>
	 */
	@GetMapping("/favorites/{id}")
	public Optional<Favorites> getOne(@PathVariable("id") int id , HttpServletRequest request) {
		Optional<Favorites> fav = favDao.findById(id);
		if(fav!=null && fav.isPresent()) {
			boolean isUserAuthorized = securityService.isActionAllowed(id+"",  request );
			
			if(isUserAuthorized) {
				return fav;
			}
			throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		}
		throw new NotFoundException("Favorite does not exist with id '"+ id +"'");
		
	}
	
	/**
	 * Get all Favorites By User Id
	 * @param
	 * @return
	 */
	@GetMapping("/user/{id}/favorites")
	public List<Favorites> getAllByUserId(@PathVariable("id") int id, HttpServletRequest request) {
		Optional<Users> userData = userDao.findById(id);
		if(userData!=null && userData.isPresent()) {
			//System.out.println("************ userData : "+userData.toString());
			//System.out.println("************ orderId : "+orderData.get().getOrderId());
			
			boolean isUserAuthorized = securityService.isActionAllowed(id+"",  request );
			if(isUserAuthorized) {
				List<Favorites> restData = favDao.findByUserId(userData.get().getId());
				//System.out.println("************ Favorite Data : "+restData.toString());
				if(!restData.isEmpty()) {
					return restData;
				}
				throw new NotFoundException("Favorite data does not exist");
			}
			throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		}
		throw new NotFoundException("User does not exist");
	}
	
	/**
	 * Create favorites
	 * @param OIReq
	 * @return
	 */
	@PostMapping("/user/{id}/favorites")
	public Favorites save(@RequestBody Favorites favReq, @PathVariable("id") int id, HttpServletRequest request) {
		Optional<Users> userData = userDao.findById(id);
		if(userData!=null && userData.isPresent()) {
			//System.out.println("************ userData : "+userData.toString());
			//System.out.println("************ orderId : "+orderData.get().getOrderId());
			
			boolean isUserAuthorized = securityService.isActionAllowed(id+"",  request );
			if(isUserAuthorized) {
				if(!favDao.existsByUserIdAndDishesId(id, favReq.getDishes().getDishId())) {
					return favDao.save(favReq);
				}
				throw new AlreadyExistException("Favorite Dish already exist with id '"+favReq.getDishes().getDishId() +"'");
			}
			throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		}
		throw new NotFoundException("User doesnot exist with id '"+id +"'");
	}
	

	/**
	 * Update favorite
	 * @param favItems
	 * @return
	 */
	@PutMapping("/favorites/{id}")
	public Favorites udpate(@RequestBody Favorites favItem, HttpServletRequest request) {
		boolean isUserAuthorized = securityService.isActionAllowed(favItem.getUser().getId()+"",  request );
		if(isUserAuthorized) {
			boolean exists = favDao.existsById(favItem.getId());
			if (exists) {
				return favDao.save(favItem);
			}
			throw new NotFoundException("Favorites does not exist with id '"+ favItem.getId() +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		
	}

	/**
	 * Delete one Favorite by id
	 * @param id
	 * @return 
	 */
	@DeleteMapping("/favorites/{id}")
	public ResponseDto deleteOne(@PathVariable("id") int id, HttpServletRequest request) {
		Optional<Favorites> favItem = favDao.findById(id);
		if(favItem!=null && favItem.isPresent()) {
			boolean isUserAuthorized = securityService.isActionAllowed(favItem.get().getUser().getId()+"",  request );
			if(isUserAuthorized) {
				favDao.deleteById(id);
				return new ResponseDto("Success","Favorites deleted with id "+id, new Date(), null);
			}
			throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		}
		throw new NotFoundException("Favorites does not exist with id '"+ id +"'");
	}

	/**
	 * Delete all Favorite by user id
	 * @param id
	 * @return 
	 */
	@DeleteMapping("/user/{id}/favorites")
	public ResponseDto deleteAllFavoritesByUserId(@PathVariable("id") int id, HttpServletRequest request) {
		Optional<Favorites> favItem = favDao.findById(id);
		if(favItem!=null && favItem.isPresent()) {
			boolean isUserAuthorized = securityService.isActionAllowed(favItem.get().getUser().getId()+"",  request );
			if(isUserAuthorized) {
				//boolean exists = dishesDao.existsById(id);
				if (userDao.existsById(id)) {
					favDao.deleteByUserId(id);
					return new ResponseDto("Success","All Favorite deleted for User id "+id, new Date(), null);
				}
				throw new NotFoundException("User with id '"+ id +"' dosenot exist.");
			}
			throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		}
		throw new NotFoundException("Favorites item does not exist for user with id '"+ id +"'");
	}
	
}
