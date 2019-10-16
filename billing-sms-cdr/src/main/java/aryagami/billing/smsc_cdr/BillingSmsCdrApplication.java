package aryagami.billing.smsc_cdr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = { "aryagami.billing.smsc_cdr.config", "aryagami.billing.smsc_cdr",
		"aryagami.billing.smsc_cdr.bs" })
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@IntegrationComponentScan(basePackages = { "aryagami.billing.smsc_cdr.config" })
public class BillingSmsCdrApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingSmsCdrApplication.class, args);
	}
}
