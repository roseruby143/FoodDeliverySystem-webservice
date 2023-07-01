package com.project.fooddeliverysystem.model.admin;

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
@Table(name="order_items")
public class OrderItems {
	
	@TableGenerator(name="customIncrementer", allocationSize = 1, initialValue = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "customIncrementer")
	private int id;
	
	@Column(name="quantity", nullable = false)
	private String quantity;
	
	@Column(name="item_total_price", nullable = false)
	private String itemTotalPrice;
	
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

	public OrderItems(int orderItemId, String quantity, String itemTotalPrice, int orderId, int menulistId) {
		super();
		this.id = orderItemId;
		this.quantity = quantity;
		this.itemTotalPrice = itemTotalPrice;
		this.order = new Orders(orderId);
		this.dish = new Dishes(menulistId);
	}

	public int getOrderItemId() {
		return id;
	}

	public void setOrderItemId(int orderItemId) {
		this.id = orderItemId;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getItemTotalPrice() {
		return itemTotalPrice;
	}

	public void setItemTotalPrice(String itemTotalPrice) {
		this.itemTotalPrice = itemTotalPrice;
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

	public void setMenulist(Dishes menulist) {
		this.dish = menulist;
	}

	@Override
	public String toString() {
		return "OrderItems [orderItemId=" + id + ", quantity=" + quantity + ", itemTotalPrice="
				+ itemTotalPrice + ", order=" + order + ", dishes=" + dish + "]";
	}
	
	

}
