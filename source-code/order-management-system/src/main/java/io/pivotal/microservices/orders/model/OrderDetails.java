package io.pivotal.microservices.orders.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Persistent OrderDetails entity with JPA markup. Taxes are stored in an H2 relational
 * database.
 * 
 * @author manikanta.s
 */
@Entity
@Table(name = "T_ORDER_DETAILS")
public class OrderDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected OrderId id;

	@Column(name = "QUANTITY")
	protected Integer quantity;

	/**
	 * Default constructor for JPA only.
	 */
	protected OrderDetails() {

	}

	public OrderDetails(Long orderId, Long productId, Integer quantity) {
		id = new OrderId();
		id.setOrderId(orderId);
		id.setProductId(productId);
		this.quantity = quantity;
	}

	public OrderId getId() {
		return id;
	}

	public void setId(OrderId id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "{orderId :" + id.getOrderId() + ",productId: " + id.getProductId() + ", quantity: " + quantity + "}";
	}

}
