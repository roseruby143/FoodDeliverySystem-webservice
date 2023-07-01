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

import com.project.fooddeliverysystem.dao.admin.DeliveryDAO;
import com.project.fooddeliverysystem.dto.ResponseDto;
import com.project.fooddeliverysystem.exceptions.AlreadyExistException;
import com.project.fooddeliverysystem.exceptions.NotFoundException;
import com.project.fooddeliverysystem.model.admin.Delivery;

@RestController
@RequestMapping("/v1")
public class DeliveryController {

	@Autowired
	private DeliveryDAO deliveryDao;
	
	/**
	 * Get all Delivery
	 * @param id
	 * @return
	 */
	@GetMapping("/delivery")
	public List<Delivery> getAll() {
		List<Delivery> restData = deliveryDao.findAll();
		if(!restData.isEmpty()) {
			return restData;
		}
		throw new NotFoundException("Delivery data does not exist");
	}
	
	/**
	 * Get Delivery by id
	 * @param id
	 * @return
	 */
	@GetMapping("/delivery/{id}")
	public Optional<Delivery> getOne(@PathVariable("id") int id) {
		Optional<Delivery> resData = deliveryDao.findById(id);
		if(resData.isPresent()) {
			return resData;
		}
		throw new NotFoundException("Delivery data does not exist with id '"+ id +"'");
	}
	
	/**
	 * Create Delivery data.
	 * @param DeliveryReq
	 * @return
	 */
	@PostMapping("/delivery")
	public Delivery save(@RequestBody Delivery deliveryReq) {
		if(!deliveryDao.existsById(deliveryReq.getDeliveryId())) {
			deliveryReq.setDeliveryDate(new Date());
			return deliveryDao.save(deliveryReq);
		}
		throw new AlreadyExistException("Order does not exist with order Id : "+deliveryReq.getDeliveryId());
	}
	

	/**
	 * Update Delivery
	 * @param id
	 * @return
	 */
	@PutMapping("/delivery/{id}")
	public Delivery udpate(@RequestBody Delivery delivery, @PathVariable("id") int id) {
		boolean exists = deliveryDao.existsById(id);
		if (exists) {
			return deliveryDao.save(delivery);
		}
		throw new NotFoundException("Delivery does not exist with id '"+ delivery.getDeliveryId() +"'");
	}

	/**
	 * Delete one Delivery by id
	 * @param id
	 * @return 
	 */
	@DeleteMapping("/delivery/{id}")
	public ResponseDto deleteOne(@PathVariable("id") int id) {
		boolean exists = deliveryDao.existsById(id);
		if (exists) {
			deliveryDao.deleteById(id);
			return new ResponseDto("Success","Delivery deleted", new Date(), null);
		}
		return new ResponseDto("Not Found","Delivery does not exist with id '"+ id +"'", new Date(), null);
		
	}
}
