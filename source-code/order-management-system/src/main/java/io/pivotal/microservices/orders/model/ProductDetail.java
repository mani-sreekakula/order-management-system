package io.pivotal.microservices.orders.model;

/**
 * Model class for product details
 * @author msreekak
 *
 */
public class ProductDetail {

	Long productId;
	
	Integer quantity;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "{productId:" + productId + ", quantity:" + quantity + "}";
	}
	
	
}
