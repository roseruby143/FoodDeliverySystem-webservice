package com.project.fooddeliverysystem.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.fooddeliverysystem.dao.user.UserDAO;
import com.project.fooddeliverysystem.dto.LoginReqDto;
import com.project.fooddeliverysystem.model.user.Users;

@Service
public class UserServiceImp implements UserService{
	
	@Autowired
	UserDAO userDao;
	
	BCryptPasswordEncoder passwordEncoder;

	@Override
	public boolean login(LoginReqDto loginReqDto) {
		passwordEncoder = new BCryptPasswordEncoder();
		Users user  = userDao.findByEmail(loginReqDto.getEmail());
		return passwordEncoder.matches(loginReqDto.getPassword(), user.getPassword());
	}

	@Override
	public Users save(Users userReq) {
		passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(userReq.getPassword());
		userReq.setPassword(encodedPassword);
		return userDao.save(userReq);
	}

}
