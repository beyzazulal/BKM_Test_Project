package com.bkm.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(plugin = {
        "pretty",
        "html:target/cucumber-reports.html",
        "json:target/cucumber.json"
},
        // Çalıştırılacak feature dosyalarının yolu (Sırayla tümünü okur)
        features = "src/test/resources/features",
        // Step definition (Java kodlarının) bulunduğu paket
        glue = "com/bkm/step_definitions",
        // true yaparsanız kodu açmadan eksik adımları tarar; false yaparsanız gerçek
        // testi başlatır
        dryRun = false,
        // Belirli senaryoları seçmek için (örn: "@smoke", "@login" vs.) Boş bırakılırsa
        // tümünü çalıştırır.
        tags = "")
public class CukesRunner extends AbstractTestNGCucumberTests {
    // Cucumber ile TestNG entegrasyonunu sağlamak için AbstractTestNGCucumberTests
    // sınıfından extend edilir.
}
