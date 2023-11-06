package com.testing;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AccountPage {
    private final WebDriver driver;
    private final Logger logger = CommonLogger.getLogger();

    public AccountPage(WebDriver driver) {
        this.driver = driver;
    }

    public void setNewPassword(String newPassword) {
        try {
            WebElement passwordChange = (new WebDriverWait(
                    driver,
                    Duration.ofSeconds(5)
            )).until(ExpectedConditions.presenceOfElementLocated(
                    By.id("password")
            ));
            passwordChange.sendKeys(newPassword);
            Thread.sleep(2000);
            logger.info("ACCPage -> Setting New Password...");
        } catch (Exception e) {
            logger.error("ACCPage -> Caught Exception: " + e.getMessage());
        }
    }

    public void submitUpdateForm() {
        try {
            WebElement updateForm = driver.findElement(
                    By.xpath("/html/body/div[2]/form")
            );
            updateForm.submit();
            Thread.sleep(2000);
            logger.info("ACCPage -> Submitting Update Form...");
        } catch (Exception e) {
            logger.error("ACCPage -> Caught Exception: " + e.getMessage());
        }
    }

    public void logOutToLogin() {
        try {
            WebElement logOutFromAccount = driver.findElement(
                    By.xpath("/html/body/header/div/nav/ul/div/a")
            );
            Thread.sleep(2000);
            logOutFromAccount.click();
            logger.info("ACCPage -> Logging Out to Check the New Password...");
        } catch (Exception e) {
            logger.error("ACCPage -> Caught Exception: " + e.getMessage());
        }
    }
}
