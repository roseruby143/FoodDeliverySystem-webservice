package com.project.fooddeliverysystem.dao.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fooddeliverysystem.model.user.Address;

import jakarta.transaction.Transactional;

public interface AddressDAO extends JpaRepository<Address, Integer>  {

	List<Address> findByUsersId(int userId);
	
	@Transactional
	void deleteByUsersId(int userId);
	
	
}
