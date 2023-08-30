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

import com.project.fooddeliverysystem.dao.user.AddressDAO;
import com.project.fooddeliverysystem.dao.user.UserDAO;
import com.project.fooddeliverysystem.dto.LoginReqDto;
import com.project.fooddeliverysystem.dto.ResponseDto;
import com.project.fooddeliverysystem.exceptions.AlreadyExistException;
import com.project.fooddeliverysystem.exceptions.NotFoundException;
import com.project.fooddeliverysystem.exceptions.UnauthorizedUserException;
import com.project.fooddeliverysystem.model.user.Address;
import com.project.fooddeliverysystem.model.user.Users;
import com.project.fooddeliverysystem.security.SecurityService;
import com.project.fooddeliverysystem.services.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = {"http://localhost:4200","http://localhost:4100"}, allowCredentials = "true")
@RestController
@RequestMapping("/v1")
public class UsersController {
	@Autowired private UserDAO userDao;
	@Autowired private UserService userService;
	@Autowired private AddressDAO addressDao;
	@Autowired private SecurityService securityService;
	
	/**
	 * Validate Login for user.
	 * @param userReq
	 * @return
	 */
	@PostMapping("/user/login")
	public Users login(@RequestBody LoginReqDto loginReqDto, HttpServletRequest request) {
		boolean eixts = userDao.existsByEmail(loginReqDto.getEmail());
		if (eixts) {
			boolean match = userService.login(loginReqDto);
			if(match) {
				Users uData = userService.findByEmail(loginReqDto.getEmail());
				
				// check for user status
				if(!uData.getStatus().equalsIgnoreCase("active")) {
					throw new NotFoundException("User not active.");
				}
				
				//String userIdentifier = "lkdsfjlksdjfldjlfk"; 
				HttpSession session = request.getSession();
				session.setMaxInactiveInterval(3600);
				session.setAttribute("userIdentifier", uData.getId()+"");
				session.setAttribute("userEmail", uData.getEmail());
				session.setAttribute("userType", "user");
				System.out.println("Session ID from /user/login = "+ session.getId());
				
				return uData;
				//return new ResponseDto("Success","User login successfull", new Date(), loginReqDto.getEmail()); 
			}else {
				throw new NotFoundException("Invalid password, password mismatch error.");
			}
		}
		throw new NotFoundException("User does not exist with email '"+loginReqDto.getEmail() +"'");
	}
	
