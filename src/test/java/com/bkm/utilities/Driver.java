package com.bkm.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.time.Duration;

public class Driver {
    private static InheritableThreadLocal<WebDriver> driverPool = new InheritableThreadLocal<>();

    private Driver() {
    }

    public static WebDriver getDriver() {
        if (driverPool.get() == null) {
            String browser = System.getProperty("browser", "chrome");

            switch (browser.toLowerCase()) {
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions fireOptions = new FirefoxOptions();
                    fireOptions.addArguments("--width=1920");
                    fireOptions.addArguments("--height=1080");
                    driverPool.set(new FirefoxDriver(fireOptions));
                    break;
                case "chrome":
                default:
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--disable-notifications");
                    options.addArguments("--disable-popup-blocking");
                    options.addArguments("--window-size=1920,1080");
                    options.addArguments("--start-maximized");
                    driverPool.set(new ChromeDriver(options));
                    break;
            }
            driverPool.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        }
        return driverPool.get();
    }

    public static void closeDriver() {
        if (driverPool.get() != null) {
            driverPool.get().quit();
            driverPool.remove();
        }
    }

    public static void handleCookies() {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(
                getDriver(), Duration.ofSeconds(3));

        // 1. Kullanıcı Aydınlatma Metni (Login/Register vb. için)
        try {
            org.openqa.selenium.WebElement aydinlatmaButton = wait
                    .until(org.openqa.selenium.support.ui.ExpectedConditions
                            .elementToBeClickable(
                                    org.openqa.selenium.By.id("cms-kullanici-aydinlatma-metni-onay-button")));
            aydinlatmaButton.click();
            System.out.println("📄 Aydınlatma metni başarıyla onaylandı.");
        } catch (Exception e) {
            System.out.println("📄 Aydınlatma metni görünmedi, devam ediliyor...");
        }

        // 2. Çerez Policy (Tümünü Kabul Et)
        try {
            org.openqa.selenium.WebElement cookieButton = wait.until(org.openqa.selenium.support.ui.ExpectedConditions
                    .elementToBeClickable(org.openqa.selenium.By.className("cc-nb-okagree")));
            cookieButton.click();
            System.out.println("🍪 Çerezler başarıyla temizlendi.");
        } catch (Exception e) {
            System.out.println("🍪 Çerez paneli görünmedi, teste devam ediliyor...");
        }
    }
}