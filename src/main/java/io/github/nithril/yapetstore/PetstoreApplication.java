package io.github.nithril.yapetstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityFilterAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by nlabrot on 21/11/16.
 */
@SpringBootApplication(exclude = {
		SecurityFilterAutoConfiguration.class,
		H2ConsoleAutoConfiguration.class
})
public class PetstoreApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PetstoreApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(PetstoreApplication.class, args);
	}
}
