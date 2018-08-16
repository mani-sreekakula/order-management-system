package io.pivotal.microservices.orders.repository;
import org.springframework.data.repository.CrudRepository;

import io.pivotal.microservices.orders.model.Order;

/**
 * Repository for Order data implemented using Spring Data JPA.
 * 
 * @author manikanta.s
 */
public interface OrderRepository extends CrudRepository<Order, Long> {

//	@Query("select t from Tax t where t.productType = ?1 and t.location = ?2")
//	List<Tax> getByTypeAndLocation(String productType, String location);
}
