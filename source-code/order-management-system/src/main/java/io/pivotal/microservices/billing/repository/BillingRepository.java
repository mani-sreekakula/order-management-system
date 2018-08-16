package io.pivotal.microservices.billing.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.pivotal.microservices.billing.model.Billing;

/**
 * Repository for Billing implemented using Spring Data JPA.
 * @author msreekak
 *
 */
public interface BillingRepository extends CrudRepository<Billing, Long> {

	List<Billing> findByOrderId(Long orderId);
}
