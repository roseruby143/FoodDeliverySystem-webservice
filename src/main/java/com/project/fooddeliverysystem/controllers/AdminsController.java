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

import com.project.fooddeliverysystem.dao.admin.AdminDAO;
import com.project.fooddeliverysystem.dto.LoginReqDto;
import com.project.fooddeliverysystem.dto.ResponseDto;
import com.project.fooddeliverysystem.exceptions.AlreadyExistException;
import com.project.fooddeliverysystem.exceptions.NotFoundException;
import com.project.fooddeliverysystem.exceptions.UnauthorizedUserException;
import com.project.fooddeliverysystem.model.admin.Admins;
import com.project.fooddeliverysystem.security.SecurityService;
import com.project.fooddeliverysystem.services.admin.AdminsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = {
		"http://ec2-54-82-234-235.compute-1.amazonaws.com:4200","ec2-54-82-234-235.compute-1.amazonaws.com:4100"}, allowCredentials = "true")
@RestController
@RequestMapping("/v1/admin")
public class AdminsController {
	
	@Autowired private AdminDAO adminDao;
	@Autowired private AdminsService adminService;
	@Autowired private SecurityService securityService;
	
	/**
	 * Validate Login for admin user.
	 * @param adminsReq
	 * @return
	 */
	@PostMapping("/login")
	public Admins login(@RequestBody LoginReqDto loginReqDto, HttpServletRequest request) {
		boolean eixts = adminService.existsByEmail(loginReqDto.getEmail());
		if (eixts) {
			boolean match = adminService.login(loginReqDto);
			if(match) {
				
				Admins aData = adminService.findByEmail(loginReqDto.getEmail());
				
				// check for user status
				if(!aData.getStatus().equalsIgnoreCase("active")) {
					throw new NotFoundException("Admin not active.");
				}
				
				//String userIdentifier = "lkdsfjlksdjfldjlfk"; 
				HttpSession session = request.getSession();
				session.setMaxInactiveInterval(3600);
				session.setAttribute("userIdentifier", aData.getAdminId()+"");
				session.setAttribute("userEmail", aData.getEmail());
				session.setAttribute("userType", "admin");
				System.out.println("Session ID from /admin/login = "+ session.getId());
				
				
				//System.out.println("********* Loggedin user data: "+ adminService.findByEmail(loginReqDto.getEmail()).toString());
				return aData;
						//new ResponseDto("Success","Admin login successfull", new Date(), loginReqDto.getEmail()); 
			}else {
				throw new NotFoundException("Email and password doesnot match");
			}
		}
		throw new NotFoundException("Invalid email. Please try again");
	}
	
	/**
	 * Logout for admin.
	 * @param 
	 * @return
	 */
	@PostMapping("/{id}/logout")
	public ResponseDto logout(@PathVariable("id") int id, HttpServletRequest request) {
		if(securityService.isloggedInAdmin(id+"",request)) {
			HttpSession session = request.getSession(false); // Get the HttpSession without creating a new one
			if (session != null) {
			    session.invalidate(); // Invalidate the session
			}
			return new ResponseDto("Success","Session Invalidated.", new Date(), null);
		}
		throw new UnauthorizedUserException("Action not authorized for this admin", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Get all admin user
	 * @param 
	 * @return
	 */
	@GetMapping("")
	public List<Admins> getAll(HttpServletRequest request) {
		System.out.println("Inside getAll ");
		if(securityService.isAdmin(request)) {
			List<Admins> adminData = adminDao.findAll();
			if(!adminData.isEmpty()) {
				return adminData;
			}
			throw new NotFoundException("No Admins data exist");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Get admin user by id
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Optional<Admins> getOne(@PathVariable("id") int id, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			Optional<Admins> adminData = adminService.findById(id);
			if(adminData.isPresent()) {
				return adminData;
			}
			throw new NotFoundException("Admins data does exist with id '"+ id +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Create admin user.
	 * @param adminsReq
	 * @return
	 */
	@PostMapping("")
	public Admins save(@RequestBody() Admins adminsReq, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			boolean eixts = adminService.existsByEmail(adminsReq.getEmail());
			if (!eixts) {
				adminsReq.setAddedOn(new Date());
				return adminService.save(adminsReq);
			}
			throw new AlreadyExistException("Admin user already exist with email '"+adminsReq.getEmail() +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	

	/**
	 * Update Admins
	 * @param Admins
	 * @return
	 */
	@PutMapping("/{id}")
	public Admins udpate(@RequestBody Admins admins, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			int id = admins.getAdminId();
			boolean eixts = adminService.existsById(id);
			if (eixts) {
				Admins adm = adminDao.findById(id).get();
				if(adm.getPassword().equals(admins.getPassword()))
					return adminDao.save(admins);
				else
					return adminService.save(admins);
			}
			throw new NotFoundException("Admin user does exist with id '"+ admins.getAdminId() +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}

	/**
	 * Delete one product by id
	 * @param productId
	 * @return 
	 */
	@DeleteMapping("/{id}")
	public ResponseDto deleteOne(@PathVariable("id") int id, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			boolean eixts = adminService.existsById(id);
			if (eixts) {
				adminService.deleteById(id);
				return new ResponseDto("Success","Admin user deleted", new Date(), null);
			}
			throw new NotFoundException("Admin user does exist with id '"+ id +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}

}
