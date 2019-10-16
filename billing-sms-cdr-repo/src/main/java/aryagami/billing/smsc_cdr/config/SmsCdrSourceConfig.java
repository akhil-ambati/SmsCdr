package aryagami.billing.smsc_cdr.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = {
		"aryagami.billing.smsc_cdr.repository" }, entityManagerFactoryRef = "smsEntityManager", transactionManagerRef = "smsTransactionManager")
@EntityScan(basePackages = { "aryagami.billing.smsc_cdr.model" })
public class SmsCdrSourceConfig {

	@Autowired
	private Environment env;

	@Bean
	// @Primary
	public LocalContainerEntityManagerFactoryBean smsEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(smsCdrSource());
		em.setPackagesToScan(new String[] { "aryagami.billing.smsc_cdr.model" });
		em.setPersistenceUnitName("smsEntityManager");
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
		properties.put("hibernate.show-sql", env.getProperty("hibernate.show-sql"));
		properties.put("hibernate.ddl-auto", env.getProperty("hibernate.ddl-auto"));
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.ddl-auto"));
		em.setJpaPropertyMap(properties);
		return em;
	}

	// @Primary
	@Bean
	public DataSource smsCdrSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driver-class-name"));
		dataSource.setUrl(env.getProperty("billingsmscdrrepo.datasource.url"));
		dataSource.setUsername(env.getProperty("billingsmscdrrepo.datasource.username"));
		dataSource.setPassword(env.getProperty("billingsmscdrrepo.datasource.password"));

		return dataSource;
	}

	// @Primary
	@Bean
	public PlatformTransactionManager smsTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(smsEntityManager().getObject());
		return transactionManager;
	}
}
