package com.project.fooddeliverysystem.services.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.fooddeliverysystem.dao.admin.AdminDAO;
import com.project.fooddeliverysystem.dto.LoginReqDto;
import com.project.fooddeliverysystem.model.admin.Admins;

@Service
public class AdminsServiceImp implements AdminsService{
	
	@Autowired
	AdminDAO adminsRepository;
	
	BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public Page<Admins> findByEmailContaining(String email,  Pageable pageable) {
		return adminsRepository.findByEmailContaining(email, pageable);
	}

	@Override
	public Page<Admins> findAll(Pageable pageable) {
		return adminsRepository.findAll(pageable);
	}

	@Override
	public Optional<Admins> findById(int id) {
		return adminsRepository.findById(id);
	}

	@Override
	public boolean existsByEmail(String email) {
		return adminsRepository.existsByEmail(email);
	}

	@Override
	public Admins save(Admins adminsReq) {
		passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(adminsReq.getPassword());
		adminsReq.setPassword(encodedPassword);
		return adminsRepository.save(adminsReq);
	}

	@Override
	public boolean existsById(int adminId) {
		return adminsRepository.existsById(adminId);
	}

	@Override
	public void deleteById(int id) {
		adminsRepository.deleteById(id);
	}

	@Override
	public boolean login(LoginReqDto loginReqDto) {
		passwordEncoder = new BCryptPasswordEncoder();
		Admins admins  = adminsRepository.findByEmail(loginReqDto.getEmail());
		return passwordEncoder.matches(loginReqDto.getPassword(), admins.getPassword());
	}}
