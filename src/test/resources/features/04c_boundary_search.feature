Feature: Arama Altyapısı Sınır Değer (Boundary) Kontrolleri

  @boundary_search @search
  Scenario: Arama çubuğuna sınırın üzerinde (255+) karakter girildiğinde sistemin tepkisi
    Given BKM Kitap anasayfasına gidilir
    When Arama çubuğuna 260 karakter uzunluğunda "A" metni girilir ve aranır
    Then Sistemin HTTP hata kodlarıyla çökmediği doğrulanır
    And Arama motorunun mantıksal davranışları (Bug tespiti) loglara yazılır
