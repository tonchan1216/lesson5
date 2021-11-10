package com.example.lesson5.backend.config;

import com.example.lesson5.common.apinfra.exception.CommonExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ComponentScan("com.example.lesson5.backend.app.web")
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Bean
    CommonExceptionHandler commonExceptionHandler(){
        return new CommonExceptionHandler();
    }

}
