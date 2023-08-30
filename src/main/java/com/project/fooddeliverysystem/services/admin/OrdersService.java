package com.project.fooddeliverysystem.services.admin;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.fooddeliverysystem.model.admin.Delivery;
import com.project.fooddeliverysystem.model.admin.OrderItems;
import com.project.fooddeliverysystem.model.admin.Orders;

@Service
public interface OrdersService {

	Delivery createDeliveryForOrder(Orders o);
	
	int assignDriver();
	
	List<OrderItems> saveAllOrderItems(OrderItems[] orderItemList);
}
