package io.pivotal.microservices.billing.model;

import java.math.BigDecimal;

/**
 * Model class to hold the items billed in invoice
 * @author msreekak
 *
 */
public class ItemDetail {

	private String name;
	private Integer quantity;
	private BigDecimal price;
	private BigDecimal tax;
	private BigDecimal total;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "ItemDetail [name=" + name + ", quantity=" + quantity + ", price=" + price + ", tax=" + tax + ", total="
				+ total + "]";
	}
}
