package com.example.lesson5.bff.config;

import com.example.lesson5.common.apinfra.exception.CommonExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ComponentScan("com.example.lesson5.bff.app.web")
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**")
				.addResourceLocations("classpath:/static/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/portal");
	}

	@Bean
	CommonExceptionHandler commonExceptionHandler(){
    	return new CommonExceptionHandler();
	}

}
