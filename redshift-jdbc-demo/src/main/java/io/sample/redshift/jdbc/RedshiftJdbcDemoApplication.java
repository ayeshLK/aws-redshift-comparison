package io.sample.redshift.jdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class RedshiftJdbcDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedshiftJdbcDemoApplication.class, args);
	}

}
