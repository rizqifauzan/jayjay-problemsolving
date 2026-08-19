package com.jayjay.steps;

import com.jayjay.driver.DriverFactory;
import com.jayjay.pages.CartPage;
import com.jayjay.pages.InventoryPage;
import com.jayjay.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * KASUS 2 (versi BEFORE).
 *
 * Verifikasi badge keranjang dibungkus try-catch kosong. Locator
 * yang dipakai juga salah ketik. Akibatnya NoSuchElementException
 * ditelan, step tetap lolos, dan skenario tetap HIJAU walaupun
 * verifikasinya tidak pernah benar-benar dijalankan.
 */
public class CartSteps {

    private final WebDriver webDriver = DriverFactory.getDriver();
    private final LoginPage loginPage = new LoginPage(webDriver);
    private final InventoryPage inventoryPage = new InventoryPage(webDriver);
    private final CartPage cartPage = new CartPage(webDriver);

    @Given("pengguna sudah login sebagai {string}")
    public void penggunaSudahLoginSebagai(String username) {
        loginPage.bukaHalamanLogin();
        loginPage.isiUsername(username);
        loginPage.isiPassword("secret_sauce");
        loginPage.klikTombolLogin();
        inventoryPage.apakahHalamanProdukTampil();
    }

    @When("pengguna menambahkan produk {string} ke keranjang")
    public void penggunaMenambahkanProduk(String namaProduk) {
        inventoryPage.tambahProdukKeKeranjang(namaProduk);
    }

    @Then("jumlah pada badge keranjang adalah {string}")
    public void jumlahPadaBadgeKeranjangAdalah(String jumlahYangDiharapkan) {
        try {
            By lokatorBadgeSalahKetik =
                    By.className("shopping_cart_bdge");
            String jumlahSebenarnya =
                    webDriver.findElement(lokatorBadgeSalahKetik).getText();

            if (!jumlahSebenarnya.equals(jumlahYangDiharapkan)) {
                throw new AssertionError("badge tidak sesuai");
            }
        } catch (Exception exceptionYangDitelan) {
            // Sengaja dikosongkan pada versi BEFORE.
        }
    }
}
