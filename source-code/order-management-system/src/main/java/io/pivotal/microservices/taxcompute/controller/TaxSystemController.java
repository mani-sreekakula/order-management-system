package io.pivotal.microservices.taxcompute.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import io.pivotal.microservices.exceptions.ProductAlreadyExistsException;
import io.pivotal.microservices.exceptions.ProductNotFoundException;
import io.pivotal.microservices.exceptions.ProductTypeNotFound;
import io.pivotal.microservices.exceptions.TaxException;
import io.pivotal.microservices.exceptions.TaxNotFoundException;
import io.pivotal.microservices.pricing.Product;
import io.pivotal.microservices.taxcompute.Tax;
import io.pivotal.microservices.taxcompute.TaxSystemService;
import io.pivotal.microservices.taxcompute.model.TaxInputWrapper;
import io.pivotal.microservices.taxcompute.repository.TaxRepository;

/**
 * Client controller, defines tax for product types via
 * {@link TaxSystemService}.
 * 
 * @author manikanta.s
 */
@Controller
public class TaxSystemController {

	@Autowired
	protected TaxSystemService taxSystemService;

	@Autowired
	protected TaxRepository taxRepository;

	protected Logger logger = Logger.getLogger(TaxSystemController.class.getName());

	public TaxSystemController(TaxSystemService taxSystemService) {
		this.taxSystemService = taxSystemService;
	}

	@RequestMapping("/tax")
	public String goHome() {
		return "index";
	}

	/**
	 * Define tax component for specific product type and location
	 * 
	 * @param productName
	 * @param productType
	 * @param price
	 * @return The status
	 * @throws ProductAlreadyExistsException
	 *             If the productName is recognised.
	 */
	@RequestMapping(value = "/tax/v1/define", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Tax defineTax(@RequestBody TaxInputWrapper taxInputWrapper) {
		logger.info("tax-service defineTax() invoked: ");
		try{
			taxSystemService.getProductByType(taxInputWrapper.getProductType());
		}catch(Exception e){
			throw new ProductTypeNotFound(taxInputWrapper.getProductType());
		}
		Tax tax = new Tax(taxInputWrapper.getProductType(), taxInputWrapper.getLocation(), taxInputWrapper.getTaxName(), taxInputWrapper.getTaxPercent());
		try{
			tax = taxRepository.save(tax);
			logger.info("tax-service defineTax() created: " + tax);
			return tax;
		}catch(Exception e){
			throw new TaxException(e.getMessage());
		}
	}

	/**
	 * Fetch all the taxes from catalog
	 * 
	 * @param productType
	 * @return The account if found.
	 * @throws ProductNotFoundException
	 *             If the number is not recognised.
	 */
	@RequestMapping(value = "/tax/v1/list", produces = "application/json")
	@ResponseBody
	public List<Tax> findAll() {

		logger.info("tax-service findAll() invoked: ");
		List<Tax> taxes = Lists.newArrayList(taxRepository.findAll());
		logger.info("tax-service findAll() found: ");

		if (taxes == null || taxes.size() == 0) {
			throw new TaxNotFoundException(null);
		} else {
			return taxes;
		}
	}

	/**
	 * Computes the tax for a product based on location
	 * @param productId
	 * @param location
	 * @return TaxAmount
	 * @throws TaxNotFoundException
	 *             If the tax is not recognised.
	 */
	@RequestMapping(value = "/tax/v1/compute", produces = "application/json")
	@ResponseBody
	public BigDecimal computeTax(@QueryParam("productId") Long productId, @QueryParam("location") String location) {

		logger.info("tax-service computeTax() invoked: " + productId);
		Product product = null;
		try{
			product = taxSystemService.getProduct(productId);
		}catch(Exception e){
			throw new ProductNotFoundException(productId.toString());
		}
		try{
			BigDecimal totalTax = BigDecimal.ZERO;
			List<Tax> taxes = taxRepository.getByTypeAndLocation(product.getType(), location);
			for(Tax tax:taxes){
				totalTax = totalTax.add(tax.getTaxPercent());
			}
			return product.getPrice().multiply(totalTax).divide(new BigDecimal(100));
		}catch(Exception e){
			throw new TaxNotFoundException(product.getType());
		}
	}
}
