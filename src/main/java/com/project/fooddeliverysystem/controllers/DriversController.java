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

import com.project.fooddeliverysystem.dao.admin.DriversDAO;
import com.project.fooddeliverysystem.exceptions.AlreadyExistException;
import com.project.fooddeliverysystem.exceptions.NotFoundException;
import com.project.fooddeliverysystem.exceptions.UnauthorizedUserException;
import com.project.fooddeliverysystem.model.admin.Drivers;
import com.project.fooddeliverysystem.model.user.Users;
import com.project.fooddeliverysystem.security.SecurityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = {"http://ec2-54-166-212-49.compute-1.amazonaws.com:4200","http://ec2-54-166-212-49.compute-1.amazonaws.com:4100"}, allowCredentials = "true")
@RestController
@RequestMapping("/v1/driver")
public class DriversController {
	
	@Autowired
	private DriversDAO driverDao;
	@Autowired private SecurityService securityService;
	
	/**
	 * Get all Drivers
	 * @param id
	 * @return
	 */
	@GetMapping("")
	public List<Drivers> getAll( HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			List<Drivers> restData = driverDao.findAll();
			if(!restData.isEmpty()) {
				return restData;
			}
			throw new NotFoundException("Drivers data does exist");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Get Driver by id
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Optional<Drivers> getOne(@PathVariable("id") int id, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			Optional<Drivers> resData = driverDao.findById(id);
			if(resData.isPresent()) {
				return resData;
			}
			throw new NotFoundException("Driver data does exist with id '"+ id +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Create Drivers data.
	 * @param DriversReq
	 * @return
	 */
	@PostMapping("")
	public Drivers save(@RequestBody Drivers driverReq, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			if(driverDao.existsByEmail(driverReq.getEmail())){
				throw new AlreadyExistException("Drivers already exist with email '"+driverReq.getEmail() +"'");
			}
			driverReq.setCreated_on(new Date());
			return driverDao.save(driverReq);
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	

	/**
	 * Update Drivers
	 * @param id
	 * @return
	 */
	@PutMapping("/{id}")
	public Drivers udpate(@RequestBody Drivers driver, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			boolean exists = driverDao.existsById(driver.getId());
			if (exists) {
				return driverDao.save(driver);
			}
			throw new NotFoundException("Drivers does not exist with id '"+ driver.getId() +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}

	/**
	 * Deleting a driver is not possible
	 * We will change the status from Active to Inactive
	 *  
	 */


}
