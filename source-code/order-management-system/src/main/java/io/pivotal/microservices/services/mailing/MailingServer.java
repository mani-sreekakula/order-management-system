package io.pivotal.microservices.services.mailing;

import javax.jms.ConnectionFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.util.ErrorHandler;

import io.pivotal.microservices.mailing.MailingConfiguration;

/**
 * Mailing server. Works as a microservice, used to send mail using message queue.
 * Uses the Discovery Server (Eureka) to find the microservice.
 * 
 * @author manikanta.s
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableJms
@Import(MailingConfiguration.class)
public class MailingServer {

	/**
	 * Run the application using Spring Boot and an embedded servlet engine.
	 * 
	 * @param args
	 *            Program arguments - ignored.
	 */
	public static void main(String[] args) {
		// Tell server to look for pricing-server.properties or
		// pricing-server.yml
		System.setProperty("spring.config.name", "mailing-server");
		SpringApplication.run(MailingServer.class, args);
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
				t.printStackTrace();
				System.err.println("An error has occurred in the transaction");
			}
		});

		configurer.configure(factory, connectionFactory);
		return factory;
	}

	// Serialize message content to json using TextMessage
	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}
}
