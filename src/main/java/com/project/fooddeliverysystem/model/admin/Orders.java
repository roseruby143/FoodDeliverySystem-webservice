package com.project.fooddeliverysystem.model.admin;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.project.fooddeliverysystem.model.user.Address;
import com.project.fooddeliverysystem.model.user.Users;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;

@Entity
@Table(name="orders")
public class Orders {

	// order details properties
	@TableGenerator(name="customIncrementer", allocationSize = 1, initialValue = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "customIncrementer")
	private int id;
		
		@Column(name="order_date", nullable = false)
		private Date orderDate;
		
		/*
		 * 	1 - Order Created
		 * 	2 - Order Accepted by Restaurant
		 * 	3 - Order Ready for pickup
		 * 	4 - Ordered Delivered
		 * 	5 - Order Cancelled
		 * */
		@Column(name="order_status", nullable = false)
		private String orderStatus;

		// pricing properties
		@Column(name="total_items", nullable = false)
		private int totalItems;
		
		@Column(name="subtotal", nullable = false)
		private double itemsSubTotal;
		
		@Column(name="tax_n_fees", nullable = false)
		private double taxNFees;
		
		@Column(name="delivery_charges", nullable = false)
		private double deliveryCharges;
		
		
		@Column(name="driver_tip", nullable = false)
		private double driverTip;
		
		@Column(name="total_amount", nullable = false)
		private double totalAmount;
		
		@Column(name="payment_status", nullable = false)
		private int paymentStatus;

		// payment status properties
		@Column(name="payment_status_title")
		private String paymentStatusTitle;
		
		@Column(name="paymen_method", nullable = false)
		private int paymentMethod;
		
		@Column(name="payment_method_title")
		private String paymentMethodTitle;
		
		@Column(name="instruction")
		private String instruction;
		
		/************** CHECK THIS ONE **************/
		/*
		 * @Column(name="creator_admin_id") private String creatorAdmin;
		 */
		
		@ManyToOne()
		@OnDelete(action = OnDeleteAction.SET_DEFAULT)
		private Address address;

		// customer / user properties
		@ManyToOne(optional = false)
		@OnDelete(action = OnDeleteAction.CASCADE)
		private Users user;
		
		// customer / user properties
		@OneToOne
		@OnDelete(action = OnDeleteAction.SET_DEFAULT)
		private Delivery delivery;

		public Orders() {
			super();
		}

		public Orders(int orderId, Date orderDate, String orderStatus, int totalItems, double itemsSubTotal,
				double taxCharges, double deliveryCharges, double driverTip, double totalAmount, int paymentStatus,
				String paymentStatusTitle, int paymentMethod, String paymentMethodTitle,String instruction, int addressId, int userId, int deliveryId) {
			super();
			this.id = orderId;
			this.orderDate = orderDate;
			this.orderStatus = orderStatus;
			this.totalItems = totalItems;
			this.itemsSubTotal = itemsSubTotal;
			this.taxNFees = taxCharges;
			this.deliveryCharges = deliveryCharges;
			this.driverTip = driverTip;
			this.totalAmount = totalAmount;
			this.paymentStatus = paymentStatus;
			this.paymentStatusTitle = paymentStatusTitle;
			this.paymentMethod = paymentMethod;
			this.paymentMethodTitle = paymentMethodTitle;
			this.address = new Address(addressId);
			this.user = new Users(userId);
			this.delivery = new Delivery(deliveryId);
			this.instruction = instruction;
		}

		public Orders(int orderId) {
			super();
			this.id = orderId;
		}

		public int getOrderId() {
			return id;
		}

		public void setOrderId(int orderId) {
			this.id = orderId;
		}

		public Date getOrderDate() {
			return orderDate;
		}

		public void setOrderDate(Date orderDate) {
			this.orderDate = orderDate;
		}

		public String getOrderStatus() {
			return orderStatus;
		}

		public void setOrderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
		}

		public int getTotalItems() {
			return totalItems;
		}

		public void setTotalItems(int totalItems) {
			this.totalItems = totalItems;
		}

		public double getItemsSubTotal() {
			return itemsSubTotal;
		}

		public void setItemsSubTotal(double itemsSubTotal) {
			this.itemsSubTotal = itemsSubTotal;
		}

		public double getTaxNFees() {
			return taxNFees;
		}

		public void setTaxNFees(double taxCharges) {
			this.taxNFees = taxCharges;
		}

		public double getDeliveryCharges() {
			return deliveryCharges;
		}

		public void setDeliveryCharges(double deliveryCharges) {
			this.deliveryCharges = deliveryCharges;
		}

		public double getDriverTip() {
			return driverTip;
		}

		public void setDriverTip(double driverTip) {
			this.driverTip = driverTip;
		}

		public double getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(double totalAmount) {
			this.totalAmount = totalAmount;
		}

		public int getPaymentStatus() {
			return paymentStatus;
		}

		public void setPaymentStatus(int paymentStatus) {
			this.paymentStatus = paymentStatus;
		}

		public String getPaymentStatusTitle() {
			return paymentStatusTitle;
		}

		public void setPaymentStatusTitle(String paymentStatusTitle) {
			this.paymentStatusTitle = paymentStatusTitle;
		}

		public int getPaymentMethod() {
			return paymentMethod;
		}

		public void setPaymentMethod(int paymentMethod) {
			this.paymentMethod = paymentMethod;
		}

		public String getPaymentMethodTitle() {
			return paymentMethodTitle;
		}

		public void setPaymentMethodTitle(String paymentMethodTitle) {
			this.paymentMethodTitle = paymentMethodTitle;
		}

		public String getInstruction() {
			return instruction;
		}

		public void setInstruction(String instruction) {
			this.instruction = instruction;
		}

		public Address getAddress() {
			return address;
		}

		public void setAddress(Address address) {
			this.address = address;
		}

		public Users getUser() {
			return user;
		}

		public void setUser(Users user) {
			this.user = user;
		}

		
		public Delivery getDelivery() { 
			return delivery; 
		}
	  
		public void setDelivery(Delivery delivery) { 
			this.delivery = delivery; 
		}

		@Override
		public String toString() {
			return "Orders [id=" + id + ", orderDate=" + orderDate + ", orderStatus=" + orderStatus + ", totalItems="
					+ totalItems + ", itemsSubTotal=" + itemsSubTotal + ", taxNFees=" + taxNFees + ", deliveryCharges="
					+ deliveryCharges + ", driverTip=" + driverTip + ", totalAmount=" + totalAmount + ", paymentStatus="
					+ paymentStatus + ", paymentStatusTitle=" + paymentStatusTitle + ", paymentMethod=" + paymentMethod
					+ ", paymentMethodTitle=" + paymentMethodTitle + ", instruction=" + instruction + ", address="
					+ address + ", user=" + user + ", delivery=" + delivery + "]";
		}
}
