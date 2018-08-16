package io.pivotal.microservices.orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import io.pivotal.microservices.orders.model.OrderDetails;
import io.pivotal.microservices.orders.model.OrderId;
/**
 * Repository for OrderDetails implemented using Spring Data JPA.
 * @author msreekak
 *
 */
public interface OrderDetailsRepository  extends CrudRepository<OrderDetails, OrderId> {

	@Query("select o from OrderDetails o where o.id.orderId = :orderId")
	List<OrderDetails> findByOrderId(@Param("orderId") Long orderId);
	
}
