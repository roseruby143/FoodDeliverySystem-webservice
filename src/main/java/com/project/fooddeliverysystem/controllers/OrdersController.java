package com.project.fooddeliverysystem.controllers;

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

import com.project.fooddeliverysystem.dao.admin.DeliveryDAO;
import com.project.fooddeliverysystem.dao.admin.OrdersDAO;
import com.project.fooddeliverysystem.dao.user.UserDAO;
import com.project.fooddeliverysystem.dto.ResponseDto;
import com.project.fooddeliverysystem.exceptions.NotFoundException;
import com.project.fooddeliverysystem.exceptions.UnauthorizedUserException;
import com.project.fooddeliverysystem.model.admin.Delivery;
import com.project.fooddeliverysystem.model.admin.Orders;
import com.project.fooddeliverysystem.model.user.Users;
import com.project.fooddeliverysystem.security.SecurityService;
import com.project.fooddeliverysystem.services.admin.OrdersServiceImp;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = {"http://ec2-54-82-234-235.compute-1.amazonaws.com:4200","http://ec2-54-82-234-235.compute-1.amazonaws.com:4100"}, allowCredentials = "true")
//@CrossOrigin(origins = {"http://localhost:4200","http://localhost:4100"}, allowCredentials = "true")
@RestController
@RequestMapping("/v1")
public class OrdersController {
	
	@Autowired
	private OrdersDAO ordersDao;
	
	@Autowired 
	private UserDAO userDao;
	
	@Autowired
	private DeliveryDAO deliveryDao;

	@Autowired 
	private OrdersServiceImp orderService;

	@Autowired private SecurityService securityService;
	
