package io.pivotal.microservices.billing.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Model class to share the Invoice details
 * @author msreekak
 *
 */
public class Invoice {

	private Long billId;

	private Date createdDate;

	private String billingAddress;

	private String location;

	private String customerName;

	private String emailId;

	private List<ItemDetail> itemsBilled;

	private BigDecimal totalItemPrice;

	private BigDecimal totalTax;

	private BigDecimal totalAmount;

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public List<ItemDetail> getItemsBilled() {
		return itemsBilled;
	}

	public void setItemsBilled(List<ItemDetail> itemsBilled) {
		this.itemsBilled = itemsBilled;
	}

	public BigDecimal getTotalItemPrice() {
		return totalItemPrice;
	}

	public void setTotalItemPrice(BigDecimal totalItemPrice) {
		this.totalItemPrice = totalItemPrice;
	}

	public BigDecimal getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(BigDecimal totalTax) {
		this.totalTax = totalTax;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Override
	public String toString() {
		return "Invoice [billId=" + billId + ", createdDate=" + createdDate + ", billingAddress=" + billingAddress
				+ ", location=" + location + ", customerName=" + customerName + ", emailId=" + emailId
				+ ", itemsBilled=" + itemsBilled + ", totalItemPrice=" + totalItemPrice + ", totalTax=" + totalTax
				+ ", totalAmount=" + totalAmount + "]";
	}

	
}
