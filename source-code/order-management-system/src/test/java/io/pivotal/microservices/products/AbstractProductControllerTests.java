package io.pivotal.microservices.products;

import java.math.BigDecimal;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import io.pivotal.microservices.productcatalog.controller.ProductsController;
import io.pivotal.microservices.productcatalog.model.Product;

// The following are equivalent, we only need to use one.

// 1. Read test properties from a file - neater when there are multiple properties
@TestPropertySource(locations = "classpath:tests.properties")

// 2. Define test properties directly, acceptable here since we only have one.
// @TestPropertySource(properties={"eureka.client.enabled=false"})
public abstract class AbstractProductControllerTests {

	protected static final String PRODUCT_NAME = "P1";
	protected static final String PRODUCT_TYPE = "PT1";
	protected static final BigDecimal PRODUCT_PRICE = new BigDecimal(100);

	@Autowired
	ProductsController productsController;

	@Test
	public void validProduct() {
		Logger.getGlobal().info("Start validAccountNumber test");
		Long productId = new Long(1);
		Product product = productsController.getProduct(productId);

		Assert.assertNotNull(product);
		Logger.getGlobal().info("End validAccount test");
	}
}
