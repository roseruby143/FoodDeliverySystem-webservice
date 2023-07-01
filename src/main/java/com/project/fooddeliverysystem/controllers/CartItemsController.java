package com.project.fooddeliverysystem.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.project.fooddeliverysystem.model.user.CartItems;
import com.project.fooddeliverysystem.model.user.Users;

@RestController
@RequestMapping("/v1")
public class CartItemsController {

	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private CartItemsDAO cartDao;
	
	/**
	 * Get all favorites
	 * @param
	 * @return List<Favorites>
	 */
	@GetMapping("/cartitems")
	public List<CartItems> getAll() {
		List<CartItems> restData = cartDao.findAll();
		if(!restData.isEmpty()) {
			return restData;
		}
		//return new ResponseDto("Failure","Order Items data does not exist", new Date(), loginReqDto.getEmail()); 
		throw new NotFoundException("CartItems data does not exist");
	}
	
	/**
	 * Get CartItems by id
	 * @param id
	 * @return Optional<CartItems>
	 */
	@GetMapping("/cartitems/{id}")
	public Optional<CartItems> getOne(@PathVariable("id") int id) {
		Optional<CartItems> resData = cartDao.findById(id);
		if(resData.isPresent()) {
			return resData;
		}
		throw new NotFoundException("CartItems does not exist with id '"+ id +"'");
	}
	
	/**
	 * Get all CartItems By User Id
	 * @param
	 * @return
	 */
	@GetMapping("/user/{id}/cartitems")
	public List<CartItems> getAllByUserId(@PathVariable("id") int id) {
		if(userDao.existsById(id)) {
			Optional<Users> userData = userDao.findById(id);
			//System.out.println("************ userData : "+userData.toString());
			//System.out.println("************ orderId : "+orderData.get().getOrderId());
			List<CartItems> restData = cartDao.findByUserId(userData.get().getUserId());
			//System.out.println("************ CartItems Data : "+restData.toString());
			if(!restData.isEmpty()) {
				return restData;
			}
			throw new NotFoundException("CartItems data does not exist");
		}
		throw new NotFoundException("User does not exist");
	}
	
	/**
	 * Create CartItems
	 * @param id
	 * @return
	 */
	@PostMapping("/user/{id}/cartitems")
	public CartItems save(@RequestBody CartItems cartItemsReq, @PathVariable("id") int id) {
		boolean exists = userDao.existsById(id);
		if (exists) {
			//System.out.println("************ CartItemsReq.getDishes() : " + cartItemsReq.toString());
			//boolean existByUserAndDIsh = cartDao.existsByUserIdAndDishesId(id, cartItemsReq.getDishes().getDishId());
			//System.out.println("************ existByUserAndDIsh : "+existByUserAndDIsh);
			if(!cartDao.existsByUserIdAndDishesId(id, cartItemsReq.getDishes().getDishId())) {
				return cartDao.save(cartItemsReq);
			}
			throw new AlreadyExistException("CartItems already exist with id '"+cartItemsReq.getDishes().getDishId() +"'");
		}
		throw new NotFoundException("User doesnot exist with id '"+id +"'");
	}
	

	/**
	 * Update CartItems
	 * @param cartItems
	 * @return
	 */
	@PutMapping("/cartitems/{id}")
	public CartItems udpate(@RequestBody CartItems cartItems) {
		boolean exists = cartDao.existsById(cartItems.getId());
		if (exists) {
			return cartDao.save(cartItems);
		}
		throw new NotFoundException("CartItems does not exist with id '"+ cartItems.getId() +"'");
	}

	/**
	 * Delete one CartItems by id
	 * @param id
	 * @return 
	 */
	@DeleteMapping("/cartitems/{id}")
	public ResponseDto deleteOne(@PathVariable("id") int id) {
		boolean exists = cartDao.existsById(id);
		if (exists) {
			cartDao.deleteById(id);
			return new ResponseDto("Success","CartItems deleted with id "+id, new Date(), null);
		}
		throw new NotFoundException("CartItems does not exist with id '"+ id +"'");
	}

	/**
	 * Delete all CartItems by user id
	 * @param id
	 * @return 
	 */
	@DeleteMapping("/user/{id}/cartitems")
	public ResponseDto deleteAllCartItemsByUserId(@PathVariable("id") int id) {
		//boolean exists = dishesDao.existsById(id);
		if (userDao.existsById(id)) {
			cartDao.deleteByUserId(id);
			return new ResponseDto("Success","All CartItems deleted for User id "+id, new Date(), null);
		}
		throw new NotFoundException("User with id '"+ id +"' dosenot exist.");
	}
}
