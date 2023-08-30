package com.project.fooddeliverysystem.model.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;

@Entity
@Table(name="users")
public class Users {

	@TableGenerator(name="customIncrementer", allocationSize = 1, initialValue = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "customIncrementer")
	private int id;
	
	@Column(name="first_name", nullable = false)
	private String first_name;
	
	@Column(name="last_name")
	private String last_name;
	
	@Column(name="email", nullable = false)
	private String email;
	
	@Column(name="password", nullable = false)
	private String password;
	
	@OneToMany(mappedBy = "users")
    private List<Address> addresses = new ArrayList<Address>();
	
	/*
	 * Refers if the user is Active or Inactive
	 * */
	@Column(name="status", nullable = false)
	private String status;
	
	@Column(name="imgUrl")
	private String imgUrl;
	
	@Column(name="created_on")
	private Date created_on;

	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Users(int userId) {
		super();
		this.id = userId;
		// TODO Auto-generated constructor stub
	}

	public Users(int id, String first_name, String last_name, String email, String password, 
			String status, String imgUrl, Date created_on) {
		super();
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.password = password;
		this.status = status;
		this.imgUrl = imgUrl;
		this.created_on = created_on;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Date getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", first_name=" + first_name + ", last_name=" + last_name + ", email=" + email
				+ ", password=" + password + ", status=" + status + ", imgUrl=" + imgUrl
				+ ", created_on=" + created_on + "]";
	}
	
}
