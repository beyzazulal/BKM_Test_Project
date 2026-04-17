package com.bkm.pages.checkout;

import com.bkm.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CartPage {
    public CartPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    // --- Kendi Sayfan İçin Giriş Elemanları ---
    @FindBy(id = "ug-email")
    public WebElement emailField;

    @FindBy(id = "ug-password")
    public WebElement passwordField;
    // Cookies için
    @FindBy(className = "cc-nb-okagree")
    public WebElement acceptCookiesBtn;

    @FindBy(id = "ug-submit-btn") // Giriş yap butonu
    public WebElement loginSubmitBtn;

    // --- Sepet Elemanları ---
    @FindBy(xpath = "//input[contains(@id, 'qty')]")
    public WebElement quantityInput;

    @FindBy(xpath = "//span[contains(@id, 'qty-plus')]")
    public WebElement increaseBtn;
    // cart-price-btn sınıfına sahip div içindeki fw-bold yazısını bulur
    @FindBy(xpath = "//div[contains(text(),'Genel Toplam')]/following-sibling::div")
    public WebElement totalPrice;

    // 4. Sil (Remove) Butonu (id="delete-product-9629731" için dinamik çözüm)
    @FindBy(xpath = "//a[contains(@id, 'delete-product')]")
    public WebElement removeProductBtn;

    @FindBy(className = "t-popconfirm-cancel-btn")
    public WebElement confirmDeleteBtn;

    @FindBy(id = "cart-buy-btn")
    public WebElement proceedToCheckoutBtn;

    @FindBy(xpath = "//*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'sepetiniz')] | //*[contains(@class, 'empty')]")
    public WebElement emptyCartMessage;
}