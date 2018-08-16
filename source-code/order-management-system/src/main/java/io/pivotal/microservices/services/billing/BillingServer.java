package io.pivotal.microservices.services.billing;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.util.ErrorHandler;
import org.springframework.web.client.RestTemplate;

import io.pivotal.microservices.billing.BillingConfiguration;
import io.pivotal.microservices.billing.controller.BillingController;
import io.pivotal.microservices.billing.controller.HomeController;
import io.pivotal.microservices.billing.repository.BillingRepository;
import io.pivotal.microservices.billing.service.BillingService;

/**
 * Billing server. Works as a microservice, generates bill and email the
 * invoice. Uses the Discovery Server (Eureka) to find the microservice.
 * 
 * @author manikanta.s
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableJms
@Import(BillingConfiguration.class)
public class BillingServer {

	@Autowired
	protected BillingRepository billingReposiroty;

	/**
	 * URL uses the logical name of product-catalog-service - upper or lower
	 * case, doesn't matter.
	 */
	public static final String PRODUCTS_SERVICE_URL = "http://PRODUCT-CATALOG-SERVICE";
	public static final String ORDER_SERVICE_URL = "http://ORDER-SERVICE";
	public static final String TAX_SERVICE_URL = "http://TAX-SERVICE";

	/**
	 * Run the application using Spring Boot and an embedded servlet engine.
	 * 
	 * @param args
	 *            Program arguments - ignored.
	 */
	public static void main(String[] args) {
		// Tell server to look for pricing-server.properties or
		// pricing-server.yml
		System.setProperty("spring.config.name", "billing-server");
		SpringApplication.run(BillingServer.class, args);
	}

	/**
	 * A customized RestTemplate that has the ribbon load balancer build in.
	 * Note that prior to the "Brixton"
	 * 
	 * @return
	 */
	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * The ProductService encapsulates the interaction with the micro-service.
	 * 
	 * @return A new service instance.
	 */
	@Bean
	public BillingService billingService() {
		return new BillingService(ORDER_SERVICE_URL, PRODUCTS_SERVICE_URL, TAX_SERVICE_URL);
	}

	/**
	 * Create the controller, passing it the {@link BillingService} to use.
	 * 
	 * @return
	 */
	@Bean
	public BillingController billingController() {
		return new BillingController(billingService());
	}

	@Bean
	public HomeController homeController() {
		return new HomeController();
	}

	// Only required due to defining myFactory in the receiver
	@Bean
	public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

		factory.setMessageConverter(jacksonJmsMessageConverter());
		// anonymous class
		factory.setErrorHandler(new ErrorHandler() {
			@Override
			public void handleError(Throwable t) {
				System.err.println("An error has occurred in the transaction");
			}
		});

		configurer.configure(factory, connectionFactory);
		return factory;
	}

	@Value("${spring.activemq.broker-url}")
	String brokerUrl;

	@Value("${spring.activemq.user}")
	String userName;

	@Value("${spring.activemq.password}")
	String password;

	/*
	 * Initial ConnectionFactory
	 */
	@Bean
	public ConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(brokerUrl);
		connectionFactory.setUserName(userName);
		connectionFactory.setPassword(password);
		return connectionFactory;
	}

	@Bean // Serialize message content to json using TextMessage
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

	/*
	 * Used for Sending Messages.
	 */
	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate template = new JmsTemplate();
		template.setMessageConverter(jacksonJmsMessageConverter());
		template.setConnectionFactory(connectionFactory());
		return template;
	}

}
