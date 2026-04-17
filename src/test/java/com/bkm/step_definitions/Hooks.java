package com.bkm.step_definitions;

import com.bkm.utilities.Driver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.AfterAll;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    @Before
    public void setup() {
        // Her senaryo öncesi tarayıcıyı hazırla ve çerezleri temizle
        Driver.getDriver().manage().deleteAllCookies();
    }

    @After
    public void teardown(Scenario scenario) {
        if (scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failed_Screenshot");
        }
        // İZLEMEK İÇİN: Her senaryodan sonra tarayıcıyı KAPATMIYORUZ.
        // Pencere kapanıp açılmasın diye bu satırı kaldırdık/yorum satırı yaptık:
        // Driver.closeDriver();

        // Senaryolar arası geçişi izleyebilmen için 3 saniyelik bekleme eklendi
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void afterAll() {
        // Tüm 24 test (Feature'lar) bittikten sonra en son 1 kere tarayıcıyı kapat!
        Driver.closeDriver();
    }
}