package io.pivotal.microservices.productcatalog.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import io.pivotal.microservices.exceptions.ProductAlreadyExistsException;
import io.pivotal.microservices.exceptions.ProductNotFoundException;
import io.pivotal.microservices.exceptions.ProductTypeNotFound;
import io.pivotal.microservices.exceptions.ProductsNotFoundException;
import io.pivotal.microservices.productcatalog.model.PageableProducts;
import io.pivotal.microservices.productcatalog.model.Product;
import io.pivotal.microservices.productcatalog.model.ProductWrapper;
import io.pivotal.microservices.productcatalog.repository.ProductRepository;

/**
 * A RESTFul controller for accessing product information.
 * 
 * @author manikanta.s
 */
@RestController
public class ProductsController {

	protected Logger logger = Logger.getLogger(ProductsController.class.getName());
	protected ProductRepository productRepository;

	/**
	 * Create an instance plugging in the respository of Products.
	 * 
	 * @param productRepository
	 *            A Product repository implementation.
	 */
	@Autowired
	public ProductsController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	/**
	 * Add product to the catalog service
	 * @param productWrapper
	 * @return
	 */
	@RequestMapping(value = "/products/v1/add", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Product addProduct(@Valid @RequestBody ProductWrapper productWrapper) {
		logger.info("product-catalog-service addProduct() invoked: ");
		List<Product> existingProducts = productRepository.findByName(productWrapper.getProductName());
		if(existingProducts != null && !existingProducts.isEmpty()){
			throw new ProductAlreadyExistsException(productWrapper.getProductName());
		}
		Product product = new Product(productWrapper.getProductName(), productWrapper.getProductType(), productWrapper.getPrice());
		product = productRepository.save(product);
		logger.info("product-catalog-service addProduct() added: " + product);
		return product;
	}
	
	/**
	 * Get the product from the system
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/products/v1/get", produces = "application/json")
	@ResponseBody
	public Product getProduct(@QueryParam("id") Long id) {
		logger.info("product-catalog-service getProduct() invoked: ");
		Product product = productRepository.findOne(id);
		logger.info("product-catalog-service getProduct() created: " + product);

		if (product == null)
			throw new ProductNotFoundException(id.toString());
		else {
			return product;
		}
	}

	/**
	 * Fetch a list of products with the specified product type.
	 * 
	 * @param productType
	 * @return The account if found.
	 * @throws ProductTypeNotFound
	 *             If the number is not recognised.
	 */
	@RequestMapping(value = "/products/v1/type/{productType}", produces = "application/json")
	@ResponseBody
	public List<Product> findByType(@PathVariable("productType") String productType) {

		logger.info("product-catalog-service findByType() invoked: " + productType);
		List<Product> products = productRepository.findByType(productType);
		logger.info("product-catalog-service findByType() found: " + products);

		if (products == null || products.size() == 0)
			throw new ProductTypeNotFound(productType);
		else {
			return products;
		}
	}
	
	/**
	 * Fetch all the products from catalog
	 * 
	 * @param productType
	 * @return The account if found.
	 * @throws ProductNotFoundException
	 *             If the number is not recognised.
	 */
	@RequestMapping(value = "/products/v1/list", produces = "application/json")
	@ResponseBody
	public PageableProducts findAll(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {

		logger.info("product-catalog-service findAll() invoked: ");
		PageableProducts pageableProducts = new PageableProducts();
		pageableProducts.setTotal(productRepository.count());
		if(page == null){
			page = 0;
		}
		if(size == null){
			size = 30;
		}
		pageableProducts.setPage(page);
		pageableProducts.setSize(size);
		List<Product> products = Lists.newArrayList(productRepository.findAll(new PageRequest(page, size)));
		logger.info("product-catalog-service findAll() found: ");
		
		if (products == null || products.size() == 0) {
			throw new ProductsNotFoundException();
		} else {
			pageableProducts.setProducts(products);
			return pageableProducts;
		}
	}

	/**
	 * Delete a product from catalog with given product id
	 * 
	 * @param productId
	 * @return The status.
	 * @throws ProductNotFoundException
	 *             If the id is not recognised.
	 */
	@RequestMapping(value = "/products/v1/delete/{productId}", produces = "application/json")
	@ResponseBody
	public boolean deleteProduct(@PathVariable("productId") Long productId) {

		logger.info("product-catalog-service deleteProduct() invoked: " + productId);
		boolean status = productRepository.exists(productId);
		logger.info("product-catalog-service deleteProduct() found: " + status);

		if (status) {
			productRepository.delete(productId);
			return true;
		} else {
			throw new ProductNotFoundException(productId.toString());
		}
	}
	
	/**
	 * Fetch the price of specified product(Used by pricing-service)
	 * 
	 * @param productId
	 * @return The price of product
	 * @throws ProductNotFoundException
	 *             If the id is not recognised.
	 */
	@RequestMapping(value ="/products/v1/pricing/{productId}", produces = "application/json")
	@ResponseBody
	public BigDecimal findPriceOfProduct(@PathVariable("productId") Long productId) {

		logger.info("product-catalog-service findPriceOfProduct() invoked: " + productId);
		Product product = productRepository.findOne(productId);
		logger.info("product-catalog-service findPriceOfProduct() found: " + product);

		if (product == null)
			throw new ProductNotFoundException(productId.toString());
		else {
			return product.getPrice();
		}
	}
	
//	@RequestMapping(value="/logmeout", method = RequestMethod.POST)
//	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
//	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//	if (auth != null){
//	new SecurityContextLogoutHandler().logout(request, response, auth);
//	}
//	return "logged out";
//	}
}
