package com.project.fooddeliverysystem.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.project.fooddeliverysystem.exceptions.UnauthorizedUserException;
import com.project.fooddeliverysystem.model.admin.OrderItems;
import com.project.fooddeliverysystem.model.admin.Orders;
import com.project.fooddeliverysystem.model.user.Users;
import com.project.fooddeliverysystem.security.SecurityService;
import com.project.fooddeliverysystem.services.admin.OrdersServiceImp;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = {"http://ec2-54-82-234-235.compute-1.amazonaws.com:4200","http://ec2-54-82-234-235.compute-1.amazonaws.com:4100"}, allowCredentials = "true")
@RestController
@RequestMapping("/v1")
public class OrderItemsController {
	
	@Autowired
	private OrderItemsDAO orderitemDao;
	
	@Autowired
	private OrdersDAO orderDao;
	
	@Autowired
	private OrdersServiceImp orderService;

	@Autowired private SecurityService securityService;
	/**
	 * Get all OrderItems
	 * @param
	 * @return
	 */
	@GetMapping("/orderitems")
	public List<OrderItems> getAll(HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			List<OrderItems> restData = orderitemDao.findAll();
			if(!restData.isEmpty()) {
				return restData;
			}
			throw new NotFoundException("Order Items data does not exist");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Get OrderItems by id
	 * @param id
	 * @return
	 */
	@GetMapping("/orderitems/{id}")
	public Optional<OrderItems> getOne(@PathVariable("id") int id, HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			Optional<OrderItems> resData = orderitemDao.findById(id);
			if(resData.isPresent()) {
				return resData;
			}
			throw new NotFoundException("OrderItems does not exist with id '"+ id +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Get all Order Items By Order Id
	 * @param
	 * @return
	 */
	@GetMapping("/orders/{id}/orderitems")
	public List<OrderItems> getAllByOrderId(@PathVariable("id") int id, HttpServletRequest request) {
		Optional<Orders> order = orderDao.findById(id);
		if(order!=null && order.isPresent()) {
			boolean isUserAuthorized = securityService.isActionAllowed(order.get().getUser().getId()+"",  request );
			
			if(isUserAuthorized) {

				//System.out.println("************ orderData : "+orderData.toString());
				//System.out.println("************ orderId : "+orderData.get().getOrderId());
				List<OrderItems> restData = orderitemDao.findByOrderId(order.get().getOrderId());
				//System.out.println("************ orderItem Data : "+restData.toString());
				if(!restData.isEmpty()) {
					return restData;
				}
				throw new NotFoundException("Order Item data does not exist");
			}
			throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		}
		throw new NotFoundException("Order does not exist");
	}
	
	/**
	 * Get all Order Items By User Id
	 * @param
	 * @return
	 */
	@GetMapping("/user/{id}/orderitems")
    public List<OrderItems> getOrderItemsForUser(@PathVariable int id, HttpServletRequest request) {
		boolean isUserAuthorized = securityService.isActionAllowed(id+"",  request );
		
		if(isUserAuthorized) {

			//System.out.println("************ orderData : "+orderData.toString());
			//System.out.println("************ orderId : "+orderData.get().getOrderId());
			List<OrderItems> restData = orderService.getOrderItemsForUser(id);
			//System.out.println("************ orderItem Data : "+restData.toString());
			if(!restData.isEmpty()) {
				return restData;
			}
			throw new NotFoundException("Order Item data does not exist");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
    }
	
	/**
	 * Create OrderItems
	 * @param OIReq
	 * @return
	 */
	@PostMapping("/orders/{id}/orderitems")
	public List<OrderItems> save(@RequestBody OrderItems[] orderItemReq, @PathVariable("id") int id, HttpServletRequest request) {
		
		Users udata = orderItemReq[0].getOrder().getUser();
		if(udata==null) {
			int orderId = orderItemReq[0].getOrder().getOrderId();
			udata = orderDao.findById(orderId).get().getUser();
		}
				
		boolean isUserAuthorized = securityService.isActionAllowed(udata.getId()+"",  request );
		
		
		if(isUserAuthorized) {

			boolean exists = orderDao.existsById(id);
			if (exists) {
				return orderService.saveAllOrderItems(orderItemReq);
			}
			throw new NotFoundException("Order doesnot exist with id '"+id +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	

	/**
	 * Update OrderItems
	 * @param OrderItems
	 * @return
	 */
	@PutMapping("/orderitems/{id}")
	public OrderItems udpate(@RequestBody OrderItems orderItem, HttpServletRequest request) {
		boolean isUserAuthorized = securityService.isActionAllowed(orderItem.getOrder().getUser().getId()+"",  request );
		
		if(isUserAuthorized) {
			boolean exists = orderitemDao.existsById(orderItem.getOrderItemId());
			if (exists) {
				return orderitemDao.save(orderItem);
			}
			throw new NotFoundException("OrderItems does not exist with id '"+ orderItem.getOrderItemId() +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}

	/**
	 * Delete one OrderItems by id
	 * @param OrderItemsId
	 * @return 
	 */
	@DeleteMapping("/orderitems/{id}")
	public ResponseDto deleteOne(@PathVariable("id") int id, HttpServletRequest request) {
		Optional<OrderItems> orderItem = orderitemDao.findById(id);
		if(orderItem!=null && orderItem.isPresent()) {
			boolean isUserAuthorized = securityService.isActionAllowed(orderItem.get().getOrder().getUser().getId()+"",  request );
			
			if(isUserAuthorized) {
				orderitemDao.deleteById(id);
				return new ResponseDto("Success","OrderItems deleted", new Date(), null);
				
			}
			throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		}
		throw new NotFoundException("OrderItems does not exist with id '"+ id +"'");
	}

	/**
	 * Delete all OrderItem by order id
	 * @param id
	 * @return 
	 */
	@DeleteMapping("/orders/{id}/orderitems")
	public ResponseDto deleteAllOrderItemsByOrderId(@PathVariable("id") int id, HttpServletRequest request) {
		Optional<Orders> order = orderDao.findById(id);
		if(order!=null && order.isPresent()) {
			boolean isUserAuthorized = securityService.isActionAllowed(order.get().getUser().getId()+"",  request );
			
			if(isUserAuthorized) {
				//boolean exists = dishesDao.existsById(id);
				orderitemDao.deleteByOrderId(id);
				return new ResponseDto("Success","All Orderitems deleted for order id "+id, new Date(), null);
				
			}
			throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		}
		throw new NotFoundException("Order does not exist with id '"+ id +"'");
	}

}
