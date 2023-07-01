package com.project.fooddeliverysystem.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.fooddeliverysystem.dao.admin.DriversDAO;
import com.project.fooddeliverysystem.exceptions.AlreadyExistException;
import com.project.fooddeliverysystem.exceptions.NotFoundException;
import com.project.fooddeliverysystem.model.admin.Drivers;

@RestController
@RequestMapping("/v1/driver")
public class DriversController {
	
	@Autowired
	private DriversDAO driverDao;
	
	/**
	 * Get all Drivers
	 * @param id
	 * @return
	 */
	@GetMapping("")
	public List<Drivers> getAll() {
		List<Drivers> restData = driverDao.findAll();
		if(!restData.isEmpty()) {
			return restData;
		}
		throw new NotFoundException("Drivers data does exist");
	}
	
	/**
	 * Get Driver by id
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Optional<Drivers> getOne(@PathVariable("id") int id) {
		Optional<Drivers> resData = driverDao.findById(id);
		if(resData.isPresent()) {
			return resData;
		}
		throw new NotFoundException("Driver data does exist with id '"+ id +"'");
	}
	
	/**
	 * Create Drivers data.
	 * @param DriversReq
	 * @return
	 */
	@PostMapping("")
	public Drivers save(@RequestBody Drivers driverReq) {
		if(driverDao.existsByEmail(driverReq.getEmail())){
			throw new AlreadyExistException("Drivers already exist with email '"+driverReq.getEmail() +"'");
		}
		driverReq.setCreated_on(new Date());
		return driverDao.save(driverReq);
	}
	

	/**
	 * Update Drivers
	 * @param id
	 * @return
	 */
	@PutMapping("/{id}")
	public Drivers udpate(@RequestBody Drivers driver) {
		boolean exists = driverDao.existsById(driver.getId());
		if (exists) {
			return driverDao.save(driver);
		}
		throw new NotFoundException("Drivers does not exist with id '"+ driver.getId() +"'");
	}

	/**
	 * Deleting a driver is not possible
	 * We will change the status from Active to Inactive
	 *  
	 */


}
