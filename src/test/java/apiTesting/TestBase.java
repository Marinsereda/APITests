package apiTesting;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class TestBase {
    public static WebDriver browser;
    HttpURLConnection con;

    @BeforeTest(alwaysRun = true)

    public static void openBrowser() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        browser = new ChromeDriver(new ChromeOptions().addArguments("--incognito"));


        browser.manage().window().maximize();
        browser.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);

    }


    @AfterTest(alwaysRun = true)
    public void closeBrowser() {
        browser.quit();
    }
}
