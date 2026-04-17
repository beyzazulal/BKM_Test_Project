package com.bkm.pages.user;

import com.bkm.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RegisterPage {
    public RegisterPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(id = "name")
    public WebElement adKutusu;

    @FindBy(id = "surname")
    public WebElement soyadKutusu;

    @FindBy(id = "email")
    public WebElement emailKutusu;

    @FindBy(id = "password")
    public WebElement sifreKutusu;

    @FindBy(id = "password_again")
    public WebElement sifreTekrarKutusu;

    @FindBy(id = "confirm_contract")
    public WebElement sozlesmeOnay;

    // RegisterPage içindeki buton kısmını bununla değiştir:
    @FindBy(xpath = "//button[contains(text(),'KAYIT OL')] | //button[@id='submit-button'] | //button[@type='submit']")
    public WebElement uyeOlButonu;

    @FindBy(id = "tel") // BKM Kitap'ta genellikle 'tel' veya 'gsm' olur, inceleyip kontrol etmelisin
    public WebElement telefonKutusu;
}