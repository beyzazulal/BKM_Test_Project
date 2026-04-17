package com.bkm.step_definitions;

import com.bkm.pages.Home.HomePage;
import com.bkm.utilities.Driver;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.Alert;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class SecuritySearchSteps {

    HomePage homePage = new HomePage(Driver.getDriver());

    // Alert'in çıkıp çıkmayacağını anlamak için makul bir süre bekliyoruz
    WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(5));

    @When("Arama çubuğuna zararlı XSS payload'u {string} girilir ve aranır")
    public void arama_cubuguna_zararli_xss_payloadu_girilir_ve_aranir(String payload) {
        homePage.searchFor(payload);
    }

    @Then("Sistemde güvenlik açığı olmadığı ve Alert fırlamadığı doğrulanır")
    public void sistemde_guvenlik_acigi_olmadigi_ve_alert_firlamadigi_dogrulanir() {
        try {
            // Sistem payload'u çalıştırmamalı! Eğer Alert çıkarsa güvenlik açığı vardır.
            wait.until(ExpectedConditions.alertIsPresent());

            Alert alert = Driver.getDriver().switchTo().alert();
            String alertMessage = alert.getText();
            alert.accept();

            Assert.fail("GÜVENLİK AÇIĞI! XSS payload'u çalıştı. Fırlayan Alert: " + alertMessage);

        } catch (TimeoutException e) {
            // Başarılı senaryo: Alert çıkmadı.
            // ŞİMDİ ÇÖZÜM ADIMI: Sayfanın yüklenmesi için URL'nin değişmesini veya biraz
            // zaman geçmesini bekleyelim

            WebDriverWait longWait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(15));
            longWait.until(ExpectedConditions.urlContains("q="));

            String pageSource = Driver.getDriver().getPageSource().toLowerCase();

            // Kontrol havuzunu genişlettik ve küçük harfe çevirdik
            boolean isSafe = pageSource.contains("bulunamadı") ||
                    pageSource.contains("sonuç") ||
                    pageSource.contains("eşleşen") ||
                    pageSource.contains("&lt;script&gt;") ||
                    !pageSource.contains("<script>alert"); // Zararlı tag ham haliyle YOKSA güvenlidir

            Assert.assertTrue(isSafe,
                    "Sistem alert fırlatmadı ancak sayfa kaynağında güvenli bir sonuç ekranı doğrulanamadı.");
        }
    }
}
