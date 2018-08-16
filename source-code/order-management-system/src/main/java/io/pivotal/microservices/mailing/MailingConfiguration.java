package io.pivotal.microservices.mailing;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * The products Spring configuration.
 * 
 * @author msreekak
 */
@Configuration
@ComponentScan
@EntityScan("io.pivotal.microservices.mailing")
@EnableJpaRepositories("io.pivotal.microservices.mailing")
@PropertySource("classpath:db-config.properties")
@EnableWebSecurity
public class MailingConfiguration extends WebSecurityConfigurerAdapter {

	protected Logger logger;

	public MailingConfiguration() {
		logger = Logger.getLogger(getClass().getName());
	}

	@Autowired
	private AuthenticationEntryPoint authEntryPoint;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/mailing/v1/getMail")
				.permitAll().anyRequest().authenticated().and().httpBasic().authenticationEntryPoint(authEntryPoint)
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().csrf()
				.disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("john123").password("password").roles("USER");
	}
}

