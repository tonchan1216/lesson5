package com.example.lesson5.bff.app.web.selenium;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "selenium")
public class SeleniumProperties {

    private String evidencePath;
    private String chromeDriverPath;

}
