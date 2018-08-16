package io.pivotal.microservices.taxcompute;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Persistent Tax entity with JPA markup. Taxes are stored in an H2 relational
 * database.
 * 
 * @author manikanta.s
 */
@Entity
@Table(name = "T_TAX")
public class Tax implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Long nextId = 3L;

	@Id
	protected Long id;

	@Column
	protected String location;

	@Column(name = "PRODUCT_TYPE")
	protected String productType;
	
	@Column(name = "TAX_NAME")
	protected String taxName;

	@Column(name = "TAX_PERCENT")
	protected BigDecimal taxPercent;

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
	protected Tax() {

	}

	public Tax(String productType, String location, String taxName, BigDecimal taxPercent) {
		id = getNextId();
		this.productType = productType;
		this.location = location;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
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

	@Override
	public String toString() {
		return "Tax " + taxPercent + "% for product type" + productType;
	}

}
