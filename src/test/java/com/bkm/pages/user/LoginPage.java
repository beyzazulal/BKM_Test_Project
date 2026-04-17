package com.bkm.pages.user;

import com.bkm.utilities.Driver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    // Constructor: Hem parametreli hem parametresiz versiyonu ekleyerek
    // her türlü kullanımda hata almanı engelledim.
    public LoginPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    // --- GİRİŞ FORMU ELEMENTLERİ ---

    @FindBy(xpath = "//a[contains(@href, 'uye-girisi') or @data-toggle='login-form']")
    public WebElement profilIkonu;

    @FindBy(id = "ug-email")
    public WebElement emailKutusu;

    @FindBy(id = "ug-password")
    public WebElement sifreKutusu;

    /*@FindBy(xpath = "//button[@id='login-btn-287' or contains(@class,'btn-primary')]")
    public WebElement girisButonu;*/

    // Hem ID'den hem de butonun içindeki "Giriş Yap" yazısından yakalıyoruz
    @FindBy(xpath = "//button[@id='ug-submit-btn' or contains(text(),'Giriş Yap')]")
    public WebElement girisButonu;

    @FindBy(xpath = "//*[contains(text(), 'Hatalı mail veya şifre girdiniz.')]")
    public WebElement hataMesaji;

    @FindBy(xpath = "//i[contains(@class, 'ti-eye')]")
    public WebElement sifreGosterIkonu;

    // --- SOSYAL LOGİN VE YARDIMCI LİNKLER ---

    @FindBy(xpath = "//a[contains(@href, 'facebook')]")
    public WebElement facebookButonu;

    @FindBy(xpath = "//a[contains(text(),'Şifremi Unuttum')]")
    public WebElement sifremiUnuttumLinki;

    // --- ŞİFRE HATIRLATMA FORMU ---

    @FindBy(xpath = "//input[@placeholder='E-posta adresinizi giriniz']")
    public WebElement sifreHatirlatEmailKutusu;

    @FindBy(xpath = "//button[@type='submit' and contains(@class, 'btn')]")
    public WebElement sifremiHatirlatButonu;

    // --- TELEFONLA GİRİŞ SEKEMESİ ---

    @FindBy(xpath = "//div[contains(translate(text(), 'TELEFON', 'telefon'), 'telefon')]")
    public WebElement telefonSekmesi;

    @FindBy(xpath = "//input[@placeholder='Telefon numaranızı giriniz']")
    public WebElement telefonKutusu;
}