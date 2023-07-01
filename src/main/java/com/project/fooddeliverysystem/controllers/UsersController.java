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

import com.project.fooddeliverysystem.dao.user.UserDAO;
import com.project.fooddeliverysystem.dto.LoginReqDto;
import com.project.fooddeliverysystem.dto.ResponseDto;
import com.project.fooddeliverysystem.exceptions.AlreadyExistException;
import com.project.fooddeliverysystem.exceptions.NotFoundException;
import com.project.fooddeliverysystem.model.user.Users;
import com.project.fooddeliverysystem.services.user.UserService;

@RestController
@RequestMapping("/v1/user")
public class UsersController {
	@Autowired private UserDAO userDao;
	@Autowired private UserService userService;
	
	/**
	 * Validate Login for user.
	 * @param userReq
	 * @return
	 */
	@PostMapping("/login")
	public ResponseDto save(@RequestBody LoginReqDto loginReqDto) {
		boolean eixts = userDao.existsByEmail(loginReqDto.getEmail());
		if (eixts) {
			boolean match = userService.login(loginReqDto);
			if(match) {
				return new ResponseDto("Success","User login successfull", new Date(), loginReqDto.getEmail()); 
			}else {
				throw new NotFoundException("Invalid password, password mismatch error.");
			}
		}
		throw new NotFoundException("User does not exist with email '"+loginReqDto.getEmail() +"'");
	}
	
	/**
	 * Get all user
	 * @param id
	 * @return
	 */
	@GetMapping("")
	public List<Users> getAll() {
		List<Users> userData = userDao.findAll();
		if(!userData.isEmpty()) {
			return userData;
		}
		throw new NotFoundException("No User data exist");
	}
	
	/**
	 * Get user by id
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Optional<Users> getOne(@PathVariable("id") int id) {
		Optional<Users> userData = userDao.findById(id);
		if(userData.isPresent()) {
			return userData;
		}
		throw new NotFoundException("User data does not exist with id '"+ id +"'");
	}
	
	/**
	 * Create user user.
	 * @param userReq
	 * @return
	 */
	@PostMapping("")
	public Users save(@RequestBody Users userReq) {
		boolean eixts = userDao.existsByEmail(userReq.getEmail());
		if (!eixts) {
			userReq.setCreated_on(new Date());
			return userService.save(userReq);
		}
		throw new AlreadyExistException("User already exist with email '"+userReq.getEmail() +"'");
	}
	

	/**
	 * Update User
	 * @param Users
	 * @return
	 */
	@PutMapping("/{id}")
	public Users udpate(@RequestBody Users user) {
		boolean eixts = userDao.existsById(user.getUserId());
		if (eixts) {
			return userService.save(user);
		}
		throw new NotFoundException("User does not exist with id '"+ user.getUserId() +"'");
	}

	/**
	 * Delete one user by id
	 * @param userId
	 * @return 
	 */
	@DeleteMapping("/{id}")
	public ResponseDto deleteOne(@PathVariable("id") int id) {
		boolean eixts = userDao.existsById(id);
		if (eixts) {
			userDao.deleteById(id);
			return new ResponseDto("Success","User deleted", new Date(), null);
		}
		throw new NotFoundException("User does not exist with id '"+ id +"'");
	}

}
