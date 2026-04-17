package com.bkm.pages.checkout;

import com.bkm.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PaymentPage {
    public PaymentPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    // --- Kargo Seçimi ---

    // HEPSİJET Label'ı
    @FindBy(xpath = "//label[@for='cargo-item-input-0']")
    public WebElement cargoHepsijetLabel;

    // DHL Label'ı
    @FindBy(xpath = "//label[@for='cargo-item-input-1']")
    public WebElement cargoDHLLabel;

    // --- Sözleşme Onayı ---
    @FindBy(id = "input-iyzico-agreements")
    public WebElement agreementCheckbox;

    // --- YENİ: Iyzico Manuel Kart Formu Elemanları ---
    @FindBy(id = "ccname")
    public WebElement cardHolderName; // Kart Üzerindeki Ad Soyad

    @FindBy(id = "ccnumber")
    public WebElement cardNumber; // Kart Numarası

    @FindBy(id = "ccexpmonth")
    public WebElement cardExpiryMonth; // AY
    @FindBy(id = "ccexpyear")
    public WebElement cardExpiryYear; // YIL

    @FindBy(id = "cccvc")
    public WebElement cardCvc; // Güvenlik Kodu (CVC)

    @FindBy(id = "iyz-payment-button")
    public WebElement payButton; // Öde Butonu

    @FindBy(className = "css-14ltnoo-ErrorWrapper")
    public WebElement iyzicoError; // Hata Mesajı Alanı
}