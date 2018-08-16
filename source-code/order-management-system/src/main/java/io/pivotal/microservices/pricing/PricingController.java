package io.pivotal.microservices.pricing;

import java.math.BigDecimal;
import java.util.logging.Logger;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.pivotal.microservices.exceptions.ProductNotFoundException;

/**
 * Client controller, fetches Products info from the microservice via
 * {@link PricingService}.
 * 
 * @author manikanta.s
 */
@Controller
public class PricingController {

	@Autowired
	protected PricingService productService;

	protected Logger logger = Logger.getLogger(PricingController.class.getName());

	public PricingController(PricingService productService) {
		this.productService = productService;
	}

	@RequestMapping("/pricing")
	public String goHome() {
		return "index";
	}

	@RequestMapping(value = "/pricing/v1/pricequote", produces = "application/json")
	@ResponseBody
	public BigDecimal findPriceOfProduct(@QueryParam("productId") Long productId, @QueryParam("location") String location) {

		logger.info("pricing-service findPriceOfProduct() invoked: " + productId);
		try {
			BigDecimal productPrice = productService.findPriceOfProduct(productId, location);
			logger.info("pricing-service findPriceOfProduct() found: " + productId);
			return productPrice;
		}catch(Exception e){
			throw new ProductNotFoundException(productId.toString());
		}
		
	}
}
