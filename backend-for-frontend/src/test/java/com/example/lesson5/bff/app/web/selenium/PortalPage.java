package com.example.lesson5.bff.app.web.selenium;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Component
public class PortalPage {

    @Autowired(required = false)
    private WebDriver webDriver;

    public void setAddUserForm1(String firstName, String familyName, String loginId,
                                String zipCode, String address, String email){
        webDriver.findElement(By.id("firstName-0")).clear();
        webDriver.findElement(By.id("firstName-0")).sendKeys(firstName);
        webDriver.findElement(By.id("familyName-0")).clear();
        webDriver.findElement(By.id("familyName-0")).sendKeys(familyName);
        webDriver.findElement(By.id("loginId-0")).clear();
        webDriver.findElement(By.id("loginId-0")).sendKeys(loginId);
        webDriver.findElement(By.id("zipCode-0")).clear();
        webDriver.findElement(By.id("zipCode-0")).sendKeys(zipCode);
        webDriver.findElement(By.id("address-0")).clear();
        webDriver.findElement(By.id("address-0")).sendKeys(address);
        webDriver.findElement(By.id("email-0_0")).clear();
        webDriver.findElement(By.id("email-0_0")).sendKeys(email);
    }

    public void setAddUserForm2(String firstName, String familyName, String loginId,
                                String zipCode, String address, String email){
        webDriver.findElement(By.id("firstName-1")).clear();
        webDriver.findElement(By.id("firstName-1")).sendKeys(firstName);
        webDriver.findElement(By.id("familyName-1")).clear();
        webDriver.findElement(By.id("familyName-1")).sendKeys(familyName);
        webDriver.findElement(By.id("loginId-1")).clear();
        webDriver.findElement(By.id("loginId-1")).sendKeys(loginId);
        webDriver.findElement(By.id("zipCode-1")).clear();
        webDriver.findElement(By.id("zipCode-1")).sendKeys(zipCode);
        webDriver.findElement(By.id("address-1")).clear();
        webDriver.findElement(By.id("address-1")).sendKeys(address);
        webDriver.findElement(By.id("email-1_0")).clear();
        webDriver.findElement(By.id("email-1_0")).sendKeys(email);
    }

}
