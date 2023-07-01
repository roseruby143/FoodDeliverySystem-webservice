package com.project.fooddeliverysystem.model.user;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.project.fooddeliverysystem.model.admin.Dishes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;

@Entity
@Table(name = "favorites")
public class Favorites {
	
	@TableGenerator(name="customIncrementer", allocationSize = 1, initialValue = 0)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "customIncrementer")
	private int id;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Dishes dishes;
	
	@ManyToOne(optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Users user;

	public Favorites() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Favorites(int id, int dishId, int userId) {
		super();
		this.id = id;
		this.dishes = new Dishes(dishId);
		this.user = new Users(userId);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Dishes getDishes() {
		return dishes;
	}

	public void setDishes(Dishes dishes) {
		this.dishes = dishes;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Favorites [id=" + id + ", dishes=" + dishes + ", user=" + user + "]";
	}

}
