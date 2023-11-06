package com.testing;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProfilePage {
    private final WebDriver driver;
    private final Logger logger = CommonLogger.getLogger();

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    public void openAccountPage() {
        try {
            WebElement toAccount = (new WebDriverWait(
                    this.driver,
                    Duration.ofSeconds(5)
            )).until(ExpectedConditions.presenceOfElementLocated(
                    By.id("first")
            ));
            Thread.sleep(2000);
            toAccount.click();
            logger.info("PROFPage -> Opening Account Page...");
        } catch (Exception e) {
            logger.error("PROFPage -> Caught Exception: " + e.getMessage());
        }
    }

    public void waitProfileTill(int millis) {
        try {
            (new WebDriverWait(
                    driver,
                    Duration.ofSeconds(5)
            )).until(ExpectedConditions.presenceOfElementLocated(
                    By.id("first")
            ));
            Thread.sleep(millis);
            logger.info("PROFPage -> Waiting In Profile Page Till " + millis + "...");
        } catch (Exception e) {
            logger.error("PROFPage -> Caught Exception: " + e.getMessage());
        }
    }
}
