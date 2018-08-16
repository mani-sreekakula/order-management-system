package io.pivotal.microservices.orders.model;

import java.util.List;

/**
 * Wrapper class for products 
 * @author msreekak
 *
 */
public class ProductsWrapper {

	List<ProductDetail> products;

	public List<ProductDetail> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDetail> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return products.toString();
	}

	
}
