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

import com.project.fooddeliverysystem.dao.user.FavoritesDAO;
import com.project.fooddeliverysystem.dao.user.UserDAO;
import com.project.fooddeliverysystem.dto.ResponseDto;
import com.project.fooddeliverysystem.exceptions.AlreadyExistException;
import com.project.fooddeliverysystem.exceptions.NotFoundException;
import com.project.fooddeliverysystem.model.user.Favorites;
import com.project.fooddeliverysystem.model.user.Users;

@RestController
@RequestMapping("/v1")
public class FavoritesController {

	@Autowired 
	private FavoritesDAO favDao;
	
	@Autowired
	private UserDAO userDao;
	
	/**
	 * Get all favorites
	 * @param
	 * @return List<Favorites>
	 */
	@GetMapping("/favorites")
	public List<Favorites> getAll() {
		List<Favorites> restData = favDao.findAll();
		if(!restData.isEmpty()) {
			return restData;
		}
		//return new ResponseDto("Failure","Order Items data does not exist", new Date(), loginReqDto.getEmail()); 
		throw new NotFoundException("Favorite Items data does not exist");
	}
	
	/**
	 * Get favorite by id
	 * @param id
	 * @return Optional<Favorites>
	 */
	@GetMapping("/favorites/{id}")
	public Optional<Favorites> getOne(@PathVariable("id") int id) {
		Optional<Favorites> resData = favDao.findById(id);
		if(resData.isPresent()) {
			return resData;
		}
		throw new NotFoundException("Favorite does not exist with id '"+ id +"'");
	}
	
	/**
	 * Get all Favorites By User Id
	 * @param
	 * @return
	 */
	@GetMapping("/user/{id}/favorites")
	public List<Favorites> getAllByUserId(@PathVariable("id") int id) {
		if(userDao.existsById(id)) {
			Optional<Users> userData = userDao.findById(id);
			//System.out.println("************ userData : "+userData.toString());
			//System.out.println("************ orderId : "+orderData.get().getOrderId());
			List<Favorites> restData = favDao.findByUserId(userData.get().getUserId());
			//System.out.println("************ Favorite Data : "+restData.toString());
			if(!restData.isEmpty()) {
				return restData;
			}
			throw new NotFoundException("Favorite data does not exist");
		}
		throw new NotFoundException("User does not exist");
	}
	
	/**
	 * Create favorites
	 * @param OIReq
	 * @return
	 */
	@PostMapping("/user/{id}/favorites")
	public Favorites save(@RequestBody Favorites favReq, @PathVariable("id") int id) {
		boolean exists = userDao.existsById(id);
		if (exists) {
			//System.out.println("************ favReq.getDishes() : "+favReq.toString());
			//boolean existByUserAndDIsh = favDao.existsByUserIdAndDishesId(id, favReq.getDishes().getDishId());
			//System.out.println("************ existByUserAndDIsh : "+existByUserAndDIsh);
			if(!favDao.existsByUserIdAndDishesId(id, favReq.getDishes().getDishId())) {
				return favDao.save(favReq);
			}
			throw new AlreadyExistException("Favorite Dish already exist with id '"+favReq.getDishes().getDishId() +"'");
		}
		throw new NotFoundException("User doesnot exist with id '"+id +"'");
	}
	

	/**
	 * Update favorite
	 * @param favItems
	 * @return
	 */
	@PutMapping("/favorites/{id}")
	public Favorites udpate(@RequestBody Favorites favItem) {
		boolean exists = favDao.existsById(favItem.getId());
		if (exists) {
			return favDao.save(favItem);
		}
		throw new NotFoundException("Favorites does not exist with id '"+ favItem.getId() +"'");
	}

	/**
	 * Delete one Favorite by id
	 * @param id
	 * @return 
	 */
	@DeleteMapping("/favorites/{id}")
	public ResponseDto deleteOne(@PathVariable("id") int id) {
		boolean exists = favDao.existsById(id);
		if (exists) {
			favDao.deleteById(id);
			return new ResponseDto("Success","Favorites deleted with id "+id, new Date(), null);
		}
		throw new NotFoundException("Favorites does not exist with id '"+ id +"'");
	}

	/**
	 * Delete all Favorite by user id
	 * @param id
	 * @return 
	 */
	@DeleteMapping("/user/{id}/favorites")
	public ResponseDto deleteAllFavoritesByUserId(@PathVariable("id") int id) {
		//boolean exists = dishesDao.existsById(id);
		if (userDao.existsById(id)) {
			favDao.deleteByUserId(id);
			return new ResponseDto("Success","All Favorite deleted for User id "+id, new Date(), null);
		}
		throw new NotFoundException("User with id '"+ id +"' dosenot exist.");
	}
}
