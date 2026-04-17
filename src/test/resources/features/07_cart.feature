@cart
Feature: BKM Kitap Sepet (Cart) ve Ödeme Modülü

  Scenario: Ürün arayıp sepete ekleme ve sepeti kontrol etme
    Given Kullanıcı "https://www.bkmkitap.com" adresine gider
    When Arama kutusuna "Selenium" yazar
    And Arama butonuna veya Enter tuşuna basar
    And Sonuçlardan ilk ürünün "Sepete Ekle" butonuna tıklar
    Then "Ürün sepete eklendi" bildiriminin çıktığı doğrulanmalı
    When Kullanıcı "Sepetim" ikonuna tıklar
    Then Sepet sayfasında eklenen ürünün yer aldığı doğrulanmalı
    And Siparişi Tamamla butonuna tıklandığında ödeme sayfasına geçilebilmeli

  @cart_e2e @checkout
  Scenario: Kullanıcının sepete ürün eklemesi, miktar artırması, adres girmesi ve ödeme hatası alması (E2E)
    Given Kullanıcı "aycandemwsw@gmail.com" ve "hido123kido123" bilgileriyle sisteme giriş yapar
    And Arama kutusundan "kitap" araması yapıp sepete ürün ekler ve sepet sayfasına gider
    Then Sepette ürünün görünür olduğu doğrulanır
    When Sepetteki ürün adet sayısını artırır ve toplam fiyatın değiştiği doğrulanır
    And Satın al butonuna tıklayıp rastgele verilerle adres kaydı yapar ve ödeme sayfasına geçer
    Then Ödeme sayfasında geçersiz kredi kartı bilgileri girer ve hata mesajını doğrular
    And Sepet sayfasına geri dönerek sepetteki tüm ürünleri temizler ve sepetin boş olduğunu doğrular
