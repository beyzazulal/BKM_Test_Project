package com.bkm.step_definitions;

import com.bkm.pages.Home.HomePage;
import com.bkm.utilities.Driver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class SearchSteps {

    HomePage homePage;
    long startTime;
    long endTime;
    long responseTime;
    String wildcardInput;

    @Given("BKM Kitap anasayfasına gidilir")
    public void bkm_kitap_anasayfasina_gidilir() {
        System.out.println("[TEST ADIMI 1] BKM Kitap anasayfasına gidiliyor...");

        homePage = new HomePage(Driver.getDriver());

        homePage.navigateToHomePage();
        System.out.println("[BAŞARILI] Anasayfa yüklendi.");
    }

    @When("Arama çubuğuna {string} joker karakterleri girilir ve süre ölçülür")
    public void arama_cubuguna_joker_karakterleri_girilir_ve_sure_olculur(String wildcard) {
        this.wildcardInput = wildcard;
        System.out.println("[TEST ADIMI 2] Arama çubuğuna veritabanını yoracak joker karakterler ('" + wildcardInput
                + "') yazılıyor ve aranıyor...");

        startTime = System.currentTimeMillis();
        homePage.searchFor(wildcardInput);

        // Sayfanın DOM'u tamamen çizmesi için kısa bir süre bekle (Timeout ölçümü için
        // bu kısmı hesaplama dışında tutacağız)
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        endTime = System.currentTimeMillis();
        responseTime = (endTime - startTime) - 3000; // sleep süresini çıkarıyoruz
        System.out.println("[BİLGİ] Tahmini Sunucu Yanıt Süresi (Network + İlk Render): " + responseTime + " ms");
    }

    @Then("Sistem çökmeleri için sayfa kaynağında kontrol yapılır")
    public void sistem_cokmeleri_icin_sayfa_kaynaginda_kontrol_yapilir() {
        System.out.println("[TEST ADIMI 3] Sunucunun yanıt süresi ve durumu kontrol ediliyor...");
        String pageSource = Driver.getDriver().getPageSource().toLowerCase();

        // A. ÇÖKME / SERVER ERROR KONTROLÜ
        boolean isServerCrashed = pageSource.contains("500 internal server error") ||
                pageSource.contains("504 gateway time-out") ||
                pageSource.contains("server error") ||
                pageSource.contains("fatal error");

        Assert.assertFalse(isServerCrashed,
                "Sistem joker karakterler (" + wildcardInput + ") girildiğinde ÇÖKTÜ (HTTP 500/504)!");
        System.out.println("[BAŞARILI] Sistem joker karakterler girildiğinde ÇÖKMEDİ.");
    }

    @And("Arama yanıt süresinin {int} milisaniyeden kısa olduğu doğrulanır")
    public void arama_yanit_suresinin_milisaniyeden_kisa_oldugu_dogrulanir(int limit) {
        // B. PERFORMANS / YÜK KONTROLÜ (Full table scan oluyor mu?)
        Assert.assertTrue(responseTime < limit,
                "Sistem yanıt süresi çok uzun (" + responseTime
                        + " ms)! Veritabanında (Full Table Scan) zafiyeti tetiklenmiş olabilir.");
        System.out.println("[BAŞARILI] Sunucu isteğe mantıklı bir süre içinde cevap verdi. (Arama Motoru Yorulmadı)");
    }

    @And("Sonuç davranışı kontrol edilir")
    public void sonuc_davranisi_kontrol_edilir() {
        String pageSource = Driver.getDriver().getPageSource().toLowerCase();

        // C. DAVRANIŞ KONTROLÜ (%%%% yazdığında tüm siteyi mi listeliyor yoksa
        // bulunamadı mı diyor?)
        boolean isEmptyMessageDisplayed = pageSource.contains("bulunamadı") ||
                pageSource.contains("eşleşen") ||
                pageSource.contains("sonuç yok");

        if (!isEmptyMessageDisplayed && pageSource.contains("sepete ekle")) {
            System.err.println(
                    "[WARN/BUG] Sistem joker karakterleri (" + wildcardInput
                            + ") algılayıp boş ekran vermek yerine MİLYONLARCA ürünü listelemeye çalışmış olabilir.");
        }
        System.out.println("========== TEST BİTTİ: BAŞARILI ==========");
    }
}
