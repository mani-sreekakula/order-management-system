package io.pivotal.microservices.taxcompute.repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import io.pivotal.microservices.taxcompute.Tax;

/**
 * Repository for Tax data implemented using Spring Data JPA.
 * 
 * @author manikanta.s
 */
public interface TaxRepository extends CrudRepository<Tax, Long> {

	@Query("select t from Tax t where t.productType = ?1 and t.location = ?2")
	List<Tax> getByTypeAndLocation(String productType, String location);
}
