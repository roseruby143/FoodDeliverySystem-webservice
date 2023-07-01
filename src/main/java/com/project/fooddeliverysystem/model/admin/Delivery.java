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
@Table(name="delivery")
public class Delivery {
	
	@TableGenerator(name="customIncrementer", allocationSize = 1, initialValue = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "customIncrementer")
	private int id;
	//private int orderId;
	
	/*
	 * 	
	 * 	1 - Driver assigned
	 * 	2 - Order Picked up
	 * 	3 - Delivered
	 * 	4 - Delivery Attempt Failed
	 * */
	@Column(name="delivery_status", nullable = false)
	private int deliveryStatus;
	
	@Column(name="delivery_title")
	private String deliveryTitle;
	
	@Column(name="delivery_date")
	private Date deliveryDate;
	//private Time deliveryTime;
	
	@Column(name="delivery_instruction")
	private String deliveryInstruction;
	//private String shipmentCompany;
	
	/*
	 * @OneToOne
	 * 
	 * @OnDelete(action = OnDeleteAction.CASCADE) private Orders order;
	 */
	
	/*
	 * Many Delivery can have same driver
	 */
	@ManyToOne(optional = false)
	@OnDelete(action = OnDeleteAction.SET_DEFAULT)
	private Drivers driver;
	
	public Delivery() {
		super();
	}

	public Delivery(int deliveryId, int deliveryStatus, String deliveryTitle, Date deliveryDate,
			String deliveryInstruction, /* int orderId, */ int driverId) {
		super();
		this.id = deliveryId;
		this.deliveryStatus = deliveryStatus;
		this.deliveryTitle = deliveryTitle;
		this.deliveryDate = deliveryDate;
		this.deliveryInstruction = deliveryInstruction;
		/* this.order = new Orders(orderId); */
		this.driver = new Drivers(driverId);
	}

	public Delivery(int deliveryId) {
		super();
		this.id = deliveryId;
	}

	public int getDeliveryId() {
		return id;
	}

	public void setDeliveryId(int deliveryId) {
		this.id = deliveryId;
	}

	public int getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(int deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public String getDeliveryTitle() {
		return deliveryTitle;
	}

	public void setDeliveryTitle(String deliveryTitle) {
		this.deliveryTitle = deliveryTitle;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryInstruction() {
		return deliveryInstruction;
	}

	public void setDeliveryInstruction(String deliveryInstruction) {
		this.deliveryInstruction = deliveryInstruction;
	}

	
	/*
	 * public Orders getOrder() { return order; }
	 * 
	 * public void setOrder(Orders order) { this.order = order; }
	 */
 


	public Drivers getDriver() { return driver; }
  
	public void setDriver(Drivers driver) { this.driver = driver; }
	 

	@Override
	public String toString() {
		return "Delivery [deliveryId=" + id + ", deliveryStatus=" + deliveryStatus + ", deliveryTitle="
				+ deliveryTitle + ", deliveryDate=" + deliveryDate + ", deliveryInstruction=" + deliveryInstruction //+"]";
				+ /* ", order=" + order + */ ", driver=" + driver + "]";
	}
}
