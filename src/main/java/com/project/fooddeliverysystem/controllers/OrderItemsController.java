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

import com.project.fooddeliverysystem.dao.admin.OrderItemsDAO;
import com.project.fooddeliverysystem.dao.admin.OrdersDAO;
import com.project.fooddeliverysystem.dto.ResponseDto;
import com.project.fooddeliverysystem.exceptions.NotFoundException;
import com.project.fooddeliverysystem.model.admin.OrderItems;
import com.project.fooddeliverysystem.model.admin.Orders;

@RestController
@RequestMapping("/v1")
public class OrderItemsController {
	
	@Autowired
	private OrderItemsDAO orderitemDao;
	
	@Autowired
	private OrdersDAO orderDao;
	
	/**
	 * Get all OrderItems
	 * @param
	 * @return
	 */
	@GetMapping("/orderitems")
	public List<OrderItems> getAll() {
		List<OrderItems> restData = orderitemDao.findAll();
		if(!restData.isEmpty()) {
			return restData;
		}
		throw new NotFoundException("Order Items data does not exist");
	}
	
	/**
	 * Get OrderItems by id
	 * @param id
	 * @return
	 */
	@GetMapping("/orderitems/{id}")
	public Optional<OrderItems> getOne(@PathVariable("id") int id) {
		Optional<OrderItems> resData = orderitemDao.findById(id);
		if(resData.isPresent()) {
			return resData;
		}
		throw new NotFoundException("OrderItems does not exist with id '"+ id +"'");
	}
	
	/**
	 * Get all Order Items By Order Id
	 * @param
	 * @return
	 */
	@GetMapping("/orders/{id}/orderitems")
	public List<OrderItems> getAllByOrderId(@PathVariable("id") int id) {
		if(orderDao.existsById(id)) {
			Optional<Orders> orderData = orderDao.findById(id);
			//System.out.println("************ orderData : "+orderData.toString());
			//System.out.println("************ orderId : "+orderData.get().getOrderId());
			List<OrderItems> restData = orderitemDao.findByOrderId(orderData.get().getOrderId());
			//System.out.println("************ orderItem Data : "+restData.toString());
			if(!restData.isEmpty()) {
				return restData;
			}
			throw new NotFoundException("Order Item data does not exist");
		}
		throw new NotFoundException("Order does not exist");
	}
	
	/**
	 * Create OrderItems
	 * @param OIReq
	 * @return
	 */
	@PostMapping("/orders/{id}/orderitems")
	public OrderItems save(@RequestBody OrderItems orderItemReq, @PathVariable("id") int id) {
		boolean exists = orderDao.existsById(id);
		if (exists) {
			return orderitemDao.save(orderItemReq);
		}
		throw new NotFoundException("Order doesnot exist with id '"+id +"'");
	}
	

	/**
	 * Update OrderItems
	 * @param OrderItems
	 * @return
	 */
	@PutMapping("/orderitems/{id}")
	public OrderItems udpate(@RequestBody OrderItems orderItem) {
		boolean exists = orderitemDao.existsById(orderItem.getOrderItemId());
		if (exists) {
			return orderitemDao.save(orderItem);
		}
		throw new NotFoundException("OrderItems does not exist with id '"+ orderItem.getOrderItemId() +"'");
	}

	/**
	 * Delete one OrderItems by id
	 * @param OrderItemsId
	 * @return 
	 */
	@DeleteMapping("/orderitems/{id}")
	public ResponseDto deleteOne(@PathVariable("id") int id) {
		boolean exists = orderitemDao.existsById(id);
		if (exists) {
			orderitemDao.deleteById(id);
			return new ResponseDto("Success","OrderItems deleted", new Date(), null);
		}
		throw new NotFoundException("OrderItems does not exist with id '"+ id +"'");
	}

	/**
	 * Delete all OrderItem by order id
	 * @param id
	 * @return 
	 */
	@DeleteMapping("/orders/{id}/orderitems")
	public ResponseDto deleteAllOrderItemsByOrderId(@PathVariable("id") int id) {
		//boolean exists = dishesDao.existsById(id);
		if (orderDao.existsById(id)) {
			orderitemDao.deleteByOrderId(id);
			return new ResponseDto("Success","All Orderitems deleted for order id "+id, new Date(), null);
		}
		throw new NotFoundException("Order with id '"+ id +"' dosenot exist.");
	}

}
