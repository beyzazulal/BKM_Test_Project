@register
Feature: BKM Kitap Yeni Kullanıcı Kayıt (Register) Modülü

  Scenario: Dinamik verilerle E2E Kayıt, Otomatik Giriş ve Sepete Ürün Ekleme
    Given Kullanıcı "https://www.bkmkitap.com/uye-kayit" adresine gider ve çerezleri temizler
    When Rastgele bilgilerle kayıt formunu doldurur ve sözleşmeleri onaylar
    And Kayıt Ol butonuna tıklar
    Then Sistemin kaydı engellemediği ve oturum açtığı akıllıca doğrulanmalı
    When Başarılı kayıt sonrası anasayfada "Java" araması yapar
    And Arama sonuçlarından ilk ürünü sepete ekler
    Then Sepet sayfasına giderek ürünün sepette olduğunu doğrular
