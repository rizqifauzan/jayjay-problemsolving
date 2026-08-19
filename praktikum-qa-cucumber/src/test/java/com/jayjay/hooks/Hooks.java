package com.jayjay.hooks;

import com.jayjay.driver.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;

/**
 * Hook Cucumber: menyiapkan browser sebelum skenario dan
 * menutupnya setelah skenario selesai.
 */
public class Hooks {

    @Before
    public void bukaBrowser() {
        DriverFactory.getDriver();
    }

    @After
    public void tutupBrowser() {
        DriverFactory.quitDriver();
    }
}
