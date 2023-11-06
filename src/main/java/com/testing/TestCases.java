package com.testing;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;

public class TestCases {
    private WebDriver driver;
    private SignInPage login;
    private ProfilePage profile;
    private final Logger logger = CommonLogger.getLogger();

    @BeforeTest
    public void beforeTest() {
        // Starting Point
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("http://13.53.173.239:8080/");
        logger.info("Tests Start / Window Website opens");
    }

    @Test
    public void MoveToSignInPage() {
        // Move to Sign In page From Index
        login = new SignInPage(driver);
        login.openSignInPage();
        login.moveToRegistration();
        logger.info("Move to Registration Page");
    }

    @Parameters({"name", "surname", "phone", "password", "email"})
    @Test(dependsOnMethods = {"MoveToSignInPage"})
    public void SignUpAndFillDataPage(
            String name,
            String surname,
            String phone,
            String password,
            String email
    ) {
        // Filling the data and creating an Account
        SignUpPage registration = new SignUpPage(driver);
        registration.setName(name);
        registration.setSurname(surname);
        registration.setPhone(phone);
        registration.setPassword(password);
        registration.setEmail(email);
        registration.submitSignUpForm();
        logger.info(
                "Data such as Name, Surname, Phone Number, Password" +
                " and Email are filled and sent to Database"
        );
    }

    @Parameters({"email", "password"})
    @Test(dependsOnMethods = {"SignUpAndFillDataPage"})
    public void SignInAndEnterAccountPage(
            String email,
            String password
    ) {
        // Sign In to User Profile after registration
        login = new SignInPage(driver);
        login.setEmail(email);
        login.setPassword(password);
        login.submitSignInForm();
        logger.info("Login to User Account");
    }

    @Test(dependsOnMethods = {"SignInAndEnterAccountPage"})
    public void OpenAccountPage() {
        // Open Account Page
        profile = new ProfilePage(driver);
        profile.openAccountPage();
        logger.info("Open Account Page, where User's data");
    }

    @Parameters({"new_password"})
    @Test(dependsOnMethods = {"OpenAccountPage"})
    public void SetNewPasswordAndLogOutInAccountPage(
            String new_password
    ) {
        // Assigning New password in Account Page and Logging Out
        AccountPage account = new AccountPage(driver);
        account.setNewPassword(new_password);
        account.submitUpdateForm();
        account.logOutToLogin();
        logger.info("Setting New password for User and Logging Out for Check");
    }

    @Parameters({"email", "new_password"})
    @Test(dependsOnMethods = {"SetNewPasswordAndLogOutInAccountPage"})
    public void CheckNewPasswordInSignInPage(
            String email,
            String new_password
    ) {
        // And again Sign In to Check New password
        login = new SignInPage(driver);
        login.setEmail(email);
        // New password
        login.setPassword(new_password);
        login.submitSignInForm();
        logger.info("Sign In to Profile Page again, to check the New Password");
    }

    @Parameters({"millis"})
    @AfterTest
    public void afterTest(int millis) {
        // Waiting on Profile Page as end point
        profile = new ProfilePage(driver);
        profile.waitProfileTill(millis);
        logger.info("Waiting Till " + millis + " to Check Consistency of Website");
        // Quiting Driver / Testing End
        driver.quit();
        logger.info("Quitting Driver of Selenium / The End of Testing");
    }
}
