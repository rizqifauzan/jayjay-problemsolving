package com.jayjay.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Ikon keranjang dan halaman keranjang SauceDemo.
 */
public class CartPage {

    private final WebDriver webDriver;
    private final WebDriverWait explicitWait;

    public CartPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.explicitWait =
                new WebDriverWait(webDriver, Duration.ofSeconds(10));
    }

    /**
     * Mengembalikan WebDriver supaya step definition dapat mencari
     * elemen badge secara langsung. Sengaja dibiarkan begini pada
     * versi BEFORE, akan dirapikan saat live session.
     */
    public WebDriver getWebDriver() {
        return webDriver;
    }

    public WebDriverWait getExplicitWait() {
        return explicitWait;
    }

    public By lokatorBadgeKeranjang() {
        return By.className("shopping_cart_badge");
    }

    By lokatorBadgeKeranjang = By.className("shopping_cart_badge");

    public String getBadge(){
        return webDriver.findElement(lokatorBadgeKeranjang).getText();
    }
}
