package io.pivotal.microservices.services.productcatalog;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import io.pivotal.microservices.productcatalog.ProductsConfiguration;
import io.pivotal.microservices.productcatalog.repository.ProductRepository;

/**
 * Run as a micro-service, registering with the Discovery Server (Eureka).
 * <p>
 * Note that the configuration for this application is imported from
 * {@link ProductsConfiguration}. This is a deliberate separation of concerns.
 * 
 * @author manikanta.s
 */
@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(ProductsConfiguration.class)
public class ProductCatalogServer {

	@Autowired
	protected ProductRepository productReposiroty;

	protected Logger logger = Logger.getLogger(ProductCatalogServer.class.getName());

	/**
	 * Run the application using Spring Boot and an embedded servlet engine.
	 * 
	 * @param args
	 *            Program arguments - ignored.
	 */
	public static void main(String[] args) {
		// Tell server to look for product-catalog-server.properties or
		// accounts-server.yml
		System.setProperty("spring.config.name", "product-catalog-server");

		SpringApplication.run(ProductCatalogServer.class, args);
	}
}
