package com.bkm.pages.user;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderHistoryPage {

    public OrderHistoryPage(WebDriver driver) {
    }

    public By getOrdersMenuLink() {
        return By.xpath("//a[contains(text(), 'Siparişlerim')]");
    }

    public By getOrderHistoryTable() {
        return By.cssSelector(".order-history-list, #orders-table");
    }
}