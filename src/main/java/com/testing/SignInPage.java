package com.testing;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignInPage {

    private final WebDriver driver;
    private final Logger logger = CommonLogger.getLogger();

    public SignInPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openSignInPage() {
        WebElement signUpButton = driver.findElement(
                By.xpath("/html/body/header/div/nav/ul/li[4]/a/button")
        );
        signUpButton.click();
        logger.info("SIPage -> Opening Sign In page...");
    }

    public void moveToRegistration() {
        try {
            WebElement toReg = (new WebDriverWait(
                    this.driver,
                    Duration.ofSeconds(5)
            )).until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("a[href='/registration']"))
            );
            Thread.sleep(2000);
            toReg.click();
            logger.info("SIPage -> Moving to registration page...");
        } catch (Exception e) {
            logger.error("SIPage -> Caught Exception: " + e.getMessage());
        }
    }

    public void submitSignInForm() {
        driver.findElement(
                By.cssSelector("input[class='submitBtn']")
        ).click();
        logger.info("SIPage -> Submitting Sign In form...");
    }

    public void setEmail(String email) {
        try {
            WebElement siEmail = (new WebDriverWait(
                    driver,
                    Duration.ofSeconds(5)
            )).until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
            siEmail.sendKeys(email);
            Thread.sleep(1000);
            logger.info("SIPage -> Setting Email field...");
        } catch (Exception e) {
            logger.error("SIPage -> Caught Exception: " + e.getMessage());
        }
    }

    public void setPassword(String password) {
        try {
            WebElement siPassword = driver.findElement(By.id("password"));
            siPassword.sendKeys(password);
            Thread.sleep(2000);
            logger.info("SIPage -> Setting Password field...");
        } catch (Exception e) {
            logger.error("SIPage -> Caught Exception: " + e.getMessage());
        }
    }
}
