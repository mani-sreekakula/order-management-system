package io.pivotal.microservices.billing.model;

import java.io.Serializable;

/**
 * Wrapper class to receive JSON information from request
 * @author msreekak
 *
 */
public class BillingInputWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6299841731445956574L;

	private Long orderId;
	private String billingAddress;
	private String location;
	private String customerName;
	private String emailId;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

}
