package io.pivotal.microservices.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Allow the controller to return a 404 if a product type in tax is not found by simply
 * throwing this exception. The @ResponseStatus causes Spring MVC to return a
 * 404 instead of the usual 500.
 * 
 * @author msreekak
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaxNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TaxNotFoundException(String productType) {
		super((productType != null ? "No taxes found for productType: "+productType : "No taxes found"));
	}
}