package com.project.fooddeliverysystem.model.user;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.project.fooddeliverysystem.model.admin.Dishes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;

@Entity
@Table(name = "cart_items")
public class CartItems {

	@TableGenerator(name="customIncrementer", allocationSize = 1, initialValue = 0)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "customIncrementer")
	private int id;
	
	@Column(nullable = false)
	private int quantity;
	
	@Column(name="instruction")
	private String instruction;
	
	@ManyToOne(optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Users user;
	
	@ManyToOne(optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Dishes dishes;

	public CartItems() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CartItems(int id, int quantity, String instruction, int userId, int dishesId) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.instruction = instruction;
		this.user = new Users(userId);
		this.dishes = new Dishes(dishesId);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public Users getUsers() {
		return user;
	}

	public void setUsers(Users users) {
		this.user = users;
	}

	public Dishes getDishes() {
		return dishes;
	}

	public void setDishes(Dishes dishes) {
		this.dishes = dishes;
	}

	@Override
	public String toString() {
		return "CartItems [id=" + id + ", quantity=" + quantity + ", instruction=" + instruction + ", user=" + user
				+ ", dishes=" + dishes + "]";
	}
}
