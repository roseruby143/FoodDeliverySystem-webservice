package com.project.fooddeliverysystem.services.user;

import org.springframework.stereotype.Service;

import com.project.fooddeliverysystem.dto.LoginReqDto;
import com.project.fooddeliverysystem.model.user.Users;

@Service
public interface UserService {
	
	boolean login(LoginReqDto loginReqDto);
	
	Users save(Users adminsReq);
	
	Users findByEmail(String email);

}
