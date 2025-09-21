package Browser;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BrowserFactory {
	 private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	    public static WebDriver createDriver(boolean headless) {
	        WebDriverManager.chromedriver().setup();
	        EdgeOptions options = new EdgeOptions();
	        if (headless) 
	        options.addArguments("--headless=new");
	        options.addArguments("--disable-notifications");
	        options.addArguments("--disable-gpu");
	        options.addArguments("--window-size-1920,1080");

	        driver.set(new EdgeDriver(options));

	        WebDriver webDriver = driver.get();
	        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	        webDriver.manage().window().maximize();
	        return webDriver;
	    }

	    public static WebDriver getDriver() {
	        return driver.get();
	    }

	    public static void quitDriver() {
	        if (driver.get() != null) {
	            driver.get().quit();
	            driver.remove();
	        }
	    }
}
