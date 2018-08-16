package io.pivotal.microservices.orders.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Embeddable id for OrderDetails class
 * @author msreekak
 *
 */
@Embeddable
public class OrderId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8730367138509697316L;

	@Column(name = "ORDER_ID")
	protected Long orderId;
	
	@Column(name = "PRODUCT_ID")
	protected Long productId;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
}
