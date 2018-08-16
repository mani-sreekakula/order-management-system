package io.pivotal.microservices.productcatalog;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * The products Spring configuration.
 * 
 * @author manikanta.s
 */
@Configuration
@ComponentScan
@EntityScan("io.pivotal.microservices.productcatalog")
@EnableJpaRepositories("io.pivotal.microservices.productcatalog")
@PropertySource("classpath:db-config.properties")
@EnableWebSecurity
public class ProductsConfiguration extends WebSecurityConfigurerAdapter {

	protected Logger logger;

	public ProductsConfiguration() {
		logger = Logger.getLogger(getClass().getName());
	}

	/**
	 * Creates an in-memory "products" database populated with test data for
	 * fast testing
	 */
	@Bean
	public DataSource dataSource() {
		logger.info("dataSource() invoked");

		// Create an in-memory H2 relational database containing some demo
		// products.
		DataSource dataSource = (new EmbeddedDatabaseBuilder()).addScript("classpath:testdb/schema.sql")
				.addScript("classpath:testdb/data.sql").build();

		logger.info("dataSource = " + dataSource);

		// Sanity check
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> products = jdbcTemplate.queryForList("SELECT name FROM T_PRODUCT");
		logger.info("System has " + products.size() + " products");
		
		List<Map<String, Object>> taxes = jdbcTemplate.queryForList("SELECT PRODUCT_TYPE FROM T_TAX");
		logger.info("System has " + taxes.size() + " taxes");

		// Populate with random balances
		Random rand = new Random();

		for (Map<String, Object> item : products) {
			String name = (String) item.get("name");
			BigDecimal price = new BigDecimal(rand.nextInt(10000000) / 100.0).setScale(2, BigDecimal.ROUND_HALF_UP);
			jdbcTemplate.update("UPDATE T_PRODUCT SET price = ? WHERE name = ?", price, name);
		}

		return dataSource;
	}
	
	@Autowired
	private AuthenticationEntryPoint authEntryPoint;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/products/v1/get*", "/products/v1/list", "/products/v1/type/*", "/products/v1/pricing/*")
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
