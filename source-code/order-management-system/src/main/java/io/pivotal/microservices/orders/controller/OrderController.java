package io.pivotal.microservices.orders.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.pivotal.microservices.exceptions.OrderNotFoundException;
import io.pivotal.microservices.exceptions.ProductNotFoundException;
import io.pivotal.microservices.orders.model.Order;
import io.pivotal.microservices.orders.model.OrderDetails;
import io.pivotal.microservices.orders.model.ProductDetail;
import io.pivotal.microservices.orders.model.ProductsWrapper;
import io.pivotal.microservices.orders.repository.OrderDetailsRepository;
import io.pivotal.microservices.orders.repository.OrderRepository;
import io.pivotal.microservices.orders.service.OrderService;

/**
 * Client controller, process order info from the microservice.
 * 
 * @author msreekak
 */
@Controller
public class OrderController {

	@Autowired
	protected OrderService orderService;

	@Autowired
	protected OrderRepository orderRepository;
	
	@Autowired
	protected OrderDetailsRepository orderDetailsRepository;

	protected Logger logger = Logger.getLogger(OrderController.class.getName());

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	/**
	 * Add the order with products to the system
	 * @param productsList
	 * @return orderList
	 * @throw ProductNotFoundException if product not recognized.
	 */
	@RequestMapping(value = "/order/v1/add", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public List<OrderDetails> addOrder(@RequestBody ProductsWrapper productsList) {
		logger.info("order-service addOrder() invoked: ");
		Order order = new Order(new Date());
		order = orderRepository.save(order);
		List<OrderDetails> orderDetailsList = new ArrayList<OrderDetails>();
		for(ProductDetail productDetails: productsList.getProducts()){
			Long productId = productDetails.getProductId();
			try{
				orderService.getProduct(productId);
			}catch(Exception e){
				throw new ProductNotFoundException(productId.toString());
			}
		}
		for(ProductDetail productDetails: productsList.getProducts()){
			OrderDetails orderDetails = new OrderDetails(order.getId(), productDetails.getProductId(), productDetails.getQuantity());
			orderDetails = orderDetailsRepository.save(orderDetails);
			orderDetailsList.add(orderDetails);
		}
		logger.info("order-service addOrder() created: ");

		if (orderDetailsList.isEmpty())
			throw new ProductNotFoundException(null);
		else {
			return orderDetailsList;
		}
	}

	/**
	 * 
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/order/v1/getDetails", produces = "application/json")
	@ResponseBody
	public List<OrderDetails> getOrderDetails(@QueryParam("orderId") Long orderId) {

		logger.info("order-service getOrderDetails() invoked: "+orderId);
		List<OrderDetails> orderDetails = orderDetailsRepository.findByOrderId(orderId);
		System.out.println("Order Details: "+ orderDetails);
		logger.info("order-service getOrderDetails() found: ");

		if (orderDetails == null || orderDetails.size() == 0) {
			throw new OrderNotFoundException(orderId.toString());
		} else {
			return orderDetails;
		}
	}
}
