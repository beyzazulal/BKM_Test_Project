Feature: Arama Kategori Filtreleme Kontrolü

  @search_category @search
  Scenario: Geçerli bir kategori filtresi uygulandığında sonuçların güncellenmesi
    Given BKM Kitap anasayfasına gidilir
    When Arama bölümüne "Harry Potter" kelimesi yazılır ve aranır
    And Sol taraftaki menüden "Çocuk Kitapları" kategorisi seçilir
    Then Sistemin filtreyi uyguladığı URL üzerinden doğrulanır
