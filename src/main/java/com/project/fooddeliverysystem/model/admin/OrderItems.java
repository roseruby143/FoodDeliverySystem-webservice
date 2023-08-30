package com.project.fooddeliverysystem.model.admin;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;

@Entity
@Table(name="order_items")
public class OrderItems {
	
	@TableGenerator(name="customIncrementer", allocationSize = 1, initialValue = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "customIncrementer")
	private int id;
	
	@Column(name="quantity", nullable = false)
	private int quantity;
	
	@Column(name="item_total_price", nullable = false)
	private double itemTotalPrice;
	
	@Column(name="instruction")
	private String instruction;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Orders order;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Dishes dish;

	public OrderItems() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderItems(int orderItemId, int quantity, double itemTotalPrice, String instruction, int orderId, int menulistId) {
		super();
		this.id = orderItemId;
		this.quantity = quantity;
		this.itemTotalPrice = itemTotalPrice;
		this.instruction = instruction;
		this.order = new Orders(orderId);
		this.dish = new Dishes(menulistId);
	}

	public int getOrderItemId() {
		return id;
	}

	public void setOrderItemId(int orderItemId) {
		this.id = orderItemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getItemTotalPrice() {
		return itemTotalPrice;
	}

	public void setItemTotalPrice(double itemTotalPrice) {
		this.itemTotalPrice = itemTotalPrice;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public Orders getOrder() {
		return order;
	}

	public void setOrder(Orders order) {
		this.order = order;
	}

	public Dishes getDish() {
		return dish;
	}

	public void setDish(Dishes menulist) {
		this.dish = menulist;
	}

	@Override
	public String toString() {
		return "OrderItems [id=" + id + ", quantity=" + quantity + ", itemTotalPrice=" + itemTotalPrice
				+ ", instruction=" + instruction + ", order=" + order + ", dish=" + dish + "]";
	}
	

}
