package com.jayjay.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Membuat dan menyimpan satu WebDriver untuk setiap thread test.
 *
 * Headless aktif secara default. Untuk melihat browser saat live:
 *   ./gradlew test -Dheadless=false
 */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER_HOLDER =
            new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        if (DRIVER_HOLDER.get() == null) {
            DRIVER_HOLDER.set(createChromeDriver());
        }
        return DRIVER_HOLDER.get();
    }

    public static void quitDriver() {
        WebDriver existingDriver = DRIVER_HOLDER.get();
        if (existingDriver != null) {
            existingDriver.quit();
            DRIVER_HOLDER.remove();
        }
    }

    private static WebDriver createChromeDriver() {
        setupChromeDriverBinary();

        ChromeOptions chromeOptions = new ChromeOptions();
        if (isHeadlessEnabled()) {
            chromeOptions.addArguments("--headless=new");
        }
        chromeOptions.addArguments("--window-size=1440,900");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--disable-gpu");

        String customChromeBinary = System.getProperty("chromeBinary");
        if (customChromeBinary != null && !customChromeBinary.isBlank()) {
            chromeOptions.setBinary(customChromeBinary);
        }

        WebDriver chromeDriver = new ChromeDriver(chromeOptions);
        chromeDriver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(20));
        return chromeDriver;
    }

    /**
     * Menyiapkan executable chromedriver.
     *
     * Secara default WebDriverManager yang mengunduhkannya. Kalau
     * jaringan kantor memblokir unduhan, chromedriver bisa ditaruh
     * manual lalu ditunjuk lewat:
     *   ./gradlew test -DchromeDriverPath=/path/ke/chromedriver
     */
    private static void setupChromeDriverBinary() {
        String chromeDriverPath = System.getProperty("chromeDriverPath");
        if (chromeDriverPath != null && !chromeDriverPath.isBlank()) {
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);
            return;
        }
        WebDriverManager.chromedriver().setup();
    }

    private static boolean isHeadlessEnabled() {
        String headlessProperty = System.getProperty("headless", "true");
        return !"false".equalsIgnoreCase(headlessProperty);
    }
}
