@search
Feature: BKM Kitap Arama (Search) Modülü

  Scenario Outline: Farklı veri tipleriyle arama yapabilme (Boundary, Wildcard)
    Given Kullanıcı "https://www.bkmkitap.com" adresine gider
    When Arama kutusuna "<aranacakKelime>" yazar
    And Arama butonuna veya Enter tuşuna basar
    Then Arama sonuçlarında "<beklenenKelime>" içeriğinin listelendiği doğrulanmalı

    Examples:
      | aranacakKelime | beklenenKelime |
      | Java           | Java           |
      | *Kitap         | Kitap          |
      | 1234567890     | Bulunamadı     |

  Scenario: Kategori bazlı arama yapılması
    Given Kullanıcı "https://www.bkmkitap.com" adresine gider
    When Kategori menüsünden "Edebiyat" seçer
    And Arama kutusuna "Roman" yazar
    And Arama butonuna veya Enter tuşuna basar
    Then Çıkan sonuçların "Edebiyat" kategorisine ait olduğu görülmeli
