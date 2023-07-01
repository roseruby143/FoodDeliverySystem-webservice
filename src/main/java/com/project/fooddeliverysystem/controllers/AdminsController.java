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

import com.project.fooddeliverysystem.dao.admin.AdminDAO;
import com.project.fooddeliverysystem.dto.LoginReqDto;
import com.project.fooddeliverysystem.dto.ResponseDto;
import com.project.fooddeliverysystem.exceptions.AlreadyExistException;
import com.project.fooddeliverysystem.exceptions.NotFoundException;
import com.project.fooddeliverysystem.model.admin.Admins;
import com.project.fooddeliverysystem.services.admin.AdminsService;

//@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/v1/admin")
public class AdminsController {
	
	@Autowired private AdminDAO adminDao;
	@Autowired private AdminsService adminService;
	
	/**
	 * Get all admin user
	 * @param 
	 * @return
	 */
	@GetMapping("")
	public List<Admins> getAll() {
		List<Admins> adminData = adminDao.findAll();
		if(!adminData.isEmpty()) {
			return adminData;
		}
		throw new NotFoundException("No Admins data exist");
	}
	
	/**
	 * Get admin user by id
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Optional<Admins> getOne(@PathVariable("id") int id) {
		Optional<Admins> adminData = adminService.findById(id);
		if(adminData.isPresent()) {
			return adminData;
		}
		throw new NotFoundException("Admins data does exist with id '"+ id +"'");
	}
	
	/**
	 * Create admin user.
	 * @param adminsReq
	 * @return
	 */
	@PostMapping("")
	public Admins save(@RequestBody() Admins adminsReq) {
		boolean eixts = adminService.existsByEmail(adminsReq.getEmail());
		if (!eixts) {
			adminsReq.setAddedOn(new Date());
			return adminService.save(adminsReq);
		}
		throw new AlreadyExistException("Admin user already exist with email '"+adminsReq.getEmail() +"'");
	}
	

	/**
	 * Update Admins
	 * @param Admins
	 * @return
	 */
	@PutMapping("/{id}")
	public Admins udpate(@RequestBody Admins admins) {
		boolean eixts = adminService.existsById(admins.getAdminId());
		if (eixts) {
			return adminService.save(admins);
		}
		throw new NotFoundException("Admin user does exist with id '"+ admins.getAdminId() +"'");
	}

	/**
	 * Delete one product by id
	 * @param productId
	 * @return 
	 */
	@DeleteMapping("/{id}")
	public ResponseDto deleteOne(@PathVariable("id") int id) {
		boolean eixts = adminService.existsById(id);
		if (eixts) {
			adminService.deleteById(id);
			return new ResponseDto("Success","Admin user deleted", new Date(), null);
		}
		throw new NotFoundException("Admin user does exist with id '"+ id +"'");
	}
	
	/**
	 * Validate Login for admin user.
	 * @param adminsReq
	 * @return
	 */
	@PostMapping("/login")
	public ResponseDto save(@RequestBody LoginReqDto loginReqDto) {
		boolean eixts = adminService.existsByEmail(loginReqDto.getEmail());
		if (eixts) {
			boolean match = adminService.login(loginReqDto);
			if(match) {
				return new ResponseDto("Success","Admin login successfull", new Date(), loginReqDto.getEmail()); 
			}else {
				throw new NotFoundException("Invalid password, password mismatch error.");
			}
		}
		throw new NotFoundException("Admin user does exist with email '"+loginReqDto.getEmail() +"'");
	}

}
