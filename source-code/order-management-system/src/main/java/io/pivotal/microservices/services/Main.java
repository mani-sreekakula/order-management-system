package io.pivotal.microservices.services;

import io.pivotal.microservices.services.billing.BillingServer;
import io.pivotal.microservices.services.mailing.MailingServer;
import io.pivotal.microservices.services.order.OrderServer;
import io.pivotal.microservices.services.pricing.PricingServer;
import io.pivotal.microservices.services.productcatalog.ProductCatalogServer;
import io.pivotal.microservices.services.registration.RegistrationServer;
import io.pivotal.microservices.services.tax.TaxServer;

/**
 * Allow the servers to be invoked from the command-line. The jar is built with
 * this as the <code>Main-Class</code> in the jar's <code>MANIFEST.MF</code>.
 * 
 * @author Paul Chapman
 */
public class Main {

	public static void main(String[] args) {

		String serverName = "NO-VALUE";

		switch (args.length) {
		case 2:
			// Optionally set the HTTP port to listen on, overrides
			// value in the <server-name>-server.yml file
			System.setProperty("server.port", args[1]);
			// Fall through into ..

		case 1:
			serverName = args[0].toLowerCase();
			break;

		default:
			usage();
			return;
		}

		if (serverName.equals("registration") || serverName.equals("reg")) {
			RegistrationServer.main(args);
		} else if (serverName.equals("product-catalog-service")) {
			ProductCatalogServer.main(args);
		} else if (serverName.equals("pricing-service")) {
			PricingServer.main(args);
		} else if (serverName.equals("tax-service")) {
			TaxServer.main(args);
		} else if (serverName.equals("order-service")) {
			OrderServer.main(args);
		} else if (serverName.equals("billing-service")) {
			BillingServer.main(args);
		} else if (serverName.equals("mailing-service")) {
			MailingServer.main(args);
		} else {
			System.out.println("Unknown server type: " + serverName);
			usage();
		}
	}

	protected static void usage() {
		System.out.println("Usage: java -jar ... <server-name> [server-port]");
		System.out.println("     where server-name is 'reg', 'registration', "
				+ "'product-catalog-service', 'pricing-service', 'tax-service', "
				+ "'order-service', 'billing-service' or 'mailing-service' and server-port > 1024");
	}
}
