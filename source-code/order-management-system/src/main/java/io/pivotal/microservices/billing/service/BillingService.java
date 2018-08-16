package io.pivotal.microservices.billing.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import io.pivotal.microservices.billing.model.Billing;
import io.pivotal.microservices.billing.model.Invoice;
import io.pivotal.microservices.billing.model.ItemDetail;
import io.pivotal.microservices.exceptions.OrderNotFoundException;
import io.pivotal.microservices.exceptions.ProductNotFoundException;
import io.pivotal.microservices.exceptions.TaxNotFoundException;
import io.pivotal.microservices.orders.model.OrderDetails;
import io.pivotal.microservices.pricing.Product;

/**
 * Service class to perform interactions with other microservices
 * @author msreekak
 *
 */
@Service
public class BillingService {

	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;

	@Autowired
	private SpringTemplateEngine templateEngine;

	protected String orderServiceUrl;

	protected String productServiceUrl;

	protected String taxServiceUrl;

	protected Logger logger = Logger.getLogger(BillingService.class.getName());

	private String pattern = "MMMM dd, yyyy";

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

	public BillingService(String orderServiceUrl, String productServiceUrl, String taxServiceUrl) {
		this.orderServiceUrl = orderServiceUrl.startsWith("http") ? orderServiceUrl : "http://" + orderServiceUrl;
		this.productServiceUrl = productServiceUrl.startsWith("http") ? productServiceUrl
				: "http://" + productServiceUrl;
		this.taxServiceUrl = taxServiceUrl.startsWith("http") ? taxServiceUrl : "http://" + taxServiceUrl;
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

	public Product getProduct(Long productId) {
		try{
		logger.info("findPriceOfProduct() invoked: for " + productId);
		return restTemplate.getForObject(productServiceUrl + "/products/v1/get?id=" + productId, Product.class);
		}catch(Exception e){
			throw new ProductNotFoundException(productId.toString());
		}
	}

	public BigDecimal getProductTax(Long productId, String location) {
		try{
		logger.info("getProductTax() invoked: for " + productId);
		return restTemplate.getForObject(
				taxServiceUrl + "/tax/v1/compute?productId=" + productId + "&location=" + location, BigDecimal.class);
		}catch(Exception e){
			throw new TaxNotFoundException(productId.toString());
		}
	}

	public OrderDetails[] getOrder(Long orderId) {
		logger.info("getOrder() invoked: for " + orderId);
		String getOrderUrl = orderServiceUrl + "/order/v1/getDetails?orderId=" + orderId;
		System.out.println(getOrderUrl);
		try {
			ResponseEntity<OrderDetails[]> responseEntity = restTemplate.getForEntity(getOrderUrl,
					OrderDetails[].class);
			OrderDetails[] objects = responseEntity.getBody();
			System.out.println(objects.length);
			return objects;
		} catch (Exception e) {
			throw new OrderNotFoundException(orderId.toString());
		}

	}

	public Invoice generateInvoice(Billing billing) {
		Invoice invoice = new Invoice();
		invoice.setBillId(billing.getId());
		invoice.setCreatedDate(new Date());
		invoice.setBillingAddress(billing.getBillingAddress());
		invoice.setLocation(billing.getLocation());
		invoice.setCustomerName(billing.getCustomerName());
		invoice.setEmailId(billing.getEmailId());
		Long orderId = billing.getOrderId();
		OrderDetails[] objects = getOrder(orderId);
		List<ItemDetail> items = new ArrayList<ItemDetail>();
		BigDecimal totalItemPrice = BigDecimal.ZERO;
		BigDecimal totalTax = BigDecimal.ZERO;
		for (int i = 0; i < objects.length; i++) {
			OrderDetails order = objects[i];
			ItemDetail itemDetail = new ItemDetail();
			Product product = getProduct(order.getId().getProductId());
			BigDecimal tax = getProductTax(product.getId(), billing.getLocation());
			itemDetail.setName(product.getName());
			itemDetail.setQuantity(order.getQuantity());
			BigDecimal price = product.getPrice().multiply(new BigDecimal(order.getQuantity()));
			BigDecimal taxPerItem = tax.multiply(new BigDecimal(order.getQuantity()));
			price = price.setScale(2, BigDecimal.ROUND_HALF_EVEN);
			taxPerItem = taxPerItem.setScale(2, BigDecimal.ROUND_HALF_EVEN);
			itemDetail.setPrice(price);
			itemDetail.setTax(taxPerItem);
			BigDecimal total = price.add(taxPerItem).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			itemDetail.setTotal(total);
			totalItemPrice = totalItemPrice.add(price);
			totalTax = totalTax.add(taxPerItem);
			items.add(itemDetail);
		}
		invoice.setItemsBilled(items);
		BigDecimal totalAmount = totalItemPrice.add(totalTax);
		totalItemPrice = totalItemPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		totalTax = totalTax.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		totalAmount = totalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		invoice.setTotalItemPrice(totalItemPrice);
		invoice.setTotalTax(totalTax);
		invoice.setTotalAmount(totalAmount);
		return invoice;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getEmailBody(Invoice invoice) {
		Context context = new Context();
		Map model = new HashMap();
		model.put("billId", invoice.getBillId());
		model.put("createdDate", simpleDateFormat.format(invoice.getCreatedDate()));
		model.put("customerName", invoice.getCustomerName());
		model.put("billingAddress", invoice.getBillingAddress());
		model.put("emailId", invoice.getEmailId());
		model.put("itemDetails", invoice.getItemsBilled());
		model.put("totalAmount", invoice.getTotalAmount());
		context.setVariables(model);
		String html = templateEngine.process("invoice_template", context);
		return html;
	}

	public Invoice dummyInvoice() {
		Invoice invoice = new Invoice();
		invoice.setBillId(new Long(123456));
		invoice.setCreatedDate(new Date());
		invoice.setCustomerName("Customer ABC");
		invoice.setBillingAddress("12345 Sunny Road");
		invoice.setEmailId("mani.rgm448@gmail.com");
		List<ItemDetail> itemDetails = new ArrayList<ItemDetail>();
		ItemDetail a = new ItemDetail();
		a.setName("Product A");
		a.setQuantity(4);
		a.setPrice(new BigDecimal(123.4));
		a.setTax(new BigDecimal(123.4));
		a.setTotal(new BigDecimal(8799.43));
		itemDetails.add(a);
		ItemDetail b = new ItemDetail();
		b.setName("Product B");
		b.setQuantity(2);
		b.setPrice(new BigDecimal(323.4));
		b.setTax(new BigDecimal(143.4));
		b.setTotal(new BigDecimal(8759.43));
		itemDetails.add(b);
		invoice.setItemsBilled(itemDetails);
		invoice.setTotalAmount(new BigDecimal(1234567.89));
		return invoice;
	}
}
