Feature: Arama ve Filtre Durumunun Korunması (State Preservation)

  @state_preservation @search
  Scenario: Ürün detayından geri gelindiğinde arama kelimesi ve filtrelerin korunması
    Given BKM Kitap anasayfasına gidilir
    When Arama çubuğunda "Roman" kelimesi aranır
    And Sol menüden "İş Bankası" filtresi uygulanır
    And Sonuçlardan listelenen ilk ürüne tıklanır
    And Tarayıcıda geri tuşuna basılır
    Then Arama kutusunda aranan kelimenin korunduğu doğrulanır
    And Uygulanan filtrenin seçili kaldığı doğrulanır
