package io.pivotal.microservices.orders.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Persistent Order entity with JPA markup. Taxes are stored in an H2 relational
 * database.
 * 
 * @author manikanta.s
 */
@Entity
@Table(name = "T_ORDER")
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Long nextId = 1L;

	@Id
	protected Long id;

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
	protected Order() {

	}

	public Order(Date createdDate) {
		id = getNextId();
		this.createdDate = createdDate;
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

	

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "Order " + id + " is created on " + createdDate;
	}

}
