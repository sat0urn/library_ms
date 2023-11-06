package com.testing;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignUpPage {
    private final WebDriver driver;
    private final Logger logger = CommonLogger.getLogger();

    public SignUpPage(WebDriver driver) {
        this.driver = driver;
    }

    public void submitSignUpForm() {
        driver.findElement(
                By.cssSelector("input[class='submitBtn']")
        ).click();
        logger.info("SUPage -> Submitting Sign Up form...");
    }

    public void setName(String name) {
        try {
            WebElement suName = (new WebDriverWait(
                    this.driver,
                    Duration.ofSeconds(5)
            )).until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
            suName.sendKeys(name);
            Thread.sleep(1000);
            logger.info("SUPage -> Setting Name field...");
        } catch (Exception e) {
            logger.error("SUPage -> Caught Exception: " + e.getMessage());
        }
    }

    public void setSurname(String surname) {
        try {
            WebElement suSurname = driver.findElement(By.id("surname"));
            suSurname.sendKeys(surname);
            Thread.sleep(1000);
            logger.info("SUPage -> Setting Surname field...");
        } catch (Exception e) {
            logger.error("SUPage -> Caught Exception: " + e.getMessage());
        }
    }

    public void setPhone(String phone) {
        try {
            WebElement suNumber = driver.findElement(By.id("phone"));
            suNumber.sendKeys(phone);
            Thread.sleep(2000);
            logger.info("SUPage -> Setting Phone Number field...");
        } catch (Exception e) {
            logger.error("SUPage -> Caught Exception: " + e.getMessage());
        }
    }

    public void setPassword(String password) {
        try {
            WebElement suPassword = driver.findElement(By.id("password"));
            suPassword.sendKeys(password);
            Thread.sleep(1000);
            logger.info("SUPage -> Setting Password field...");
        } catch (Exception e) {
            logger.error("SUPage -> Caught Exception: " + e.getMessage());
        }
    }

    public void setEmail(String email) {
        try {
            WebElement suEmail = driver.findElement(By.id("email"));
            suEmail.sendKeys(email);
            Thread.sleep(2000);
            logger.info("SUPage -> Setting Email field...");
        } catch (Exception e) {
            logger.error("SUPage -> Caught Exception: " + e.getMessage());
        }
    }
}
