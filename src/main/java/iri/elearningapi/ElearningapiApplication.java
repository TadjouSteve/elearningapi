package iri.elearningapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ElearningapiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ElearningapiApplication.class, args);
		System.out.println("Lancement API okeee...!");
	}

	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ElearningapiApplication.class);
	}

}
