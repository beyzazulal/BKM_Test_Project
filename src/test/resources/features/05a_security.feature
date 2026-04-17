@security
Feature: Fonksiyonel Olmayan Testler - Güvenlik ve Oturum

  Scenario: Kullanıcı oturumunun güvenli bir şekilde sonlandırılması
    Given Kullanıcı "https://www.bkmkitap.com/uye-girisi-sayfasi" adresine gider
    When E-posta alanına "internetkopuk@mail.com" yazar
    And Şifre alanına "Sabır123!" yazar
    And Giriş Yap butonuna tıklar
    Then Kullanıcının başarıyla giriş yaptığı doğrulanmalı
    And Kullanıcı "Çıkış Yap" butonuna tıklar
    Then Kullanıcının hesaptan çıktığı ve login sayfasına yönlendirildiği doğrulanmalı
    And Tarayıcı geri butonuna basıldığında oturumun kapalı kaldığı (cache kontrolü) görülmeli
