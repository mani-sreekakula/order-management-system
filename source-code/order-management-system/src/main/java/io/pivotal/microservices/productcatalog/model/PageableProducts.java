package io.pivotal.microservices.productcatalog.model;

import java.io.Serializable;
import java.util.List;

/**
 * Model class to return pagination of product objects
 * @author msreekak
 *
 */
public class PageableProducts implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7653983943975616962L;

	private long total;

	private int page;

	private int size;

	private List<Product> products;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "PageableProducts [total=" + total + ", page=" + page + ", size=" + size + ", products=" + products
				+ "]";
	}

}
