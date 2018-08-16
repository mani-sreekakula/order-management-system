package io.pivotal.microservices.productcatalog.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import io.pivotal.microservices.productcatalog.model.Product;

/**
 * Repository for Product data implemented using Spring Data JPA.
 * 
 * @author manikanta.s
 */
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
	/**
	 * Find a list of products with the specified product type.
	 *
	 * @param productType
	 * @return The product if found, null otherwise.
	 */
	public List<Product> findByType(String productType);
	
	public List<Product> findByName(String productName);
	
}
