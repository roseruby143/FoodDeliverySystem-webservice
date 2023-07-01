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
@Table(name="drivers")
public class Drivers {
	
	@TableGenerator(name="customIncrementer", allocationSize = 1, initialValue = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "customIncrementer")
	private int id;
	
	@Column(name="first_name", nullable = false)
	private String first_name;
	
	@Column(name="last_name", nullable = false)
	private String last_name;
	
	@Column(name="email", nullable = false)
	private String email;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="created_on")
	private Date created_on;
	
	@Column(name="vehical_number")
	private String vehicalNumber;
	
	/*
	 * Refers if the driver is Active or Inactive
	 * */
	@Column(name="status")
	private String status;
	
	@Column(name="rating")
	private double rating;
	
	/*
	 * 
	 * @OneToMany(optional=false)
	 * 
	 * @OnDelete(action = OnDeleteAction.SET_NULL) private Delivery delivery;
	 */

	public Drivers() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Drivers(int id, String first_name, String last_name, String email, String phone, Date created_on,
			String vehicalNumber, String status, double rating) {
		super();
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.phone = phone;
		this.created_on = created_on;
		this.vehicalNumber = vehicalNumber;
		this.status = status;
		this.rating = rating;
	}

	/*
	 * public Delivery getDelivery() { return delivery; }
	 * 
	 * public void setDelivery(Delivery delivery) { this.delivery = delivery; }
	 */

	public Drivers(int driverId) {
		super();
		this.id = driverId;
	}

	public int getId() {
		return id;
	}

	public void setId(int driverId) {
		this.id = driverId;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}

	public String getVehicalNumber() {
		return vehicalNumber;
	}

	public void setVehicalNumber(String vehicalNumber) {
		this.vehicalNumber = vehicalNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "Drivers [id=" + id + ", first_name=" + first_name + ", last_name=" + last_name + ", email=" + email
				+ ", phone=" + phone + ", created_on=" + created_on + ", vehicalNumber=" + vehicalNumber + ", status="
				+ status + ", rating=" + rating + "]";
	}

}
