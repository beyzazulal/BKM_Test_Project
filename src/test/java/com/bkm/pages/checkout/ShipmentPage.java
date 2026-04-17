package com.bkm.pages.checkout;

import com.bkm.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ShipmentPage {
    public ShipmentPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(id = "is_company_active") // Fatura Türü (Bireysel/Kurumsal)
    public WebElement faturaTuru;

    @FindBy(id = "title") // Adres Başlığı
    public WebElement adresBasligi;

    @FindBy(id = "fullname") // Ad Soyad
    public WebElement adSoyad;

    @FindBy(id = "city_code") // İl Seçimi
    public WebElement cityDropdown;

    @FindBy(id = "town_code") // İlçe Seçimi
    public WebElement townDropdown;

    @FindBy(id = "district_code") // Semt Seçimi
    public WebElement districtDropdown;

    @FindBy(id = "address") // Açık Adres (Textarea)
    public WebElement acikAdres;

    @FindBy(id = "post_code") // Posta Kodu
    public WebElement postaKodu;

    @FindBy(id = "mobile_phone") // Cep Telefonu
    public WebElement cepTelefonu;

    @FindBy(xpath = "//button[@type='submit']//span[text()='Adresi Kaydet']/..")
    public WebElement adresiKaydetBtn;
}