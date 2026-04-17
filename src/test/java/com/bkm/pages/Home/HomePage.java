package com.bkm.pages.Home;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private WebDriver driver;

    // Locator'lar (Element Bulucular)
    // Not: "q" BKM Kitap arama çubuğunun name değeridir. Gerekirse güncel DOM'dan
    // teyit edebilirsin.
    public By searchBox = By.name("q");

    public By searchBox() {
        return searchBox;
    }

    // Constructor
    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    // Aksiyon Metotları
    public void navigateToHomePage() {
        driver.get("https://www.bkmkitap.com/");
    }

    public void searchFor(String keyword) {
        // Bazen sayfa yüklenirken DOM elementi yenilenirse StaleElement fırlatır.
        // Bunun önüne geçmek adına ufak bir bekleme ve tekrar (retry) mekanizması
        // ekliyoruz.
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(10));

        int attempts = 0;
        while (attempts < 3) {
            try {
                org.openqa.selenium.WebElement searchInput = wait
                        .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(searchBox));
                searchInput.clear();
                searchInput.sendKeys(keyword);
                searchInput.sendKeys(Keys.ENTER);
                break; // Hata almazsa döngüden çık
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                System.out.println("[WARN] Arama çubuğu yenilendi (StaleElement). Tekrar deneniyor... (Deneme: "
                        + (attempts + 1) + ")");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
            attempts++;
        }
    }
}