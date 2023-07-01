package com.project.fooddeliverysystem.model.admin;

import java.util.Date;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;

@Entity
@Table(name = "admins")
public class Admins {
	
	@TableGenerator(name="customIncrementer", allocationSize = 1, initialValue = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "customIncrementer")
	@Column(name="adminId", nullable = false)
	private int adminId;
	
	@Column(name="email", nullable = false)
	private String email;
	
	@Column(name="password", nullable = false)
	private String password;
	
	@Column(name="fullName", nullable = false)
	private String fullName;
	
	@Column(name="loginType", nullable = false)
	private int loginType;
	
	@Column(name="status", nullable = false)
	private String status;
	
	@Column(name="addedOn")
	private Date addedOn;

	public Admins() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Admins(int adminId, String email, String password, String fullName, int loginType, Date addedOn, String status) {
		super();
		this.adminId = adminId;
		this.email = email;
		this.password = password;
		this.fullName = fullName;
		this.loginType = loginType;
		this.addedOn = addedOn;
		this.status = status;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getLoginType() {
		return loginType;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}

	public Date getAddedOn() {
		return addedOn;
	}

	public void setAddedOn(Date addedOn) {
		this.addedOn = addedOn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Admins [adminId=" + adminId + ", email=" + email + ", password=" + password + ", fullName=" + fullName
				+ ", loginType=" + loginType + ", status=" + status + ", addedOn=" + addedOn + "]";
	}
}
