Feature: Arama Altyapısı Güvenlik (XSS) Kontrolleri

  @security_search @search
  Scenario: Arama çubuğuna XSS payload'u girildiğinde sistemin zafiyet vermemesi
    Given BKM Kitap anasayfasına gidilir
    When Arama çubuğuna zararlı XSS payload'u "<script>alert('Hack')</script>" girilir ve aranır
    Then Sistemde güvenlik açığı olmadığı ve Alert fırlamadığı doğrulanır
