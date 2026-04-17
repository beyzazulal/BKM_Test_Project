@login
Feature: BKM Kitap Kullanıcı Girişi Modülü

  Scenario: Doğru bilgilerle giriş yapabilme
    Given Kullanıcı "https://www.bkmkitap.com/uye-girisi-sayfasi" adresine gider
    When E-posta alanına "internetkopuk@mail.com" yazar
    And Şifre alanına "Sabır123!" yazar
    And Giriş Yap butonuna tıklar
    Then Kullanıcının başarıyla giriş yaptığı doğrulanmalı

  Scenario Outline: Yanlış bilgilerle giriş yapamama (Data Driven)
    Given Kullanıcı "https://www.bkmkitap.com/uye-girisi-sayfasi" adresine gider
    When E-posta alanına "<email>" yazar
    And Şifre alanına "<password>" yazar
    And Giriş Yap butonuna tıklar
    Then Ekranda hata mesajı görülmeli

    Examples:
      | email                      | password       |
      | test_olmayan_mail@mail.com | yanlissifre123 |
      |                            | BosMailSifreli1|
      | dogrumail@mail.com         |                |

  Scenario: Boş alanlarla giriş yapmaya çalışma (Validation Check)
    Given Kullanıcı "https://www.bkmkitap.com/uye-girisi-sayfasi" adresine gider
    When Alanları tamamen temizler
    And Normal şekilde Giriş Yap butonuna tıklar
    Then Sistemin boş alanlarla girişi reddettiği doğrulanmalı

  Scenario: Geçersiz E-Posta formatı ile giriş yapmaya çalışma
    Given Kullanıcı "https://www.bkmkitap.com/uye-girisi-sayfasi" adresine gider
    When E-posta alanına "testmail.com" yazar
    And Şifre alanına "Sifre123" yazar
    And Giriş Yap butonuna tıklar
    Then Geçersiz format uyarısının çıktığı doğrulanmalı

  Scenario: Şifre görünürlüğü ikonunun çalışması
    Given Kullanıcı "https://www.bkmkitap.com/uye-girisi-sayfasi" adresine gider
    When Şifre alanına "GizliSifre123" yazar
    And Şifre göster ikonuna tıklar
    Then Şifrenin görünür hale geldiği doğrulanmalı

  Scenario: Facebook ile giriş yönlendirmesi
    Given Kullanıcı "https://www.bkmkitap.com/uye-girisi-sayfasi" adresine gider
    When Facebook ile giriş butonuna tıklar
    Then Kullanıcının Facebook sayfasına yönlendirildiği doğrulanmalı

  Scenario: Şifremi unuttum akışı
    Given Kullanıcı "https://www.bkmkitap.com/uye-girisi-sayfasi" adresine gider
    When Şifremi unuttum linkine tıklar
    And Hatırlatma e-posta alanına "internetkopuk@mail.com" yazar
    And Şifremi Hatırlat butonuna tıklar
    Then Şifre sıfırlama olayının gerçekleştiği doğrulanmalı

  Scenario: Telefon numarası ile giriş yapabilme
    Given Kullanıcı "https://www.bkmkitap.com/uye-girisi-sayfasi" adresine gider
    When Telefon numarası alanına "5065889272" yazar
    And Şifre alanına "Sabır123!" yazar
    And Giriş Yap butonuna tıklar
    Then Mobil hesaptan girişin başarılı olduğu veya yakalandığı doğrulanmalı