	/**
	 * Get all Order
	 * @param
	 * @return
	 */
	@GetMapping("/orders")
	public List<Orders> getAllOrders(HttpServletRequest request) {
		if(securityService.isAdmin(request)) {
			List<Orders> restData = ordersDao.findAll();
			if(!restData.isEmpty()) {
				return restData;
			}
			throw new NotFoundException("Orders does not exist");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Get Orders by id
	 * @param id
	 * @return
	 */
	@GetMapping("/orders/{id}")
	public Optional<Orders> getOneOrderById(@PathVariable("id") int id, HttpServletRequest request) {
		int userId = ordersDao.findById(id).get().getUser().getId();
		boolean isUserAuthorized = securityService.isActionAllowed(userId+"",  request );
		if(isUserAuthorized) {
			Optional<Orders> resData = ordersDao.findById(id);
			if(resData.isPresent()) {
				return resData;
			}
			throw new NotFoundException("Orders does not exist with id '"+ id +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Get all Orders By User Id
	 * @param
	 * @return
	 */
	@GetMapping("/user/{id}/orders")
	public List<Orders> getAllByUserId(@PathVariable("id") int id, HttpServletRequest request) {
		//int userId = ordersDao.findById(id)?.get().getUser().getId();
		boolean isUserAuthorized = securityService.isActionAllowed(id+"",  request );
		if(isUserAuthorized) {
			if(userDao.existsById(id)) {
				List<Orders> restData = ordersDao.findByUserId(id);
				if(!restData.isEmpty()) {
					return restData;
				}
				throw new NotFoundException("Orders data does not exist for userid '"+id+"'");
			}
			throw new NotFoundException("User with id '"+id+"' does not exist");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	/**
	 * Create Orders
	 * @param ordersReq
	 * @return
	 */
	@PostMapping("/user/{id}/orders")
	public Orders saveOrder(@RequestBody Orders ordersReq, @PathVariable("id") int id, HttpServletRequest request) {
		int userId = ordersReq.getUser().getId();
		boolean isUserAuthorized = securityService.isActionAllowed(userId+"",  request );
		if(isUserAuthorized) {
			boolean exists = userDao.existsById(id);
			if (exists) {
				ordersReq.setOrderDate(new Date());
				ordersReq.setOrderStatus("Order Created");
				Delivery d = orderService.createDeliveryForOrder(ordersReq);
				Delivery newDelivery = deliveryDao.save(d);
				ordersReq.setDelivery(newDelivery);
				//System.out.println("********** New newDelivery : "+newDelivery);
				//System.out.println("********** New ordersReq : "+ordersReq);
				//orderService.updateDelivery(newDelivery);
				return ordersDao.save(ordersReq);
				//orderService.
				 
			}
			throw new NotFoundException("User with id '"+id +"' doesnot exist");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	

	/**
	 * Update Orders
	 * @param id
	 * @return
	 */
	@PutMapping("/orders/{id}")
	public Orders udpateOrder(@RequestBody Orders orderData, HttpServletRequest request) {
		int userId = orderData.getUser().getId();
		boolean isUserAuthorized = securityService.isActionAllowed(userId+"",  request );
		if(isUserAuthorized) {
			boolean exists = ordersDao.existsById(orderData.getOrderId());
			if (exists) {
				//System.out.println("******************* orderData : "+orderData.toString());
				return ordersDao.save(orderData);
			}
			throw new NotFoundException("Orders does not exist with id '"+ orderData.getOrderId() +"'");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}

	/**
	 * Delete one Orders by id
	 * @param Ordersid
	 * @return 
	 */
	@DeleteMapping("/orders/{id}")
	public ResponseDto deleteOneOrder(@PathVariable("id") int id, HttpServletRequest request) {
		Optional<Orders> orderData = ordersDao.findById(id);
		if(orderData!=null && !orderData.isEmpty()) {
			int userId = orderData.get().getUser().getId();
			boolean isUserAuthorized = securityService.isActionAllowed(userId+"",  request );
			if(isUserAuthorized) {
				ordersDao.deleteById(id);
				return new ResponseDto("Success","Orders deleted", new Date(), null);
			}
			throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		}
		throw new NotFoundException("Orders does not exist with id '"+ id +"'");
	}
	
	/**
	 * Delete all Orders by User id
	 * @param id
	 * @return 
	 */
	@DeleteMapping("/user/{id}/orders") // TODO check if we can do it without foreach
	public ResponseDto deleteAllOrdersByUserId(@PathVariable("id") int id, HttpServletRequest request) {
		//int userId = userDao.findById(id).get().getId();
		boolean isUserAuthorized = securityService.isActionAllowed(id+"",  request );
		if(isUserAuthorized) {

			if (userDao.existsById(id)) {
				List<Orders> orderList = ordersDao.findByUserId(id);
				ordersDao.deleteByUserId(id);
				//System.out.println("*************** orderList: "+orderList.toString());
				orderList.forEach(list -> {
					Delivery delData = list.getDelivery();
					//System.out.println("*************** delData: "+delData.toString());
					if(delData != null) {
						//System.out.println("*************** delData to be deleted for - "+delData.getDeliveryId());
						
						deliveryDao.deleteById(delData.getId());
					}
				});
				return new ResponseDto("Success","All Orders deleted for User id "+id, new Date(), null);
			}
			throw new NotFoundException("User with id '"+ id +"' dosenot exist.");
		}
		throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	
	/*************** Order -> Delivery Controllers ***************/
	
	/**
	 * Get all Deliverys By Order Id
	 * @param
	 * @return
	 */
	@GetMapping("/orders/{id}/delivery")
	public Optional<Delivery> getAllByOrderId(@PathVariable("id") int id, HttpServletRequest request) {
		Optional<Orders> orderDetail = ordersDao.findById(id);
		if(orderDetail!=null && orderDetail.isPresent()) {
			boolean isUserAuthorized = securityService.isActionAllowed(orderDetail.get().getUser().getId()+"",  request );
			if(isUserAuthorized) {
				if(orderDetail.get().getDelivery() != null) {
					return deliveryDao.findById(orderDetail.get().getDelivery().getId());
				}
				throw new NotFoundException("Delivery for order with id '"+ id +"' dosenot exist.");
				
			}
			throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		}

		throw new NotFoundException("Order does not exist");
		
		
	}

	/**
	 * Delete all Deliveries by order id
	 * @param restaurantId
	 * @return 
	 */
	@DeleteMapping("/orders/{id}/delivery")
	public ResponseDto deleteAllDeliveryByOrderId(@PathVariable("id") int id, HttpServletRequest request) {
		//boolean exists = dishesDao.existsById(id);
		Optional<Orders> orderDetail = ordersDao.findById(id);
		if(orderDetail!=null && orderDetail.isPresent()) {
			boolean isUserAuthorized = securityService.isActionAllowed(orderDetail.get().getUser().getId()+"",  request );
			if(isUserAuthorized) {
				//Optional<Orders> orderDetail = ordersDao.findById(id);
				if(orderDetail.get().getDelivery()!=null) {
					deliveryDao.deleteById(orderDetail.get().getDelivery().getId());
					return new ResponseDto("Success","All Delivery deleted for order id "+id, new Date(), null);
				}
				throw new NotFoundException("Delivery for order with id '"+ id +"' dosenot exist.");
			}
			throw new UnauthorizedUserException("Action not authorized for this user", HttpServletResponse.SC_UNAUTHORIZED);
		}
		throw new NotFoundException("Order with id '"+ id +"' dosenot exist.");
		
	}
	

}
