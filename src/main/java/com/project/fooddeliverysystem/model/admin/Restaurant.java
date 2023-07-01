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
@Table(name = "restaurant")
public class Restaurant {
	
	@TableGenerator(name="customIncrementer", allocationSize = 1, initialValue = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "customIncrementer")
	private int id;
	
	@Column(name="name", nullable = false)
	private String name;
	
	@Column(name="description")
	private String description;
	
	@Column(name="address", nullable = false)
	private String address;
	
	@Column(name="phone", nullable = false)
	private String phone;
	
	@Column(name="email", nullable = false)
	private String email;
	
	@Column(name="contact_person", nullable = false)
	private String contact_person;
	
	@Column(name="rating")
	private double rating;
	
	@Column(name="restaurant_image_url")
	private String restaurantImageUrl;
	
	@Column(name="added_on")
	private Date added_on;

	public Restaurant() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Restaurant(int restaurantId, String name, String address, String phone, String email, String contact_person,
			double rating, String restaurantImageUrl, Date added_on) {
		super();
		this.id = restaurantId;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.email = email;
		this.contact_person = contact_person;
		this.rating = rating;
		this.restaurantImageUrl = restaurantImageUrl;
		this.added_on = added_on;
	}

	public Restaurant(int restaurantId) {
		super();
		this.id = restaurantId;
	}

	public int getRestaurantId() {
		return id;
	}

	public void setRestaurantId(int restaurantId) {
		this.id = restaurantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContact_person() {
		return contact_person;
	}

	public void setContact_person(String contact_person) {
		this.contact_person = contact_person;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getRestaurantImageUrl() {
		return restaurantImageUrl;
	}

	public void setRestaurantImageUrl(String restaurantImageUrl) {
		this.restaurantImageUrl = restaurantImageUrl;
	}

	public Date getAdded_on() {
		return added_on;
	}

	public void setAdded_on(Date added_on) {
		this.added_on = added_on;
	}

	@Override
	public String toString() {
		return "Restaurant [restaurantId=" + id + ", name=" + name + ", address=" + address + ", phone="
				+ phone + ", email=" + email + ", contact_person=" + contact_person + ", rating=" + rating
				+ ", restaurantImageUrl=" + restaurantImageUrl + ", added_on=" + added_on + "]";
	}
	

}
