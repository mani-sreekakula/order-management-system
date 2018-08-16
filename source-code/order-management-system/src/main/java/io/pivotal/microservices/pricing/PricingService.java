package io.pivotal.microservices.pricing;

import java.math.BigDecimal;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Hide the access to the microservice inside this local service.
 * 
 * @author manikanta.s
 */
@Service
public class PricingService {

	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;

	protected String productServiceUrl;

	protected String taxComputeServiceUrl;

	protected Logger logger = Logger.getLogger(PricingService.class.getName());

	public PricingService(String productServiceUrl, String taxComputeServiceUrl) {
		this.productServiceUrl = productServiceUrl.startsWith("http") ? productServiceUrl
				: "http://" + productServiceUrl;
		this.taxComputeServiceUrl = taxComputeServiceUrl.startsWith("http") ? taxComputeServiceUrl
				: "http://" + taxComputeServiceUrl;
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
		logger.warning("The RestTemplate request factory is " + restTemplate.getRequestFactory().getClass());
	}

	public BigDecimal findPriceOfProduct(Long productId, String location) {

		logger.info("findPriceOfProduct() invoked: for " + productId);
		Product product = restTemplate.getForObject(productServiceUrl + "/products/v1/get?id=" + productId,
				Product.class);
		BigDecimal taxAmount = BigDecimal.ZERO;
		try {
			taxAmount = restTemplate.getForObject(
					taxComputeServiceUrl + "/tax/v1/compute?productId=" + productId + "&location=" + location,
					BigDecimal.class);
		} catch (Exception e) {
			System.out.println("Exception in getting tax" + e.getMessage());
		}
		return product.getPrice().add(taxAmount);
	}

}
