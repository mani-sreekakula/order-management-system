package io.pivotal.microservices.pricing;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Product DTO - used to interact with the {@link PricingService}.
 * 
 * @author manikanta.s
 */
@JsonRootName("Product")
public class Product {

	protected Long id;
	protected String name;
	protected String type;
	protected BigDecimal price;

	/**
	 * Default constructor for JPA only.
	 */
	protected Product() {
		price = BigDecimal.ZERO;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getPrice() {
		return price.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
		this.price.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	@Override
	public String toString() {
		return "product " + name + " of type" + type + " with price: $" + price;
	}

}
