package io.pivotal.microservices.services.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import io.pivotal.microservices.orders.OrderConfiguration;
import io.pivotal.microservices.orders.controller.OrderController;
import io.pivotal.microservices.orders.repository.OrderRepository;
import io.pivotal.microservices.orders.service.OrderService;
import io.pivotal.microservices.taxcompute.TaxSystemService;
import io.pivotal.microservices.taxcompute.controller.HomeController;

/**
 * Order server. Works as a microservice, add products to the order. 
 * Uses the Discovery Server (Eureka) to find the microservice.
 * 
 * @author manikanta.s
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(OrderConfiguration.class)
public class OrderServer {

	@Autowired
	protected OrderRepository orderReposiroty;
	
	/**
	 * URL uses the logical name of product-catalog-service - upper or lower case,
	 * doesn't matter.
	 */
	public static final String PRODUCTS_SERVICE_URL = "http://PRODUCT-CATALOG-SERVICE";

	/**
	 * Run the application using Spring Boot and an embedded servlet engine.
	 * 
	 * @param args
	 *            Program arguments - ignored.
	 */
	public static void main(String[] args) {
		// Tell server to look for pricing-server.properties or pricing-server.yml
		System.setProperty("spring.config.name", "order-server");
		SpringApplication.run(OrderServer.class, args);
	}

	/**
	 * A customized RestTemplate that has the ribbon load balancer build in.
	 * Note that prior to the "Brixton" 
	 * 
	 * @return
	 */
	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * The ProductService encapsulates the interaction with the micro-service.
	 * 
	 * @return A new service instance.
	 */
	@Bean
	public OrderService orderService() {
		return new OrderService(PRODUCTS_SERVICE_URL);
	}

	/**
	 * Create the controller, passing it the {@link TaxSystemService} to use.
	 * 
	 * @return
	 */
	@Bean
	public OrderController orderController() {
		return new OrderController(orderService());
	}

	@Bean
	public HomeController homeController() {
		return new HomeController();
	}
}
