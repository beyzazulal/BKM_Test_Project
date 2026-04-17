Feature: Kampanya ve Ana Sayfa UI Kontrolleri

  @campaign
  Scenario: Ana sayfadaki resimlerin kırık (broken) link kontrolü
    Given Kullanıcı ana sayfaya gider ve çerezleri kabul eder
    When Sayfadaki resimler yüklendiğinde
    Then Sayfadaki ilk 50 resmin kırık olup olmadığı HTTP kodlarıyla kontrol edilir

  @campaign
  Scenario: Ana sayfa slider'ının çalışabilirlik kontrolü
    Given Kullanıcı ana sayfaya gider
    When Slider'daki ilk resmin bağlantısı kaydedilir
    And Slider ileri butonuna JavaScript ile tıklanır
    Then Slider resminin değiştiği doğrulanır

  @campaign
  Scenario: Olmayan kampanya sayfasına gidildiğinde hata alınması
    Given Kullanıcı "https://www.bkmkitap.com/kampanyalar/olmayan-sayfa-999" adresine gider
    Then Sayfa kaynağında 404 veya ulaşılamıyor hatası görülmelidir

  @campaign
  Scenario: Mobil görünümde slider butonunun diğer elementler tarafından kesilmesi (Responsive)
    Given Ekran boyutu mobil cihaz boyutlarına getirilir
    When Kullanıcı ana sayfaya gider
    Then Slider butonuna normal tıklama yapıldığında ElementClickInterceptedException alınmalıdır
