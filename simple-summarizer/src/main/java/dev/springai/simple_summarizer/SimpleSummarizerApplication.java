package dev.springai.simple_summarizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SimpleSummarizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleSummarizerApplication.class, args);
	}

}
