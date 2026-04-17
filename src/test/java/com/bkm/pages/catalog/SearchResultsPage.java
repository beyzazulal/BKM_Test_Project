package com.bkm.pages.catalog;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SearchResultsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    // Seçim yapılınca çıkacak uyarı elementinin genel locatörü. Siteden teyit
    // edilebilir:
    private By noResultMessage = By
            .xpath("//div[contains(@class, 'empty') or contains(text(), 'bulunamadı') or contains(text(), 'yok')]");

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private void closeAnnoyingPopups() {
        System.out.println("[INFO] Ekrandaki pop-up engelleri aranıyor ve kapatılıyor...");
        try {
            // 1. Ortadaki "Masaüstü bildirimlerine ekleyin" - "Daha Sonra" butonu
            By laterBtn = By.xpath(
                    "//*[contains(text(), 'Daha Sonra') or contains(text(), 'İzin Verme') or contains(@class, 'close')]");
            java.util.List<org.openqa.selenium.WebElement> laterElements = driver.findElements(laterBtn);
            for (org.openqa.selenium.WebElement btn : laterElements) {
                if (btn.isDisplayed()) {
                    btn.click();
                    System.out.println("  -> 'Daha Sonra/Kapat' butonuna basıldı.");
                    Thread.sleep(500); // Kapanması için kısa bir süre
                }
            }

            // 2. Sağ alttaki "Çerezleri kullanıyoruz" - "Tümünü Kabul Et" butonu
            By acceptCookiesBtn = By.className("cc-nb-okagree");
            java.util.List<org.openqa.selenium.WebElement> cookieElements = driver.findElements(acceptCookiesBtn);
            for (org.openqa.selenium.WebElement btn : cookieElements) {
                if (btn.isDisplayed()) {
                    btn.click();
                    System.out.println("  -> 'cc-nb-okagree' butonuna basıldı.");
                    Thread.sleep(500);
                }
            }

            // 3. Ekrandaki herhangib bir modal/overlay kapanması için ESC tuşuna bir defa
            // bas (Önlem)
            new org.openqa.selenium.interactions.Actions(driver).sendKeys(org.openqa.selenium.Keys.ESCAPE).perform();

        } catch (Exception e) {
            System.out.println("[WARN] Pop-up kapatma rutini tamamlanamadı (önemsiz).");
        }
    }

    public void selectCategory(String categoryName) {
        System.out.println("[INFO] Sol menüden kategorisi seçiliyor: " + categoryName);

        // Engelleri temizlemek (Gerçek kullanıcı simülasyonu)
        closeAnnoyingPopups();

        // Sol filtre alanındaki kategori linkini bulup tıklama:
        // Üst ana menüde de "Çocuk Kitapları" yazdığından kazara ona tıklarsak sayfa
        // tamamen değişir, arama bozulur.
        // Bu yüzden filtrenin geçtiği linkleri bulup aralarından URL'sinde "arama" veya
        // "q=" içeren HAS FİLTRE linkini seçiyoruz.
        java.util.List<org.openqa.selenium.WebElement> elements = wait.until(ExpectedConditions
                .presenceOfAllElementsLocatedBy(By.xpath("//a[contains(normalize-space(), '" + categoryName + "')]")));

        org.openqa.selenium.WebElement targetFilterElement = null;
        for (org.openqa.selenium.WebElement el : elements) {
            String href = el.getAttribute("href");
            // Eğer link sol tarafta bir filtre ise URL'sinde hâlâ eski arama kelimemiz
            // ("q=" vb.) kalmalıdır.
            if (href != null && (href.contains("q=") || href.contains("arama") || href.contains("category="))) {
                targetFilterElement = el;
                break;
            }
        }

        // Garanti olsun diye, eğer özel href'i bulamazsak sayfadaki SON eşleşen linke
        // tıkla (Çünkü Header menü hep en üsttedir, sol bar / filtre HTML'de daha
        // alttadır).
        if (targetFilterElement == null && !elements.isEmpty()) {
            targetFilterElement = elements.get(elements.size() - 1);
        }

        try {
            // Elementin yüklenebilir ve tıklanabilir olduğunu bekle:
            wait.until(ExpectedConditions.elementToBeClickable(targetFilterElement)).click();
            System.out.println("[SUCCESS] '" + categoryName + "' kategorisi başarıyla tıklandı.");
        } catch (Exception e) {
            System.out.println("[WARN] Normal tıklama başarısız. Javascript ile tıklanıyor...");
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();",
                    targetFilterElement);
            System.out.println("[SUCCESS] '" + categoryName + "' kategorisi JS ile başarıyla tıklandı.");
        }
    }

    public String getNoResultMessage() {
        System.out.println("[INFO] Uyarı/Bilgi mesajı için bekleniyor...");
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(noResultMessage)).getText();
        System.out.println("[SUCCESS] Ekranda okunan mesaj: " + text);
        return text;
    }
}
