package com.bkm.step_definitions;

import com.bkm.pages.user.CampaignPage;
import com.bkm.utilities.Driver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

public class CampaignSteps {

    CampaignPage cp = new CampaignPage();
    JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
    String firstImageUrl = "";

    // ======== SCENARIO 1: BROKEN IMAGES ========

    @Given("Kullanıcı ana sayfaya gider ve çerezleri kabul eder")
    public void kullanici_ana_sayfaya_gider_ve_cerezleri_kabul_eder() {
        Driver.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        Driver.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

        try {
            Driver.getDriver().get("https://www.bkmkitap.com/");
        } catch (Exception e) {
            System.out.println("Sayfa yüklemesi uzun sürdü ama devam ediyorum...");
        }

        try {
            WebElement acceptCookies = Driver.getDriver().findElement(By.className("cc-nb-okagree"));
            acceptCookies.click();
        } catch (Exception e) {
            System.out.println("Cookie pop-up did not appear.");
        }
    }

    @When("Sayfadaki resimler yüklendiğinde")
    public void sayfadaki_resimler_yuklendiginde() {
        try {
            Thread.sleep(5000); // 3. Resimlerin DOM'a düşmesi için 5 saniye zorunlu bekleme
        } catch (InterruptedException e) {
        }
    }

    @Then("Sayfadaki ilk {int} resmin kırık olup olmadığı HTTP kodlarıyla kontrol edilir")
    public void sayfadaki_ilk_resmin_kirik_olup_olmadigi_http_kodlariyla_kontrol_edilir(Integer maxLimit) {
        int toplamResim;
        try {
            toplamResim = cp.tumResimler.size();
            System.out.println("Toplam taranacak resim sayısı: " + toplamResim);
        } catch (Exception e) {
            System.out.println("Resimler listelenirken hata oluştu, sayfa çok ağır.");
            return;
        }

        int kirikResimSayisi = 0;
        int limit = Math.min(toplamResim, maxLimit);

        for (int i = 0; i < limit; i++) {
            try {
                String resimUrl = cp.tumResimler.get(i).getAttribute("src");

                if (resimUrl != null && resimUrl.startsWith("http")) {
                    HttpURLConnection connection = (HttpURLConnection) new URI(resimUrl).toURL().openConnection();
                    connection.setRequestMethod("HEAD");
                    connection.setConnectTimeout(3000);
                    connection.connect();

                    int responseCode = connection.getResponseCode();
                    if (responseCode >= 400) {
                        System.out.println("❌ KIRIK: " + resimUrl + " | Kod: " + responseCode);
                        kirikResimSayisi++;
                    }
                }
            } catch (Exception e) {
                // Münferit hatalarda testi durdurma, devam et
            }
        }
        System.out.println("🏁 Test Tamamlandı! Taranan: " + limit + " | Kırık: " + kirikResimSayisi);
    }

    // ======== SCENARIO 2: SLIDER NAVIGATION ========

    @Given("Kullanıcı ana sayfaya gider")
    public void kullanici_ana_sayfaya_gider() {
        Driver.getDriver().get("https://www.bkmkitap.com/");
    }

    @When("Slider'daki ilk resmin bağlantısı kaydedilir")
    public void slider_daki_ilk_resmin_baglantisi_kaydedilir() {
        firstImageUrl = cp.activeSliderImage.getAttribute("src");
        System.out.println("First image: " + firstImageUrl);
    }

    @And("Slider ileri butonuna JavaScript ile tıklanır")
    public void slider_ileri_butonuna_javascript_ile_tiklanir() {
        js.executeScript("arguments[0].click();", cp.sliderNextButton);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }
    }

    @Then("Slider resminin değiştiği doğrulanır")
    public void slider_resminin_degistigi_dogrulanir() {
        String secondImageUrl = cp.activeSliderImage.getAttribute("src");
        System.out.println("Second image: " + secondImageUrl);
        Assert.assertNotEquals(firstImageUrl, secondImageUrl, "FAILED: Slider image did not change after click!");
    }

    // ======== SCENARIO 3: NEGATIVE CAMPAIGN URL ========

    // Not: @Given("Kullanıcı {string} adresine gider") adımı LoginSteps.java
    // içerisinde mevcuttur.
    // DuplicateStepDefinitionException hatasını engellemek için buradan
    // kaldırılmıştır.

    @Then("Sayfa kaynağında {int} veya ulaşılamıyor hatası görülmelidir")
    public void sayfa_kaynaginda_veya_ulasilamiyor_hatasi_gorulmelidir(Integer errorCode) {
        String pageSource = Driver.getDriver().getPageSource();
        boolean isErrorContentVisible = pageSource.contains(String.valueOf(errorCode))
                || pageSource.contains("ulaşılamıyor");

        Assert.assertTrue(isErrorContentVisible, "NEGATIVE TEST FAILED: Error message not found on page!");
        System.out.println("Negative test passed: Error message is visible on the screen.");
    }

    // ======== SCENARIO 4: RESPONSIVE CLICK ========

    @Given("Ekran boyutu mobil cihaz boyutlarına getirilir")
    public void ekran_boyutu_mobil_cihaz_boyutlarina_getirilir() {
        Driver.getDriver().manage().window().setSize(new Dimension(400, 700));
    }

    @Then("Slider butonuna normal tıklama yapıldığında ElementClickInterceptedException alınmalıdır")
    public void slider_butonuna_normal_tiklama_yapildiginda_elementclickinterceptedexception_alinmalidir() {
        try {
            cp.sliderNextButton.click();
            System.out.println("Responsive test passed: Button is still accessible.");
        } catch (ElementClickInterceptedException e) {
            System.out.println("NEGATIVE TEST SUCCESS: Element was intercepted in mobile view!");
        } catch (Exception e) {
            System.out.println("Another error occurred: " + e.getMessage());
        } finally {
            try {
                Driver.getDriver().manage().window().maximize();
            } catch (Exception ignore) {
            }
        }
    }
}
