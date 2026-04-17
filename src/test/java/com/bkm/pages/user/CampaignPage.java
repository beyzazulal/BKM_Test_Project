package com.bkm.pages.user;

import com.bkm.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class CampaignPage {
    public CampaignPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    // Sayfadaki TÜM resimleri liste olarak yakalıyoruz
    @FindBy(tagName = "img")
    public List<WebElement> tumResimler;

    // Ana sayfadaki büyük kampanya bannerları
    @FindBy(css = ".swiper-slide img")
    public List<WebElement> kampanyaBannerlari;

    @FindBy(css = ".swiper-button-next") // Sağa ok butonu
    public WebElement sliderNextButton;

    @FindBy(css = ".swiper-slide-active img") // O an ekranda görünen aktif resim
    public WebElement activeSliderImage;
}