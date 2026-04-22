# 📚 BKM Kitap - E-Ticaret Test Otomasyon Projesi Rehberi

Bu proje, BKM Kitap e-ticaret sitesinin uçtan uca (End-to-End) test otomasyonunu gerçekleştirmek amacıyla 3 kişilik bir QA (Kalite Güvence) ekibi tarafından geliştirilecektir.

## 🛠 1. Teknoloji Yığını (Tech Stack)
* **Programlama Dili:** Java (JDK 11 veya üzeri)
* **Otomasyon Aracı:** Selenium WebDriver
* **Test Çerçevesi (Framework):** TestNG
* **Proje Yönetim Aracı:** Maven
* **Sürüm Kontrol Sistemi:** Git ve GitHub
* **Geliştirme Ortamı (IDE):** IntelliJ IDEA (Community Edition)

---

## 🏗 2. Proje Mimarisi (Page Object Model - POM)
Projede sürdürülebilir, bakımı kolay ve okunabilir testler yazmak için **Page Object Model (POM)** tasarım deseni kullanılacaktır. Her web sayfası için ayrı bir Java sınıfı oluşturulacak ve o sayfaya ait web elementler (Locator'lar) ve metotlar bu sınıflarda tutulacaktır.

### Klasör / Paket Yapısı (Maven)
3 kişinin aynı anda çalışırken birbirini ezmemesi (conflict yaşamaması) için paketleri de kendi içlerinde modüllere ayırdık.

```text
bkm-test-project/
├── pom.xml                                   # Proje kütüphanelerinin (Selenium, TestNG vb.) tanımlandığı dosya
├── src/
│   ├── test/
│   │   ├── java/
│   │   │   └── com/bkm/                      # Projenin ana paketi (GroupId)
│   │   │       ├── base/                     # Test yürütme ayarları
│   │   │       │   └── TestBase.java         # Driver başlatma/kapatma (@BeforeMethod - @AfterMethod) işlemlerinin olduğu ana sınıf
│   │   │       │
│   │   │       ├── pages/                    # PAGE Sınıfları (Sadece Web Elementleri ve sayfa işlevleri)
│   │   │       │   ├── user/                 # --> 1. Kişinin Sorumluluğu
│   │   │       │   │   ├── LoginPage.java
│   │   │       │   │   ├── RegisterPage.java
│   │   │       │   │   └── ProfilePage.java
│   │   │       │   ├── catalog/              # --> 2. Kişinin Sorumluluğu
│   │   │       │   │   ├── HomePage.java
│   │   │       │   │   ├── SearchResultsPage.java
│   │   │       │   │   └── ProductDetailsPage.java
│   │   │       │   └── checkout/             # --> 3. Kişinin Sorumluluğu
│   │   │       │       ├── CartPage.java
│   │   │       │       └── PaymentPage.java
│   │   │       │
│   │   │       ├── tests/                    # TEST Sınıfları (Senaryolar ve Doğrulamalar - Assertions)
│   │   │       │   ├── user_tests/           # --> 1. Kişinin Sorumluluğu
│   │   │       │   │   └── LoginTest.java
│   │   │       │   ├── catalog_tests/        # --> 2. Kişinin Sorumluluğu
│   │   │       │   │   └── SearchTest.java
│   │   │       │   └── checkout_tests/       # --> 3. Kişinin Sorumluluğu
│   │   │       │       └── CartTest.java
│   │   │       │
│   │   │       └── utilities/                # Projenin kalbi (Framework Çekirdeği)
│   │   │           ├── ConfigReader.java     # properties dosyasındaki verileri okuyan sınıf
│   │   │           ├── Driver.java           # Singleton model ile sistemin tek bir tarayıcı kullanmasını sağlayan sınıf
│   │   │           ├── ReusableMethods.java  # Ortak metodlar (Beklemeler, JS Click, Ekran Görüntüsü alma vb.)
│   │   │           └── ExcelUtil.java        # (Opsiyonel) Excel'den okunacak veriler için yardımcı araç
│   │   │
│   │   └── resources/                        # Test ortam konfigürasyonları ve dosyaları
│   │       ├── configuration.properties      # Değişkenler (browser=chrome, url=https://bkmkitap.com, valid_email=test@test.com)
│   │       ├── testData/                     # Data-Driven Testing için dış kaynaklı veriler
│   │       │   └── users.xlsx                # Farklı kullanıcı rolleri için test dataları
│   │       └── runners/                      # TestNG XML Dosyaları
│   │           ├── smoke_test.xml            # Kritik fonksiyonların test edileceği hızlı koşturma listesi
│   │           └── regression_test.xml       # Tüm sistemin baştan sona çalıştırıldığı liste
```

---

## 👥 3. Ekip Görev Dağılımı ve Test Modülleri
3 kişilik ekibin çakışma (conflict) yaşamadan, paralel ve verimli çalışabilmesi için proje 3 ana modüle ayrılmıştır:

### 👤 Kişi 1: Kullanıcı ve Hesap Yönetimi Modülü
* **Sorumluluk Alanı:** Üye olma, giriş yapma, profil işlemleri.
* **Test Senaryoları (Test Cases):**
  * Geçerli ve geçersiz bilgilerle Login / Register işlemleri.
  * Şifremi unuttum akışı.
  * Adres ekleme, silme ve düzenleme (Adres Defteri).
  * Profil bilgileri güncelleme.
* **Page Sınıfları:** `LoginPage`, `RegisterPage`, `ProfilePage`, `AddressPage`.

### 👤 Kişi 2: Ürün Keşfi ve Arama Modülü
* **Sorumluluk Alanı:** Sitede ürün arama, kategorilerde gezinme, filtreleme.
* **Test Senaryoları (Test Cases):**
  * Arama kutusu ile kelime bazlı, yazar bazlı geçerli/geçersiz ürün arama.
  * Kategori menülerini kullanarak alt kategorilere (Örn: Edebiyat -> Roman) ulaşma.
  * Ürün listeleme sayfasında fiyata göre sıralama, yayınevine göre filtreleme.
  * Ürün detay sayfasındaki fiyat, resim ve açıklama doğrulamaları.
* **Page Sınıfları:** `HomePage` (Arama barı ve üst menüler), `SearchResultsPage`, `CategoryPage`, `ProductDetailsPage`.

### 👤 Kişi 3: Sepet ve Satın Alma (Checkout) Modülü
* **Sorumluluk Alanı:** Sepet işlemleri, ödeme adımları.
* **Test Senaryoları (Test Cases):**
  * Ürün detaydan ve ürün listesinden sepete ürün ekleme.
  * Sepetteki ürün adetini artırma, azaltma ve ürünü silme.
  * Sepet alt toplam ve genel toplam (Kargo dahil) fiyat doğrulaması.
  * Satın alma (Checkout) akışı: Teslimat adresi girme, kargo seçme.
  * Ödeme (Payment) sayfasında geçersiz kart ile ödeme adımının hata verdiğini doğrulama.
* **Page Sınıfları:** `CartPage`, `CheckoutPage`, `PaymentPage`.

---

## ⚙️ 4. IntelliJ IDEA ile Projeye Başlama Adımları (Kurulum)

Ekipteki **SADECE 1 KİŞİ** bu adımları yapacak, GitHub'a yükleyecek ve diğer 2 kişi bu projeyi kendi bilgisayarına **Clone** (Çekme) işlemi yapacaktır.

1. **IntelliJ IDEA'yı Açın:** `New Project` (Yeni Proje) butonuna tıklayın.
2. **Proje Türü:** Sol menüden `Maven` seçin (veya Build system olarak Maven, dil olarak Java'yı seçin).
3. **Proje Bilgileri:**
   * Name: `bkm-automation-project`
   * GroupId: `com.bkm`
   * ArtifactId: `bkm-automation`
4. Proje açıldıktan sonra `pom.xml` dosyasını açın ve `<dependencies>` etiketleri arasına şu temel kütüphaneleri ekleyin:
   * **Selenium Java** (Web otomasyonu için)
   * **TestNG** (Testleri yönetmek ve assertion yapmak için)
   * **WebDriverManager** (İsteğe bağlı, tarayıcı sürücülerini otomatik indirmek için / veya Selenium 4.6+ yerleşik manager)
5. `src/test/java` altında `base`, `pages`, `tests`, `utilities` paketlerini (package) oluşturun.
6. Projeyi GitHub'a yükleyin.

---

## 🔀 5. Git ve GitHub Çalışma Stratejisi (Çok Önemli)

3 kişi aynı anda proje geliştireceği için Git kullanımı hayat kurtaracaktır.

1. **Asla Doğrudan Main/Master'a Kod Yazmayın!**
2. **Branch (Dal) Mantığı:** Herkes kendi modülü için ayrı bir branch açmalıdır.
   * `git checkout -b feature/login-module` (Kişi 1 için)
   * `git checkout -b feature/search-module` (Kişi 2 için)
   * `git checkout -b feature/checkout-module` (Kişi 3 için)
3. **Günlük Rutin (Pull & Push):**
   * Her gün koda başlamadan önce: `git pull origin main` (Ana projedeki güncellemeleri kendi dalınıza almak için).
   * Kodlama bittiğinde: Kendi branch'inize `Commit` ve `Push` yapın.
4. **Kod Birleştirme (Pull Request - PR):**
   * Bir modül testi bittiğinde, GitHub üzerinden `Pull Request (PR)` açın. 
   * Ekipteki diğer 2 kişi kodu inceler (Code Review). Hata yoksa `main` branch'ine `Merge` (Birleştirme) yapılır.

---

## 🚀 6. Test Yazarken Dikkat Edilecek Kurallar
* Kodlara veya test metotlarının içine asla bekleme ( `Thread.sleep(5000);` ) **yazılmamalıdır**. Onun yerine Selenium `WebDriverWait` (Explicit Wait) kullanılmalıdır.
* Tüm `Xpath`, `CssSelector`, `Id` gibi element bulucular sadece **Page** sınıflarında tutulacak, test class'larına asla element locator'ı sızmayacaktır.
* Site adresi, test kullanıcısı şifresi gibi veriler Java kodunun içinde değil, mutlaka `configuration.properties` dosyasında saklanmalıdır.

---

## 📊 7. Test Raporlama ve Sürekli Entegrasyon (CI/CD)

Projenin profesyonel standartlarda çalışabilmesi için otomasyon koşumlarının ve raporlamasının otomatikleştirilmiş olması gerekir.

### Test Raporlama (Reporting)
* Test koşum sonuçlarını (Başarılı/Başarısız/Atlanan testler) görselleştirmek için projeye **ExtentReports** veya **Allure Report** kütüphanesi entegre edilmelidir.
* Testler hata aldığında (Failed olduğunda) otomatik olarak ekran görüntüsü (Screenshot) alınıp bu raporlara eklenecek bir Listener (TestNG Listener) alt yapısı kurulmalıdır.

### CI/CD Pipeline (GitHub Actions / Jenkins)
* Testlerin her kod gönderiminde (push) veya her gün gece yarısı otomatik çalışması için bir **CI/CD Pipeline** oluşturulmuştur (Örn: `.github/workflows/ci.yml`).
* CI sunucularında Chrome Monitor (Arayüz) olmadan çalışacağı için testler `Headless` (Arka plan) modda veya sanal ekran (`xvfb`) ile koşturulacak şekilde ayarlanmalıdır.

---

## 🏃 8. Testleri Çalıştırma (Execution) Talimatları

Projeyi Clone'ladıktan sonra terminal üzerinden testleri başlatmak için gerekli komutlar:

* **Tüm Testleri Çalıştırmak için:**
  `mvn clean test`
* **Sadece Smoke Test (Kritik Testler) Çalıştırmak için:**
  `mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/runners/smoke_test.xml`
* **Belirli bir Browser ile Çalıştırmak için (Eğer parametrik ayarlandıysa):**
  `mvn clean test -Dbrowser=firefox`

---

## 🎯 9. Test Kapsamı (Scope of Project)
Bu otomasyon projesinin sınırları resmi bir test planı olarak aşağıdaki gibi belirlenmiştir:

### 🌐 Web Portalı Kapsamı (Scope of web portal)
BKM Kitap Web portalının testi bu test planının kapsamındadır. Aşağıdaki bileşenler ve fonksiyonlar test edilecektir:
* Üye kaydı (Registration) ve onay süreçleri
* Giriş yapma (Login) ve şifre kurtarma (Password recovery)
* Ürün arama (Search), filtreleme, özel/sınır (boundary) aramalar ve güvenlik (XSS/SQL injection) denetimleri
* Ana sayfa kampanya (Campaign) geçişleri ve kırık görsel (Broken images) testleri
* Sepet işlemleri (Ekleme, çıkarma, miktar güncelleme)
* Ödeme akışı (Payment) ve geçersiz kredi kartı doğrulama bildirimleri
* Kullanıcı profili menüsünde dolaşma

### 📱 Mobil Arayüz ve Uygulama Kapsamı (Scope of mobile application)
Native (Yerel) Android/iOS mobil uygulamalarının testi bu projenin **kapsam dışındadır (Out of scope)**. Ancak, web portalının **Mobil (Responsive) Görünümü** kapsam dahilindedir. Mobil arayüz boyutunda aşağıdaki bileşenler test edilecektir:
* Mobil cihaz boyutlarında (Örn: 400x700px) tıklama ve arayüz engellemeleri (Responsive Click Interruption)
* Mobil çözünürlük üzerinden elementlere (Slider, Menu, Butonlar) erişilebilirlik
* Mobil görünümde şifre işlemleri

### ⚙️ Admin Paneli Kapsamı (Scope of Admin part)
Yönetici/Admin panelinin test edilmesi bu test planının **kapsam dışındadır (Out of scope)**. Proje yalnızca son kullanıcı (Müşteri - Frontend) deneyimini hedefler. (Eğer kapsama dahil edilseydi şu adımları içerecekti: Admin girişi, ürün stoğu ekleme/çıkarma, sipariş onaylama ve kargo takip numarası girme işlemleri).

---

## 🚦 10. Giriş ve Çıkış Kriterleri (Entry and Exit Criteria)
Planlı bir çalışma sağlamak amacıyla kalite güvenlik limitleri şu şekilde belirlenmiştir:

**Giriş Kriterleri (Ne zaman başlanır?):**
* Otomasyon mimarisinin (POM tabanlı) ve klasör yapısının oluşturulması
* Test ortamının (bkmkitap.com) ve elementlerinin erişilebilir olması
* Ekiplerin GitHub Branch (Dal) yapılandırmasını tamamlamış olması

**Çıkış Kriterleri (Proje ne zaman "Bitti" sayılır?):**
* Planlanan Test Senaryolarının (Test Cases) **en az %95'inin** kodlanmış ve yürütülmüş olması
* Kritik (Critical/Blocker) seviyedeki **hiçbir hatanın "AÇIK (Open)" statüsünde bırakılmaması**
* Ana (Main/Master) branch'te çalışan uygulamanın **hiçbir testten kalmıyor (100% Pass) veya koşulan testlerin en az %80 başarı oranı** veriyor olması
* CI/CD Pipeline'ının hatasız çalışarak Rapor (Artifact) üretmesi

---

## ⚠️ 11. Risk Analizi ve Azaltma Stratejisi (Risk Analysis & Mitigation)

| Teknik / Proje Riskleri | Azaltma Stratejisi (Mitigation / Çözüm) |
| :--- | :--- |
| **Tarayıcı ve Driver Uyumsuzlukları** (Chrome sürümünün güncellenip testleri bozması) | Projede `Selenium Manager` (Selenium 4.6+) kullanılarak sürücü yönetimi otomatize edilmelidir. Böylece her çalıştırmada uyumlu driver otomatik indirilir. |
| **UI Değişiklikleri** (BKM Kitap arayüzünü güncellerse elementlerin bozulması) | Proje **Page Object Model (POM)** ile kodlandığı için sadece ilgili `Page` dosyasındaki *Locator* değişkenini değiştirmek tüm testleri düzeltecektir. |
| **Element Yüklenme Gecikmeleri (Timeouts)** | Statik beklemeler (`Thread.sleep`) yerine sadece element merkezli **Explicit Wait** (`WebDriverWait`) kullanılıp test stabil kılınacaktır. |
| **Stale Element / Intercept Hataları** (Üst üste binen div/popup'lar) | İlgili butona tıklanamadığı noktalarda projeye eklediğimiz özel **JavaScriptExecutor (JS Click)** metoduyla elemente direkt DOM üzerinden müdahale edilecektir. |

---

## 📦 12. Proje Çıktıları ve Teslimatlar (Test Deliverables)
Proje tamamlandığında proje sorumlusuna (veya değerlendirici hocalara) aşağıdaki iş paketleri teslim edilecektir:

1. **Yazılım Test Planı ve Strateji Belgesi** *(Özelleştirilmiş olan bu MarkDown Rehberi)*
2. **Otomatikleştirilmiş Test Kodları** *(GitHub Deposu (Repository) Linki)*
3. **CI/CD Pipeline Yapılandırma Dosyası** *(.github/workflows/ci.yml)*
4. **TestNG Koşum Raporları** *(ExtentReports / Allure / Default HTML Report çıktıları)*
5. **Bulunan Hatalar Raporu (Defect Report)** *(Testler esnasında bulunan bugların ekran görüntüleri ile birlikte listelenmesi)*

<img width="751" height="273" alt="image" src="https://github.com/user-attachments/assets/87a3ee2b-8330-49a0-9acf-7eaf78808c74" />
