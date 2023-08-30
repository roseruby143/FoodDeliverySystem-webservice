package com.project.fooddeliverysystem.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.fooddeliverysystem.dao.admin.DeliveryDAO;
import com.project.fooddeliverysystem.exceptions.AlreadyExistException;
import com.project.fooddeliverysystem.exceptions.NotFoundException;
import com.project.fooddeliverysystem.exceptions.UnauthorizedUserException;
import com.project.fooddeliverysystem.model.admin.Delivery;
import com.project.fooddeliverysystem.model.user.Users;
import com.project.fooddeliverysystem.security.SecurityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = {"http://ec2-54-82-234-235.compute-1.amazonaws.com:4200","http://ec2-54-82-234-235.compute-1.amazonaws.com:4100"}, allowCredentials = "true")
@RestController
@RequestMapping("/v1")
public class DeliveryController {

	@Autowired
	private DeliveryDAO deliveryDao;
	@Autowired private SecurityService securityService;
	
	/**
	 * Get all Delivery
	 * @param id
	 * @return
	 */
	@GetMapping("/delivery")
	public List<Delivery> getAll(HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			List<Delivery> restData = deliveryDao.findAll();
			if(!restData.isEmpty()) {
				return restData;
			}
			throw new NotFoundException("Delivery data does not exist");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Get Delivery by id
	 * @param id
	 * @return
	 */
	@GetMapping("/delivery/{id}")
	public Optional<Delivery> getOne(@PathVariable("id") int id, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			Optional<Delivery> resData = deliveryDao.findById(id);
			if(resData.isPresent()) {
				return resData;
			}
			throw new NotFoundException("Delivery data does not exist with id '"+ id +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Get all Delivery for Driver Id
	 * @param id
	 * @return
	 */
	@GetMapping("/driver/{id}/delivery")
	public List<Delivery> getDeliveryByDriver(@PathVariable("id") int id, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			List<Delivery> deliveryData = deliveryDao.findByDriverId(id);
			if(!deliveryData.isEmpty()) {
				return deliveryData;
			}
			throw new NotFoundException("Delivery data does not exist for driver '"+id+"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Create Delivery data.
	 * @param DeliveryReq
	 * @return
	 */
	@PostMapping("/delivery")
	public Delivery save(@RequestBody Delivery deliveryReq, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			if(!deliveryDao.existsById(deliveryReq.getId())) {
				deliveryReq.setCreatedOn(new Date());
				return deliveryDao.save(deliveryReq);
			}
			throw new AlreadyExistException("Order does not exist with order Id : "+deliveryReq.getId());
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	

	/**
	 * Update Delivery
	 * @param id
	 * @return
	 */
	@PutMapping("/delivery/{id}")
	public Delivery udpate(@RequestBody Delivery delivery, @PathVariable("id") int id, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			boolean exists = deliveryDao.existsById(id);
			if (exists) {
				return deliveryDao.save(delivery);
			}
			throw new NotFoundException("Delivery does not exist with id '"+ delivery.getId() +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}

	/**
	 * Delete delivery is not an option
	 * @param id
	 * @return 
	 */
	/*
	 * @DeleteMapping("/delivery/{id}") public ResponseDto
	 * deleteOne(@PathVariable("id") int id) { boolean exists =
	 * deliveryDao.existsById(id); if (exists) { deliveryDao.deleteById(id); return
	 * new ResponseDto("Success","Delivery deleted", new Date(), null); } return new
	 * ResponseDto("Not Found","Delivery does not exist with id '"+ id +"'", new
	 * Date(), null);
	 * 
	 * }
	 */
}
