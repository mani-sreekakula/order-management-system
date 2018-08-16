package io.pivotal.microservices.taxcompute.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Wrapper class to get the json request from rest end points
 * @author msreekak
 *
 */
public class TaxInputWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -265873934610138305L;

	private String productType;
	private String location;
	private String taxName;
	private BigDecimal taxPercent;

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public BigDecimal getTaxPercent() {
		return taxPercent;
	}

	public void setTaxPercent(BigDecimal taxPercent) {
		this.taxPercent = taxPercent;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

}
