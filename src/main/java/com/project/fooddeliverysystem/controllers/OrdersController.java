package com.project.fooddeliverysystem.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.fooddeliverysystem.dao.admin.DeliveryDAO;
import com.project.fooddeliverysystem.dao.admin.OrdersDAO;
import com.project.fooddeliverysystem.dao.user.UserDAO;
import com.project.fooddeliverysystem.dto.ResponseDto;
import com.project.fooddeliverysystem.exceptions.NotFoundException;
import com.project.fooddeliverysystem.model.admin.Delivery;
import com.project.fooddeliverysystem.model.admin.Orders;

@RestController
@RequestMapping("/v1")
public class OrdersController {
	
	@Autowired
	private OrdersDAO ordersDao;
	
	@Autowired 
	private UserDAO userDao;
	
	@Autowired
	private DeliveryDAO deliveryDao;
	
	/**
	 * Get all Order
	 * @param
	 * @return
	 */
	@GetMapping("/orders")
	public List<Orders> getAllOrders() {
		List<Orders> restData = ordersDao.findAll();
		if(!restData.isEmpty()) {
			return restData;
		}
		throw new NotFoundException("Orders does not exist");
	}
	
	/**
	 * Get Orders by id
	 * @param id
	 * @return
	 */
	@GetMapping("/orders/{id}")
	public Optional<Orders> getOneOrderById(@PathVariable("id") int id) {
		Optional<Orders> resData = ordersDao.findById(id);
		if(resData.isPresent()) {
			return resData;
		}
		throw new NotFoundException("Orders does not exist with id '"+ id +"'");
	}
	
	/**
	 * Get all Orders By User Id
	 * @param
	 * @return
	 */
	@GetMapping("/user/{id}/orders")
	public List<Orders> getAllByUserId(@PathVariable("id") int id) {
		if(userDao.existsById(id)) {
			List<Orders> restData = ordersDao.findByUserId(id);
			if(!restData.isEmpty()) {
				return restData;
			}
			throw new NotFoundException("Orders data does not exist");
		}
		throw new NotFoundException("User does not exist");
	}
	
	/**
	 * Create Orders
	 * @param ordersReq
	 * @return
	 */
	@PostMapping("/user/{id}/orders")
	public Orders saveOrder(@RequestBody Orders ordersReq, @PathVariable("id") int id) {
		boolean exists = userDao.existsById(id);
		if (exists) {
			ordersReq.setOrderDate(new Date());
			return ordersDao.save(ordersReq);
		}
		throw new NotFoundException("User with id '"+id +"' doesnot exist");
	}
	

	/**
	 * Update Orders
	 * @param id
	 * @return
	 */
	@PutMapping("/orders/{id}")
	public Orders udpateOrder(@RequestBody Orders orderData) {
		boolean exists = ordersDao.existsById(orderData.getOrderId());
		if (exists) {
			//System.out.println("******************* orderData : "+orderData.toString());
			return ordersDao.save(orderData);
		}
		throw new NotFoundException("Orders does not exist with id '"+ orderData.getOrderId() +"'");
	}

	/**
	 * Delete one Orders by id
	 * @param Ordersid
	 * @return 
	 */
	@DeleteMapping("/orders/{id}")
	public ResponseDto deleteOneOrder(@PathVariable("id") int id) {
		boolean exists = ordersDao.existsById(id);
		if (exists) {
			ordersDao.deleteById(id);
			return new ResponseDto("Success","Orders deleted", new Date(), null);
		}
		throw new NotFoundException("Orders does not exist with id '"+ id +"'");
	}
	
	/**
	 * Delete all Orders by User id
	 * @param id
	 * @return 
	 */
	@DeleteMapping("/user/{id}/orders")
	public ResponseDto deleteAllOrdersByUserId(@PathVariable("id") int id) {
		//boolean exists = dishesDao.existsById(id);
		if (userDao.existsById(id)) {
			List<Orders> orderList = ordersDao.findByUserId(id);
			ordersDao.deleteByUserId(id);
			//System.out.println("*************** orderList: "+orderList.toString());
			orderList.forEach(list -> {
				Delivery delData = list.getDelivery();
				//System.out.println("*************** delData: "+delData.toString());
				if(delData != null) {
					//System.out.println("*************** delData to be deleted for - "+delData.getDeliveryId());
					
					deliveryDao.deleteById(delData.getDeliveryId());
				}
			});
			return new ResponseDto("Success","All Orders deleted for User id "+id, new Date(), null);
		}
		throw new NotFoundException("User with id '"+ id +"' dosenot exist.");
	}
	
	
	/*************** Order -> Delivery Controllers ***************/
	
	/**
	 * Get all Deliverys By Order Id
	 * @param
	 * @return
	 */
	@GetMapping("/orders/{id}/delivery")
	public Optional<Delivery> getAllByOrderId(@PathVariable("id") int id) {
		if(ordersDao.existsById(id)) {
			Optional<Orders> orderDetail = ordersDao.findById(id);
			if(orderDetail.get().getDelivery() != null) {
				return deliveryDao.findById(orderDetail.get().getDelivery().getDeliveryId());
			}
			throw new NotFoundException("Delivery for order with id '"+ id +"' dosenot exist.");
		}
		throw new NotFoundException("Order does not exist");
	}

	/**
	 * Delete all Deliveries by order id
	 * @param restaurantId
	 * @return 
	 */
	@DeleteMapping("/orders/{id}/delivery")
	public ResponseDto deleteAllDeliveryByOrderId(@PathVariable("id") int id) {
		//boolean exists = dishesDao.existsById(id);
		if (ordersDao.existsById(id)) {
			Optional<Orders> orderDetail = ordersDao.findById(id);
			if(orderDetail.get().getDelivery()!=null) {
				deliveryDao.deleteById(orderDetail.get().getDelivery().getDeliveryId());
				return new ResponseDto("Success","All Delivery deleted for order id "+id, new Date(), null);
			}
			throw new NotFoundException("Delivery for order with id '"+ id +"' dosenot exist.");
		}
		throw new NotFoundException("Order with id '"+ id +"' dosenot exist.");
	}
	

}