	/**
	 * Logout for user.
	 * @param userReq
	 * @return
	 */
	@PostMapping("/user/{id}/logout")
	public ResponseDto logout(@PathVariable("id") int id, HttpServletRequest request) {
		if(securityService.isUser(id+"", request)) {
			HttpSession session = request.getSession(false); // Get the HttpSession without creating a new one
			if (session != null) {
			    session.invalidate(); // Invalidate the session
			}
			return new ResponseDto("Success","Session Invalidated.", new Date(), null);
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Get all user
	 * @param id
	 * @return
	 */
	@GetMapping("/user")
	public List<Users> getAll(HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			List<Users> userData = userDao.findAll();
			if(!userData.isEmpty()) {
				return userData;
			}
			throw new NotFoundException("No User data exist");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Get user by id
	 * @param id
	 * @return
	 */
	@GetMapping("/user/{id}")
	public Optional<Users> getOne(@PathVariable("id") int id, HttpServletRequest request) {
		
		boolean isUserAuthorized = securityService.isActionAllowed(id+"",  request );
		
		if(isUserAuthorized) {
			Optional<Users> userData = userDao.findById(id);
			if(userData.isPresent()) {
				return userData;
			}
			throw new NotFoundException("User data does not exist with id '"+ id +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		
	}
	
	/**
	 * Create user user.
	 * @param userReq
	 * @return
	 */
	@PostMapping("/user/register")
	public Users save(@RequestBody Users userReq, HttpServletRequest httpRequest) {
		
		boolean isUserAuthorized = securityService.isActionAllowed(userReq.getId()+"",  httpRequest );
		
		if(isUserAuthorized) {
			boolean eixts = userDao.existsByEmail(userReq.getEmail());
			if (!eixts) {
				userReq.setCreated_on(new Date());
				return userService.save(userReq);
			}
			throw new AlreadyExistException("User already exist with email '"+userReq.getEmail() +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		
		
	}
	

	/**
	 * Update User
	 * @param Users
	 * @return
	 */
	@PutMapping("/user/{id}")
	public Users udpate(@RequestBody Users user, HttpServletRequest httpRequest) {
		
		boolean isUserAuthorized = securityService.isActionAllowed(user.getId()+"",  httpRequest );
		if(isUserAuthorized) {
			int id = user.getId();
			boolean eixts = userDao.existsById(id);
			if (eixts) {
				Users userData = userDao.findById(id).get();
				if(userData.getPassword().equals(user.getPassword())) {
					return userDao.save(user);
				}
				else
					return userService.save(user);
			}
			throw new NotFoundException("User does not exist with id '"+ user.getId() +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		
		
	}
	
	/**
	 * Get user by id
	 * @param id
	 * @return
	 */
	@GetMapping("/user/{id}/address")
	public List<Address> getAddressByUserId(@PathVariable("id") int id, HttpServletRequest httpRequest) {
		
		boolean isUserAuthorized = securityService.isActionAllowed(id+"",  httpRequest );
		if(isUserAuthorized) {
			boolean eixts = userDao.existsById(id);
			if(eixts) {
				return addressDao.findByUsersId(id);
			}
			throw new NotFoundException("User data does not exist with id '"+ id +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		
	}
	
	/**
	 * Create Address.
	 * @param userReq
	 * @return
	 */
	@PostMapping("/user/{id}/address")
	public Address saveAddress(@RequestBody Address addressData, @PathVariable("id") int userId, HttpServletRequest httpRequest) {
		boolean isUserAuthorized = securityService.isActionAllowed(addressData.getUsers().getId()+"",  httpRequest );
		if(isUserAuthorized) {
			boolean eixts = userDao.existsById(userId);
			if (eixts) {
				//userReq.setCreated_on(new Date());
				return addressDao.save(addressData);
			}
			throw new NotFoundException("User does not exist with id '"+ userId +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		
	}
	

	/**
	 * Update Address
	 * @param Address
	 * @return
	 */
	@PutMapping("address/{id}")
	public Address udpateAddress(@RequestBody Address address, HttpServletRequest httpRequest) {
		boolean isUserAuthorized = securityService.isActionAllowed(address.getUsers().getId()+"",  httpRequest );
		if(isUserAuthorized) {

			boolean eixts = addressDao.existsById(address.getId());
			if (eixts) {
				return addressDao.save(address);
			}
			throw new NotFoundException("Address does not exist with id '"+ address.getId() +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	

	/**
	 * Update Address
	 * @param Address
	 * @return
	 */
	@DeleteMapping("address/{id}")
	public ResponseDto deleteOneAddress(@PathVariable("id") int id, HttpServletRequest httpRequest) {
		int userId = addressDao.findById(id).get().getUsers().getId();
		boolean isUserAuthorized = securityService.isActionAllowed(userId+"",  httpRequest );
		if(isUserAuthorized) {

			boolean exists = addressDao.existsById(id);
			if (exists) {
				addressDao.deleteById(id);
				return new ResponseDto("Success","Address deleted", new Date(), null);
			}
			throw new NotFoundException("Address does not exist with id '"+ id +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		
	}

	/**
	 * Delete one user by id
	 * @param userId
	 * @return 
	 */
	/*
	 * @DeleteMapping("/{id}") public ResponseDto deleteOne(@PathVariable("id") int
	 * id) { boolean eixts = userDao.existsById(id); if (eixts) {
	 * userDao.deleteById(id); return new ResponseDto("Success","User deleted", new
	 * Date(), null); } throw new NotFoundException("User does not exist with id '"+
	 * id +"'"); }
	 */
	
	
	
}
