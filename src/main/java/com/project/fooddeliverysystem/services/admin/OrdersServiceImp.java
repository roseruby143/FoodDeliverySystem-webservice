package com.project.fooddeliverysystem.services.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.fooddeliverysystem.dao.admin.DeliveryDAO;
import com.project.fooddeliverysystem.dao.admin.DriversDAO;
import com.project.fooddeliverysystem.dao.admin.OrderItemsDAO;
import com.project.fooddeliverysystem.model.admin.Delivery;
import com.project.fooddeliverysystem.model.admin.Drivers;
import com.project.fooddeliverysystem.model.admin.OrderItems;
import com.project.fooddeliverysystem.model.admin.Orders;

@Service
public class OrdersServiceImp implements OrdersService {

	@Autowired
	private DriversDAO driverDao;
	
	@Autowired
	private OrderItemsDAO orderItemsDao;
	
	@Override
	public Delivery createDeliveryForOrder(Orders ordersReq) {
		// TODO Auto-generated method stub
		Delivery newDelivery = new Delivery();
		newDelivery.setCreatedOn(new Date());
		newDelivery.setDeliveryStatus(1);
		newDelivery.setDeliveryTitle("Driver Assigned");
		Drivers d = driverDao.findById(assignDriver()).get();
	    System.out.println("*********** random driver Id : "+d);
		newDelivery.setDriver(d);
		newDelivery.setDeliveryInstruction(ordersReq.getInstruction());
	    //System.out.println("*********** random driver Id : "+newDelivery);
		return newDelivery;
	}
	
	@Override
	public int assignDriver(){
		List<Integer> allDriverIds = driverDao.getAllIdsByStatus("active");
		Random rndm = new Random(); 
	     // creating object
	     int rndmElem = allDriverIds.get(rndm.nextInt(allDriverIds.size()));
	     //System.out.println("*********** random driver Id : "+rndmElem);
	     return rndmElem;
	}
	
	@Override
	@Transactional
	public List<OrderItems> saveAllOrderItems(OrderItems[] orderItemList){
		List<OrderItems> list = new ArrayList<OrderItems>();
		for(OrderItems orderItem : orderItemList) {
			list.add(orderItemsDao.save(orderItem));
		}
		return list;
	}
	
	public List<OrderItems> getOrderItemsForUser(int userId) {
        return orderItemsDao.findByUserId(userId);
    }

}
