package io.pivotal.microservices.billing.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.pivotal.microservices.billing.model.Billing;
import io.pivotal.microservices.billing.model.BillingInputWrapper;
import io.pivotal.microservices.billing.model.Invoice;
import io.pivotal.microservices.billing.repository.BillingRepository;
import io.pivotal.microservices.billing.service.BillingService;
import io.pivotal.microservices.exceptions.OrderNotFoundException;
import io.pivotal.microservices.mailing.model.Mail;

/**
 * Client controller, fetches Billing info and generates Bill from order 
 * 
 * @author msreekak
 */
@Controller
public class BillingController {

	@Autowired
	protected BillingService billingService;

	@Autowired
	protected BillingRepository billingRepository;

	@Autowired
	private JmsTemplate jmsTemplate;

	protected Logger logger = Logger.getLogger(BillingController.class.getName());

	public BillingController(BillingService billingService) {
		this.billingService = billingService;
	}

	/**
	 * Generates Bill from the given order and email the invoice to customer
	 * @param billingInputWrapper
	 * @return Invoice
	 */
	@RequestMapping(value = "/billing/v1/generateBill", produces = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public Invoice generateBill(@RequestBody BillingInputWrapper billingInputWrapper) {
		logger.info("billing-service generateBill() invoked: ");
		Billing billing = new Billing(billingInputWrapper.getOrderId(), billingInputWrapper.getBillingAddress(),
				billingInputWrapper.getLocation(), billingInputWrapper.getCustomerName(),
				billingInputWrapper.getEmailId());
		billing = billingRepository.save(billing);
		// perform the tax computation logic and generate the invoice from it
		Invoice invoice = billingService.generateInvoice(billing);
		String body = billingService.getEmailBody(invoice);
		logger.info("billing-service generateBill() created: ");
		jmsTemplate.convertAndSend("OrderTransactionQueue", new Mail("mani.rgm448@gmail.com", "Invoice Slip", body));
		return invoice;
	}

	/**
	 * Gets the existing bill with the order Id
	 * @param orderId
	 * @return Invoice
	 */
	@RequestMapping(value = "/billing/v1/getBill", produces = "application/json")
	@ResponseBody
	public Invoice getBillDetails(@QueryParam("orderId") Long orderId) {

		logger.info("billing-service getBillDetails() invoked: " + orderId);
		List<Billing> billings = billingRepository.findByOrderId(orderId);
		System.out.println("Order Details: " + billings);
		logger.info("billing-service getBillDetails() found: ");

		if (billings == null || billings.size() == 0) {
			throw new OrderNotFoundException(orderId.toString());
		} else {
			Billing billing = billings.get(0);
			Invoice invoice = billingService.generateInvoice(billing);
			return invoice;
		}
	}
}
