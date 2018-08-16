package io.pivotal.microservices.services.tax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import io.pivotal.microservices.taxcompute.TaxConfiguration;
import io.pivotal.microservices.taxcompute.TaxSystemService;
import io.pivotal.microservices.taxcompute.controller.HomeController;
import io.pivotal.microservices.taxcompute.controller.TaxSystemController;
import io.pivotal.microservices.taxcompute.repository.TaxRepository;

/**
 * Tax server. Works as a microservice, defining tax to the products in catalog and computes tax on demand. 
 * Uses the Discovery Server (Eureka) to find the microservice.
 * 
 * @author manikanta.s
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(TaxConfiguration.class)
public class TaxServer {

	@Autowired
	protected TaxRepository taxReposiroty;
	
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
		System.setProperty("spring.config.name", "tax-server");
		SpringApplication.run(TaxServer.class, args);
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
	public TaxSystemService taxSystemService() {
		return new TaxSystemService(PRODUCTS_SERVICE_URL);
	}

	/**
	 * Create the controller, passing it the {@link TaxSystemService} to use.
	 * 
	 * @return
	 */
	@Bean
	public TaxSystemController taxSystemController() {
		return new TaxSystemController(taxSystemService());
	}

	@Bean
	public HomeController homeController() {
		return new HomeController();
	}
}
