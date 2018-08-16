package io.pivotal.microservices.billing.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Persistent Billing entity with JPA markup. Billing Info are stored in an H2 relational
 * database.
 * 
 * @author msreekak
 */
@Entity
@Table(name = "T_BILLING")
public class Billing implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Long nextId = 1048L;

	@Id
	protected Long id;

	@Column(name = "ORDER_ID")
	protected Long orderId;
	
	@Column(name = "BILLING_ADDRESS")
	protected String billingAddress;
	
	@Column(name = "LOCATION")
	protected String location;
	
	@Column(name = "CUSTOMER_NAME")
	protected String customerName;
	
	@Column(name = "EMAIL_ID")
	protected String emailId;
	
	@Column(name = "CREATED_DATE")
	protected Date createdDate;

	/**
	 * This is a very simple, and non-scalable solution to generating unique
	 * ids. Not recommended for a real application. Consider using the
	 * <tt>@GeneratedValue</tt> annotation and a sequence to generate ids.
	 * 
	 * @return The next available id.
	 */
	protected static Long getNextId() {
		synchronized (nextId) {
			return nextId++;
		}
	}

	/**
	 * Default constructor for JPA only.
	 */
	protected Billing() {

	}

	public Billing(Long orderId, String billingAddress, String location, String customerName, String emailId) {
		id = getNextId();
		this.orderId = orderId;
		this.billingAddress = billingAddress;
		this.location = location;
		this.customerName = customerName;
		this.emailId = emailId;
		this.createdDate = new Date();
	}

	public long getId() {
		return id;
	}

	/**
	 * Set JPA id - for testing and JPA only. Not intended for normal use.
	 * 
	 * @param id
	 *            The new id.
	 */
	protected void setId(long id) {
		this.id = id;
	}


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
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "{orderId: " + orderId + ", billingAddress: " + billingAddress + ", location: " + location
				+ ", customerName: " + customerName + ", emailId: " + emailId + "}";
	}

}
