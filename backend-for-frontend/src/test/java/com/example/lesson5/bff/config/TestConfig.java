package com.example.lesson5.bff.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ResourceLoader;

public class TestConfig {

    @SpringBootApplication
    public static class UnitTestConfig{
        @Bean
        public MessageSource messageSource() {
            ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource(){
                @Override
                public void setResourceLoader(ResourceLoader resourceLoader) {
                }
            };
            messageSource.setBasename("messages");
            messageSource.setDefaultEncoding("UTF-8");
            return messageSource;
        }
    }

    @SpringBootApplication
    public static class ControllerTestConfig{
    }

    @SpringBootApplication
    public static class EndToEndTestConfig{
    }

}
