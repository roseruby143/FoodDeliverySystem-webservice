package com.project.fooddeliverysystem.services.admin;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.fooddeliverysystem.dto.LoginReqDto;
import com.project.fooddeliverysystem.model.admin.Admins;

@Service
public interface AdminsService {

	Page<Admins> findByEmailContaining(String email, Pageable pageable);

	Page<Admins> findAll(Pageable pageable);

	Optional<Admins> findById(int id);

	boolean existsByEmail(String email);

	Admins save(Admins adminsReq);

	boolean existsById(int adminId);

	void deleteById(int id);

	boolean login(LoginReqDto loginReqDto);
	
	Admins findByEmail(String email);
}
