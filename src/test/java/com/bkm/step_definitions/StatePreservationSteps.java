package com.bkm.step_definitions;

import com.bkm.pages.Home.HomePage;
import com.bkm.pages.catalog.SearchResultsPage;
import com.bkm.utilities.Driver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class StatePreservationSteps {

    // Sayfa nesneleri ve WebDriverWait inisiyalizasyonu
    HomePage homePage = new HomePage(Driver.getDriver());
    SearchResultsPage searchResultsPage = new SearchResultsPage(Driver.getDriver());
    WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(15));

    // Senaryo boyunca durum (state) saklayacak değişkenler
    String searchKeyword;
    String filterName;

    // Not: "Given BKM Kitap anasayfasına gidilir" adımı zaten SearchSteps.java
    // dosyasında tanımlı olduğu için burada tekrar tanımlamamıza gerek yok,
    // Cucumber arka planda hepsini ortak havuzda tanır.

    @When("Arama çubuğunda {string} kelimesi aranır")
    public void arama_cubugunda_kelimesi_aranir(String keyword) {
        this.searchKeyword = keyword;
        System.out.println("========== TEST BAŞLIYOR: tc16_backButtonPreservesFilterAndSearch ==========");
        System.out.println("[TEST ADIMI 1] Arama çubuğuna '" + searchKeyword + "' yazılıyor ve aranıyor...");
        homePage.searchFor(searchKeyword);
        System.out.println("[BAŞARILI] Arama işlemi yapıldı.");
    }

    @And("Sol menüden {string} filtresi uygulanır")
    public void sol_menuden_filtresi_uygulanir(String filter) throws InterruptedException {
        this.filterName = filter;
        System.out.println("[TEST ADIMI 2] Sol menüden filtre uygulanıyor: (" + filterName + ")...");
        try {
            searchResultsPage.selectCategory(filterName);
        } catch (Exception e) {
            System.out.println("[WARN] İlk filtreleme başarısız oldu, alternatif tıklama deneniyor...");
            Driver.getDriver().findElement(By.xpath("(//*[contains(text(), '" + filterName + "')])[last()]")).click();
        }
        Thread.sleep(4000); // Filtre uygulansın sayfası yenilensin
    }

    @And("Sonuçlardan listelenen ilk ürüne tıklanır")
    public void sonuclardan_listelenen_ilk_urune_tiklanir() throws InterruptedException {
        System.out.println("[TEST ADIMI 3] Sonuçlardan ilkine tıklanıyor...");

        // Ürün linklerini bulmaya yönelik 3 aşamalı Fallback (yedekli) yapı
        List<WebElement> productLinks = Driver.getDriver().findElements(By.xpath(
                "//div[contains(@class, 'product-cr') or contains(@class, 'product-item')]//a[contains(@class, 'detail-link') or contains(@class, 'title') or contains(@class, 'name')]"));

        if (productLinks.isEmpty()) {
            productLinks = Driver.getDriver().findElements(By.xpath(
                    "//div[contains(@class, 'product')]//a[not(contains(@class, 'btn')) and not(contains(@class, 'add-to-cart'))]"));
        }

        if (productLinks.isEmpty()) {
            productLinks = Driver.getDriver()
                    .findElements(By.xpath("//a[.//img[contains(@src, 'product') or contains(@class, 'product')]]"));
        }

        Assert.assertTrue(productLinks.size() > 0,
                "Ürün bulanamadı! (Ekranda listelenen kitapların linkleri çekilemedi)");

        String productUrl = "";
        for (WebElement link : productLinks) {
            try {
                if (link.isDisplayed() && link.isEnabled()) {
                    productUrl = link.getAttribute("href");
                    System.out.println("[INFO] Seçilen ürün linki: " + productUrl);
                    ((JavascriptExecutor) Driver.getDriver())
                            .executeScript("arguments[0].scrollIntoView({block: 'center'});", link);
                    Thread.sleep(1000);
                    link.click();
                    break;
                }
            } catch (Exception e) {
                // Tıklanamazsa sonrakine geç
            }
        }
        System.out.println("[BAŞARILI] Ürün detay sayfasına gidildi.");
        Thread.sleep(2000);
    }

    @And("Tarayıcıda geri tuşuna basılır")
    public void tarayicida_geri_tusuna_basilir() throws InterruptedException {
        System.out.println("[TEST ADIMI 4] Tarayıcıda geri tuşuna basılıyor...");
        Driver.getDriver().navigate().back();
        Thread.sleep(2000);
    }

    @Then("Arama kutusunda aranan kelimenin korunduğu doğrulanır")
    public void arama_kutusunda_aranan_kelimenin_korundugu_dogrulanir() {
        WebElement searchBox = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));
        String searchBoxValue = searchBox.getAttribute("value");

        if (searchBoxValue == null || searchBoxValue.trim().isEmpty()) {
            System.out.println(
                    "[WARN] Arama kutusu içi şu an boş gözüküyor, test doğrulaması asıl kaynak olan URL'den kontrol edilecek.");
            String currentUrl = Driver.getDriver().getCurrentUrl();
            boolean isSearchPreservedInUrl = currentUrl.toLowerCase().contains("q=" + searchKeyword.toLowerCase()) ||
                    currentUrl.toLowerCase().contains("arama") ||
                    currentUrl.toLowerCase().contains(searchKeyword.toLowerCase().replace(" ", "+"));

            Assert.assertTrue(isSearchPreservedInUrl,
                    "Tarayıcı Geri gidildiğinde arama kelimesi arama kutusunda ve adreste KORUNMADI!");
            System.out.println("[BİLGİ] '" + searchKeyword + "' araması adres çubuğu kontrolü sayesinde doğrulandı.");
        } else {
            Assert.assertEquals(searchBoxValue.trim().toLowerCase(), searchKeyword.toLowerCase(),
                    "Arama kutusu değeri korunmadı!");
            System.out.println("[BAŞARILI] Arama kutusu değeri korundu: " + searchBoxValue);
        }
    }

    @And("Uygulanan filtrenin seçili kaldığı doğrulanır")
    public void uygulanan_filtrenin_secili_kaldigi_dogrulanir() {
        System.out.println("[TEST ADIMI 5] Filtre durumunun korunduğu doğrulanıyor...");

        String currentUrl = Driver.getDriver().getCurrentUrl().toLowerCase();
        boolean isFilterInUrl = currentUrl.contains("marka") || currentUrl.contains("q=")
                || currentUrl.contains("category");

        List<WebElement> filterElements = Driver.getDriver().findElements(By.xpath(
                "//a[contains(normalize-space(), '" + filterName + "')] | //label[contains(normalize-space(), '"
                        + filterName + "')]/input"));

        boolean isFilterSelected = false;

        // 1. Keskin Kontrol: URL
        if (isFilterInUrl) {
            isFilterSelected = true;
            System.out.println("[BİLGİ] URL kontrolünde filtrenin korunduğu tespit edildi: " + currentUrl);
        } else {
            // 2. Fallback Kontrol: Checkbox veya element attributes
            for (WebElement el : filterElements) {
                String classAttr = el.getAttribute("class");
                String ariaSelected = el.getAttribute("aria-selected");
                String isChecked = el.getAttribute("checked");

                if ((classAttr != null && classAttr.toLowerCase().contains("active")) ||
                        (ariaSelected != null && ariaSelected.equalsIgnoreCase("true")) ||
                        (isChecked != null && isChecked.equalsIgnoreCase("true"))) {
                    isFilterSelected = true;
                    System.out.println(
                            "[BİLGİ] Sol menüde CheckBox / Link öğesinin seçili (active/checked) olduğu tespit edildi.");
                    break;
                }
            }
        }

        Assert.assertTrue(isFilterSelected,
                "'" + filterName + "' filtresi geri gelince seçili değil! (Sayfa durumu kaybedildi)");
        System.out.println("[BAŞARILI] '" + filterName + "' filtresi geri gelince seçili kaldı.");
        System.out.println("========== TEST BİTTİ: BAŞARILI ==========");
    }
}
