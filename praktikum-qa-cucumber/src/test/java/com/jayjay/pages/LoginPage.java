package com.jayjay.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Halaman login SauceDemo.
 */
public class LoginPage {

    private static final String LOGIN_URL = "https://www.saucedemo.com";

    private final WebDriver webDriver;
    private final WebDriverWait explicitWait;

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorContainer = By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.explicitWait =
                new WebDriverWait(webDriver, Duration.ofSeconds(10));
    }

    public void bukaHalamanLogin() {
        webDriver.get(LOGIN_URL);
        explicitWait.until(
                ExpectedConditions.visibilityOfElementLocated(usernameField));
    }

    public void isiUsername(String username) {
        webDriver.findElement(usernameField).clear();
        webDriver.findElement(usernameField).sendKeys(username);
    }

    public void isiPassword(String password) {
        webDriver.findElement(passwordField).clear();
        webDriver.findElement(passwordField).sendKeys(password);
    }

    public void klikTombolLogin() {
        webDriver.findElement(loginButton).click();
    }

    public String ambilPesanError() {
        WebElement errorElement = explicitWait.until(
                ExpectedConditions.visibilityOfElementLocated(errorContainer));
        return errorElement.getText();
    }
}
