package io.pivotal.microservices.orders.service;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.pivotal.microservices.pricing.Product;

/**
 * Hide the access to the microservice inside this local service.
 * 
 * @author manikanta.s
 */
@Service
public class OrderService {

	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;

	protected String serviceUrl;

	protected Logger logger = Logger.getLogger(OrderService.class
			.getName());

	public OrderService(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl
				: "http://" + serviceUrl;
	}

	/**
	 * The RestTemplate works because it uses a custom request-factory that uses
	 * Ribbon to look-up the service to use. This method simply exists to show
	 * this.
	 */
	@PostConstruct
	public void demoOnly() {
		// Can't do this in the constructor because the RestTemplate injection
		// happens afterwards.
		logger.warning("The RestTemplate request factory is "
				+ restTemplate.getRequestFactory().getClass());
	}

	/**
	 * Get the product from product-catalog-service microservice
	 * @param productId
	 * @return
	 */
	public Product getProduct(Long productId) {
		logger.info("findPriceOfProduct() invoked: for " + productId);
		return restTemplate.getForObject(serviceUrl + "/products/v1/get?id="+productId,
				Product.class);
	}
}
