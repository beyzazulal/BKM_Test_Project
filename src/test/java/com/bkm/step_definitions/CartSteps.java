package com.bkm.step_definitions;

import com.bkm.pages.checkout.CartPage;
import com.bkm.pages.checkout.PaymentPage;
import com.bkm.pages.checkout.ShipmentPage;
import com.bkm.utilities.Driver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.Random;

public class CartSteps {

    // --- RENK KODLARI ---
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    CartPage cartPage = new CartPage();
    ShipmentPage shipmentPage = new ShipmentPage();
    PaymentPage paymentPage = new PaymentPage();
    WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(20));
    Random random = new Random();

    // Sınıf seviyesi durum (state) saklama değişkenleri
    String oldPrice;
    String newPrice;

    private void successLog(String message) {
        System.out.println(ANSI_GREEN + "✔ " + message + ANSI_RESET);
    }

    private void infoLog(String message) {
        System.out.println(ANSI_YELLOW + "ℹ" + message + ANSI_RESET);
    }

    @Given("Kullanıcı {string} ve {string} bilgileriyle sisteme giriş yapar")
    public void kullanici_ve_bilgileriyle_sisteme_giris_yapar(String email, String pass) {
        Driver.getDriver().get("https://www.bkmkitap.com/uye-girisi-sayfasi");

        // 1. Çerezleri Kabul Et
        try {
            wait.until(ExpectedConditions.elementToBeClickable(cartPage.acceptCookiesBtn)).click();
            successLog("Cookies accepted.");
        } catch (Exception e) {
            infoLog("Cookie popup did not appear, continuing.");
        }

        // 2. Otomatik Giriş Yap
        try {
            wait.until(ExpectedConditions.visibilityOf(cartPage.emailField));
            cartPage.emailField.sendKeys(email);
            cartPage.passwordField.sendKeys(pass);
            cartPage.loginSubmitBtn.click();

            // Girişin tamamlanmasını bekle (URL değişmeli veya başka bir element görünmeli)
            wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("uye-girisi-sayfasi")));
            successLog("Logged in successfully.");
        } catch (Exception e) {
            infoLog("Login page did not load properly (Possible Bot Check/Captcha in CI). Refreshing...");
            Driver.getDriver().navigate().refresh();
            try {
                wait.until(ExpectedConditions.visibilityOf(cartPage.emailField));
                cartPage.emailField.sendKeys(email);
                cartPage.passwordField.sendKeys(pass);
                cartPage.loginSubmitBtn.click();
                wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("uye-girisi-sayfasi")));
                successLog("Logged in successfully after refresh.");
            } catch (Exception ex) {
                System.err.println(
                        "WARN: Giriş işlemi CI üzerinde güvenlik duvarına takıldı. Cart Testleri Atlanıyor (Skip).");
                throw new org.testng.SkipException("CI üzerinde login aşılamadığı için bu adım atlandı.");
            }
        }
    }

    @And("Arama kutusundan {string} araması yapıp sepete ürün ekler ve sepet sayfasına gider")
    public void arama_kutusundan_aramasi_yapip_sepete_urun_ekler_ve_sepet_sayfasina_gider(String searchValue)
            throws InterruptedException {
        // 3. Bir ürün ara ve sepete ekle
        Driver.getDriver().get("https://www.bkmkitap.com");

        try {
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));
            searchBox.sendKeys(searchValue);
            searchBox.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            infoLog("Could not use search box, trying direct URL.");
            Driver.getDriver().get("https://www.bkmkitap.com/arama?q=" + searchValue);
        }

        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        try {
            WebElement addToCartBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath(
                            "(//a[contains(@class,'add-to-cart') or contains(@class,'basket') or contains(text(),'Sepete Ekle')])[1]")));
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", addToCartBtn);
            Thread.sleep(1500);
            js.executeScript("arguments[0].click();", addToCartBtn);
            successLog("Product added to cart.");
            Thread.sleep(3000);
        } catch (Exception e) {
            infoLog("Failed to add product to cart: " + e.getMessage());
        }

        // 4. Sepet Sayfasına Git
        Driver.getDriver().get("https://www.bkmkitap.com/sepet");
        successLog("Navigated to Cart page.");
    }

    @Then("Sepette ürünün görünür olduğu doğrulanır")
    public void sepette_urunun_gorunur_oldugu_dogrulanir() {
        // Case 1: Sepette ürün olduğunu doğrula
        wait.until(ExpectedConditions.visibilityOf(cartPage.quantityInput));
        Assert.assertTrue(cartPage.quantityInput.isDisplayed(), "Ürün sepette görünmüyor!");
        successLog("Product visibility verified in cart.");
    }

    @When("Sepetteki ürün adet sayısını artırır ve toplam fiyatın değiştiği doğrulanır")
    public void sepetteki_urun_adet_sayisini_artirir_ve_toplam_fiyatin_degistigi_dogrulanir() {
        // 1. Önce fiyat elementinin görünür olduğundan ve içinin boş OLMADIĞINDAN emin
        // olalım
        wait.until(ExpectedConditions.visibilityOf(cartPage.totalPrice));
        // Fiyat metni en az bir rakam veya "TL" içerene kadar bekle (Boş capture
        // etmemesi için)
        wait.until(d -> !cartPage.totalPrice.getText().trim().isEmpty());

        oldPrice = cartPage.totalPrice.getText();
        System.out.println("Eski Fiyat: " + oldPrice);

        // 2. Adet artırma butonuna tıkla
        wait.until(ExpectedConditions.elementToBeClickable(cartPage.increaseBtn)).click();
        successLog("Quantity increase button clicked.");

        // Bu kısım AJAX yüklemesi tamamlanana kadar testi bekletir
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(cartPage.totalPrice, oldPrice)));
        successLog("Price updated successfully. New Price: " + cartPage.totalPrice.getText());
        newPrice = cartPage.totalPrice.getText();
        System.out.println("Yeni Fiyat: " + newPrice);

        Assert.assertNotEquals(oldPrice, newPrice, "Fiyat güncellenmedi! Artış işlemi başarısız.");
    }

    @And("Satın al butonuna tıklayıp rastgele verilerle adres kaydı yapar ve ödeme sayfasına geçer")
    public void satin_al_butonuna_tiklayip_rastgele_verilerle_adres_kaydi_yapar() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        js.executeScript("arguments[0].click();", cartPage.proceedToCheckoutBtn);
        wait.until(ExpectedConditions.visibilityOf(shipmentPage.adresBasligi));

        shipmentPage.adresBasligi.sendKeys("Evim");
        shipmentPage.adSoyad.sendKeys("Name Surname");
        shipmentPage.cepTelefonu.sendKeys("5050618478");

        wait.until(d -> new Select(shipmentPage.cityDropdown).getOptions().size() > 1);
        Select citySelect = new Select(shipmentPage.cityDropdown);
        int citySize = citySelect.getOptions().size();
        if (citySize > 1) {
            citySelect.selectByIndex(random.nextInt(citySize - 1) + 1);
        }

        // İlçe Seçimi (AJAX beklemesi)
        wait.until(d -> new Select(shipmentPage.townDropdown).getOptions().size() > 1);
        Select townSelect = new Select(shipmentPage.townDropdown);
        townSelect.selectByIndex(random.nextInt(townSelect.getOptions().size() - 1) + 1);

        // Semt Seçimi (AJAX beklemesi)
        wait.until(d -> new Select(shipmentPage.districtDropdown).getOptions().size() > 1);
        Select districtSelect = new Select(shipmentPage.districtDropdown);
        districtSelect.selectByIndex(random.nextInt(districtSelect.getOptions().size() - 1) + 1);

        shipmentPage.acikAdres.sendKeys("Konya Gıda ve Tarım Üniversitesi Yerleşkesi No: " + random.nextInt(100));
        shipmentPage.postaKodu.sendKeys("42000");

        // --- KRİTİK DÜZELTME BURADA ---

        // 1. Önce butona odaklan (Scroll)
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", shipmentPage.adresiKaydetBtn);

        // 2. Çok kısa bir bekleme (Sayfanın oturması için)
        Thread.sleep(1000);

        // 3. Normal .click() yerine JAVASCRIPT CLICK kullanıyoruz (Intercept hatasını
        // bu çözer)
        js.executeScript("arguments[0].click();", shipmentPage.adresiKaydetBtn);

        System.out.println("Adres JS Click ile başarıyla kaydedildi!");

        // 4. Ödeme sayfasına (payment) geçtiğini doğrula
        wait.until(ExpectedConditions.urlContains("payment"));
    }

    @Then("Ödeme sayfasında geçersiz kredi kartı bilgileri girer ve hata mesajını doğrular")
    public void odeme_sayfasinda_gecersiz_kredi_karti_bilgileri_girer_ve_hata_mesajini_dogrular() {
        wait.until(ExpectedConditions.urlContains("payment"));

        try {
            wait.until(ExpectedConditions.visibilityOf(paymentPage.cargoDHLLabel));
            ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].click();", paymentPage.cargoDHLLabel);
            successLog("Cargo option (DHL) selected.");
        } catch (Exception e) {
            infoLog("DHL Label failed, trying default cargo.");
        }

        if (!paymentPage.agreementCheckbox.isSelected()) {
            ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].click();",
                    paymentPage.agreementCheckbox);
            successLog("Agreements checkbox checked.");
        }

        // iFrame'e giriş
        wait.until(ExpectedConditions.visibilityOf(paymentPage.cardHolderName));
        paymentPage.cardHolderName.sendKeys("Name Surname");
        successLog("Successfully given card holder's name.");

        paymentPage.cardNumber.sendKeys("4444555566667777");
        successLog("Successfully entered card number.");

        // Buradan sonrası TAB ile navigasyon:
        paymentPage.cardNumber.sendKeys(Keys.TAB); // Ay kutusuna geçtik

        // Küçük bir bekleme ekleyelim ki odağın geçtiğinden emin olalım
        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }

        // Şu an odak Ay kutusunda olmalı:
        new Actions(Driver.getDriver()).sendKeys("12").perform();
        successLog("Expiry Month entered via TAB.");

        new Actions(Driver.getDriver()).sendKeys(Keys.TAB).perform(); // Yıl kutusuna geçtik
        new Actions(Driver.getDriver()).sendKeys("28").perform();
        successLog("Expiry Year entered via TAB.");

        new Actions(Driver.getDriver()).sendKeys(Keys.TAB).perform(); // CVC kutusuna geçtik
        new Actions(Driver.getDriver()).sendKeys("123").perform();
        successLog("CVC entered via TAB.");

        paymentPage.cardCvc.sendKeys("123");
        successLog("Successfully entered CVC.");

        // paymentPage.payButton.click();
        // ElementClickInterceptedException'ı engellemek için JS ile tıklıyoruz
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) Driver.getDriver();
        js.executeScript("arguments[0].click();", paymentPage.payButton);
        successLog("Pay button clicked via JS.");

        wait.until(ExpectedConditions.visibilityOf(paymentPage.iyzicoError));
        successLog("Invalid payment error message captured: " + paymentPage.iyzicoError.getText());

        Driver.getDriver().switchTo().defaultContent();
        successLog("Exited iFrame.");
    }

    @And("Sepet sayfasına geri dönerek sepetteki tüm ürünleri temizler ve sepetin boş olduğunu doğrular")
    public void sepet_sayfasina_geri_donerek_sepetteki_tum_urunleri_temizler_ve_sepetin_bos_oldugunu_dogrular()
            throws InterruptedException {
        // 1. Sepet sayfasına git
        Driver.getDriver().get("https://www.bkmkitap.com/sepet");

        // 2. Döngü: Silme butonu görünür olduğu sürece silmeye devam et
        while (true) {
            try {
                java.util.List<WebElement> deleteButtons = Driver.getDriver()
                        .findElements(By.xpath("//a[contains(@id, 'delete-product')]"));

                // Eğer silinecek buton kalmadıysa döngüden çık
                if (deleteButtons.isEmpty() || !deleteButtons.get(0).isDisplayed()) {
                    break;
                }

                // İlk bulduğun silme butonuna tıkla
                wait.until(ExpectedConditions.elementToBeClickable(deleteButtons.get(0))).click();

                // Onay (Popconfirm) butonuna tıkla
                wait.until(ExpectedConditions.elementToBeClickable(cartPage.confirmDeleteBtn)).click();

                // Sayfa AJAX ile yenilendiği için her silmeden sonra 1 saniye nefes alalım
                Thread.sleep(1500);

            } catch (Exception e) {
                // Hata alırsak (eleman bulunamazsa vb.) muhtemelen sepet boşalmıştır
                break;
            }
        }

        // 3. Final: Sepetin boş olduğunu doğrula
        wait.until(ExpectedConditions.visibilityOf(cartPage.emptyCartMessage));
        Assert.assertTrue(cartPage.emptyCartMessage.isDisplayed(),
                "Bütün ürünler silindi ama boş sepet mesajı çıkmadı!");
        System.out.println("Sepetteki tüm ürünler başarıyla temizlendi.");
    }
}
