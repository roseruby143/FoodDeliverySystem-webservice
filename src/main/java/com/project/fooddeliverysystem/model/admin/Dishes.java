package com.project.fooddeliverysystem.model.admin;


import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;

@Entity
@Table(name = "dishes")
public class Dishes {
	
	@TableGenerator(name="customIncrementer", allocationSize = 1, initialValue = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "customIncrementer")
	private int id;
	
	@Column(name="dish_name", nullable = false)
	private String dish_name;
	
	@Column(name="price", nullable = false)
	private double price;
	
	@Column(name="description")
	private String description;
	
	@Column(name="category")
	private String category;
	
	@Column(name="dish_image_url")
	private String dishImageUrl;
	
	@Column(name="added_on")
	private Date added_on;
	
	@ManyToOne(optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Restaurant restaurant;

	public Dishes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Dishes(int dishId, String dish_name, double price, String description, String dishImageUrl, Date added_on,
			int restaurantId) {
		super();
		this.id = dishId;
		this.dish_name = dish_name;
		this.price = price;
		this.description = description;
		this.dishImageUrl = dishImageUrl;
		this.added_on = added_on;
		this.restaurant = new Restaurant(restaurantId);
	}

	public Dishes(int dishId) {
		super();
		this.id = dishId;
	}

	public int getDishId() {
		return id;
	}

	public void setDishId(int dishId) {
		this.id = dishId;
	}

	public String getDish_name() {
		return dish_name;
	}

	public void setDish_name(String dish_name) {
		this.dish_name = dish_name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDishImageUrl() {
		return dishImageUrl;
	}

	public void setDishImageUrl(String dishImageUrl) {
		this.dishImageUrl = dishImageUrl;
	}

	public Date getAdded_on() {
		return added_on;
	}

	public void setAdded_on(Date added_on) {
		this.added_on = added_on;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@Override
	public String toString() {
		return "MenuList [dishId=" + id + ", dish_name=" + dish_name + ", price=" + price + ", description="
				+ description + ", dishImageUrl=" + dishImageUrl + ", added_on=" + added_on + ", restaurant="
				+ restaurant + "]";
	}

}
