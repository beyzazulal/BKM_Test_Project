Feature: Arama Altyapısı Wildcard ve Performans Kontrolleri

  @wildcard_search @search
  Scenario: Joker karakterlerle (%%%%) arama yapıldığında sistemin Full Table Scan zafiyetine düşmemesi ve çökmemesi
    Given BKM Kitap anasayfasına gidilir
    When Arama çubuğuna "%%%%" joker karakterleri girilir ve süre ölçülür
    Then Sistem çökmeleri için sayfa kaynağında kontrol yapılır
    And Arama yanıt süresinin 10000 milisaniyeden kısa olduğu doğrulanır
    And Sonuç davranışı kontrol edilir
