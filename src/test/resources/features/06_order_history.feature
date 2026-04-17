@orders
Feature: BKM Kitap Sipariş Geçmişi Modülü

  Scenario: Kullanıcının sipariş geçmişini görüntüleyebilmesi
    Given Kullanıcı "https://www.bkmkitap.com/uye-girisi-sayfasi" adresine gider
    When E-posta alanına "internetkopuk@mail.com" yazar
    And Şifre alanına "Sabır123!" yazar
    And Giriş Yap butonuna tıklar
    Then Kullanıcının başarıyla giriş yaptığı doğrulanmalı
    When Kullanıcı profil menüsünden "Siparişlerim" butonuna tıklar
    Then Kullanıcının sipariş geçmişi sayfasında eski siparişlerini listeleyebildiği görülmeli
