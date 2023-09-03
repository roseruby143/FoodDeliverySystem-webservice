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

import com.project.fooddeliverysystem.dao.user.CartItemsDAO;
import com.project.fooddeliverysystem.dao.user.UserDAO;
import com.project.fooddeliverysystem.dto.ResponseDto;
import com.project.fooddeliverysystem.exceptions.AlreadyExistException;
import com.project.fooddeliverysystem.exceptions.NotFoundException;
import com.project.fooddeliverysystem.exceptions.UnauthorizedUserException;
import com.project.fooddeliverysystem.model.user.CartItems;
import com.project.fooddeliverysystem.model.user.Users;
import com.project.fooddeliverysystem.security.SecurityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@CrossOrigin(origins = {"http://ec2-54-82-234-235.compute-1.amazonaws.com:4200","http://ec2-54-82-234-235.compute-1.amazonaws.com:4100"}, allowCredentials = "true")
//@CrossOrigin(origins = {"http://localhost:4200","http://localhost:4100"}, allowCredentials = "true")
@CrossOrigin(origins = {"${eatout.base.url.user}","${eatout.base.url.admin}"}, allowCredentials = "true")
@RestController
@RequestMapping("/v1")
public class CartItemsController {

	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private CartItemsDAO cartDao;
	@Autowired private SecurityService securityService;
	
	/**
	 * Get all favorites
	 * @param
	 * @return List<Favorites>
	 */
	@GetMapping("/cartitems")
	public List<CartItems> getAll(HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			List<CartItems> restData = cartDao.findAll();
			if(!restData.isEmpty()) {
				return restData;
			}
			throw new NotFoundException("No Cart Item data exist");
			
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Get CartItems by id
	 * @param id
	 * @return Optional<CartItems>
	 */
	@GetMapping("/cartitems/{id}")
	public Optional<CartItems> getOne(@PathVariable("id") int id,  HttpServletRequest request ) {
		if(securityService.isAdmin(request)) {
			Optional<CartItems> restData = cartDao.findById(id);
			if(restData.isPresent()) {
				return restData;
			}
			throw new NotFoundException("No Cart Item data exist");
			
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Get all CartItems By User Id
	 * @param
	 * @return
	 */
	@GetMapping("/user/{id}/cartitems")
	public List<CartItems> getAllByUserId(@PathVariable("id") int id, HttpServletRequest request ) {
		Optional<Users> user = userDao.findById(id);
		if(user!=null && user.isPresent()) {
			boolean isUserAuthorized = securityService.isActionAllowed(user.get().getId()+"",  request );
			
			if(isUserAuthorized) {
				List<CartItems> restData = cartDao.findByUserId(user.get().getId());
				//System.out.println("************ CartItems Data : "+restData.toString());
				/*
				 * if(!restData.isEmpty()) { return restData; } throw new
				 * NotFoundException("CartItems data does not exist");
				 */
				return restData;
			}
			throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		}
		throw new NotFoundException("User does not exist");
		
	}
	
	/**
	 * Create CartItems
	 * @param id
	 * @return
	 */
	@PostMapping("/user/{id}/cartitems")
	public CartItems save(@RequestBody CartItems cartItemsReq, @PathVariable("id") int id, HttpServletRequest request ) {
		Optional<Users> user = userDao.findById(cartItemsReq.getUsers().getId());
		if(user!=null && user.isPresent()) {
			boolean isUserAuthorized = securityService.isActionAllowed(user.get().getId()+"",  request );
			
			if(isUserAuthorized) {
				if(!cartDao.existsByUserIdAndDishesId(user.get().getId(), cartItemsReq.getDishes().getDishId())) {
					return cartDao.save(cartItemsReq);
				}else
					udpate(cartItemsReq, request);
				throw new AlreadyExistException("CartItems already exist with id '"+cartItemsReq.getDishes().getDishId() +"'");
			}
			throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		}
		throw new NotFoundException("User doesnot exist with id '"+user.get().getId() +"'");
	}
	

	/**
	 * Update CartItems
	 * @param cartItems
	 * @return
	 */
	@PutMapping("/cartitems/{id}")
	public CartItems udpate(@RequestBody CartItems cartItems, HttpServletRequest request) {
		
		Optional<Users> user = userDao.findById(cartItems.getUsers().getId());
		if(user!=null && user.isPresent()) {
			boolean isUserAuthorized = securityService.isActionAllowed(user.get().getId()+"",  request );
			
			if(isUserAuthorized) {
				boolean exists = cartDao.existsById(cartItems.getId());
				if (exists) {
					return cartDao.save(cartItems);
				}
				throw new NotFoundException("CartItems does not exist with id '"+ cartItems.getId() +"'");
			}
			throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		}
		throw new NotFoundException("User doesnot exist with id '"+user.get().getId() +"'");
	}

	/**
	 * Delete one CartItems by id
	 * @param id
	 * @return 
	 */
	@DeleteMapping("/cartitems/{id}")
	public ResponseDto deleteOne(@PathVariable("id") int id, HttpServletRequest request) {
		Optional<CartItems> cartItems = cartDao.findById(id);
		if(cartItems!=null && cartItems.isPresent()) {
			Optional<Users> user = userDao.findById(cartItems.get().getUsers().getId());
			if(user!=null && user.isPresent()) {
				boolean isUserAuthorized = securityService.isActionAllowed(user.get().getId()+"",  request );
				
				if(isUserAuthorized) {
					cartDao.deleteById(id);
					return new ResponseDto("Success","CartItems deleted with id "+id, new Date(), null);
				}
				throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
			}
			throw new NotFoundException("User doesnot exist with id '"+user.get().getId() +"'");
		}
		throw new NotFoundException("CartItems does not exist with id '"+ id +"'");
	}
		

	/**
	 * Delete all CartItems by user id
	 * @param id
	 * @return 
	 */
	@DeleteMapping("/user/{id}/cartitems")
	public ResponseDto deleteAllCartItemsByUserId(@PathVariable("id") int id, HttpServletRequest request) {
		//boolean exists = dishesDao.existsById(id);
		Optional<Users> user = userDao.findById(id);
		if(user!=null && user.isPresent()) {
			boolean isUserAuthorized = securityService.isActionAllowed(id+"",  request );
			
			if(isUserAuthorized) {
				cartDao.deleteByUserId(id);
				return new ResponseDto("Success","All CartItems deleted for User id "+id, new Date(), null);
			}
			throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		}
		throw new NotFoundException("User doesnot exist with id '"+user.get().getId() +"'");
	}
	
}
