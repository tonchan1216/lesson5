package com.example.lesson5.bff.config;

import com.example.lesson5.bff.domain.ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;

@ComponentScan("com.example.lesson5.bff.domain")
@Configuration
public class DomainConfig {

    @Autowired
	ServiceProperties properties;

	@Bean
	public RestOperations restOperations(RestTemplateBuilder restTemplateBuilder){
		return restTemplateBuilder.rootUri(properties.getDns()).build();
	}

}
