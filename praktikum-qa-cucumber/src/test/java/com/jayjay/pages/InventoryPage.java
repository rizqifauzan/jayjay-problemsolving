package com.jayjay.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Halaman daftar produk setelah login berhasil.
 */
public class InventoryPage {

    private final WebDriver webDriver;
    private final WebDriverWait explicitWait;

    private final By inventoryContainer = By.id("inventory_container");
    private final By halamanJudul = By.className("title");

    public InventoryPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.explicitWait =
                new WebDriverWait(webDriver, Duration.ofSeconds(10));
    }

    public boolean apakahHalamanProdukTampil() {
        try {
            explicitWait.until(ExpectedConditions
                    .visibilityOfElementLocated(inventoryContainer));
            return true;
        } catch (org.openqa.selenium.TimeoutException timeoutException) {
            return false;
        }
    }

    public String ambilJudulHalaman() {
        return webDriver.findElement(halamanJudul).getText();
    }

    public void tambahProdukKeKeranjang(String namaProduk) {
        By tombolTambah = By.xpath(
                "//div[text()='" + namaProduk + "']"
                        + "/ancestor::div[@class='inventory_item']"
                        + "//button");
        explicitWait.until(
                ExpectedConditions.elementToBeClickable(tombolTambah)).click();
    }
}
