package org.ogn.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:stats-application-context.xml")
public class OgnGatewayWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(OgnGatewayWebApplication.class, args);
	}
}
