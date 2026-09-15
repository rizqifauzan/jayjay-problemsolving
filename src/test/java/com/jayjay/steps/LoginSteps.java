package com.jayjay.steps;

// import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import com.jayjay.driver.DriverFactory;
import com.jayjay.pages.InventoryPage;
import com.jayjay.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

/**
 * KASUS 1 (versi BEFORE).
 *
 * Semua assertion di bawah ini memakai assertTrue tanpa pesan.
 * Ketika gagal, output hanya berbunyi "expected: <true> but was:
 * <false>" dan tidak menjelaskan apa pun.
 */
public class LoginSteps {

    private final WebDriver webDriver = DriverFactory.getDriver();
    private final LoginPage loginPage = new LoginPage(webDriver);
    private final InventoryPage inventoryPage = new InventoryPage(webDriver);

    @Given("pengguna membuka halaman login SauceDemo")
    public void penggunaMembukaHalamanLogin() {
        loginPage.bukaHalamanLogin();
    }

    @When("pengguna login dengan username {string} dan password {string}")
    public void penggunaLoginDengan(String username, String password) {
        loginPage.isiUsername(username);
        loginPage.isiPassword(password);
        loginPage.klikTombolLogin();
    }

    @Then("halaman daftar produk tampil")
    public void halamanDaftarProdukTampil() {
        //    assertTrue(inventoryPage.apakahHalamanProdukTampil());
    }

    @Then("pesan error memuat teks {string}")
    public void pesanErrorMemuatTeks(String teksYangDiharapkan) {
        String pesanErrorSebenarnya = loginPage.ambilPesanError();
        assertThat(pesanErrorSebenarnya).as("Memeriksa pesan error yang ditampilkan")
                .isEqualTo(teksYangDiharapkan);
    }
}
