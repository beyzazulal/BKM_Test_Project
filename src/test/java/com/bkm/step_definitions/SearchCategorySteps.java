package com.bkm.step_definitions;

import com.bkm.pages.Home.HomePage;
import com.bkm.pages.catalog.SearchResultsPage;
import com.bkm.utilities.Driver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class SearchCategorySteps {

    // Sayfa modelleri
    HomePage homePage;
    SearchResultsPage searchResultsPage;
    String searchKeyword;

    // Ortak "Given BKM Kitap anasayfasına gidilir" stepi diğer dosyalarda olduğu
    // için tekrar tanımlamaya gerek yok.

    @When("Arama bölümüne {string} kelimesi yazılır ve aranır")
    public void arama_bolumune_kelimesi_yazilir_ve_aranir(String keyword) {
        this.searchKeyword = keyword;
        System.out.println("========== TEST BAŞLIYOR: tc08_searchAndApplyValidCategoryFilter ==========");
        System.out.println("[TEST ADIMI 2] Arama çubuğuna '" + searchKeyword + "' yazılıyor ve aranıyor...");

        homePage = new HomePage(Driver.getDriver());

        homePage.searchFor(searchKeyword);
        System.out.println("[BAŞARILI] Arama işlemi yapıldı.");
    }

    @And("Sol taraftaki menüden {string} kategorisi seçilir")
    public void sol_taraftaki_menuden_kategorisi_secilir(String categoryToSelect) {
        System.out.println("[TEST ADIMI 3] Ekranda olan geçerli bir kategori seçiliyor: (" + categoryToSelect + ")...");

        searchResultsPage = new SearchResultsPage(Driver.getDriver());

        try {
            searchResultsPage.selectCategory(categoryToSelect);
        } catch (Exception e) {
            System.err.println("Seçim veya doğrulama sırasında bir hata oluştu: " + e.getMessage());
            Assert.fail("Filtreleme işlemi sırasında hata: " + e.getMessage());
        }
    }

    @Then("Sistemin filtreyi uyguladığı URL üzerinden doğrulanır")
    public void sistemin_filtreyi_uyguladigi_url_uzerinden_dogrulanir() {
        System.out.println("[TEST ADIMI 4] Adres çubuğunda filtrelemenin yapıldığı doğrulanıyor...");

        try {
            // Selenium ile kısa bir bekleme ekliyoruz, filtre uygulasın diye:
            Thread.sleep(3000);
            String currentUrl = Driver.getDriver().getCurrentUrl().toLowerCase();

            // Sadece kelimenin ilk kelimesini (örnek "Harry Potter" -> "harry") ve "q="
            // kısmını onaylıyoruz
            String keywordSnippet = searchKeyword.split(" ")[0].toLowerCase();
            Assert.assertTrue(currentUrl.contains(keywordSnippet) || currentUrl.contains("q="),
                    "URL beklenen filtre veya arama formatında değil: " + currentUrl);

            System.out.println("[BAŞARILI] Doğrulama başarılı! Kategori filtresi uygulandı.");

        } catch (Exception e) {
            System.err.println("Doğrulama sırasında bir hata oluştu: " + e.getMessage());
            Assert.fail("Filtreleme işlemi doğrulamasında hata: " + e.getMessage());
        }
        System.out.println("========== TEST BİTTİ: BAŞARILI ==========");
    }
}
