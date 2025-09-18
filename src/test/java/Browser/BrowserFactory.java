package Browser;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BrowserFactory {
	public static WebDriver driverinstance;
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	public WebDriver browsersetup(String browsername) throws IOException {
		if (browsername.equalsIgnoreCase("Chrome")) {
			driver.set(new ChromeDriver());
		} else if (browsername.equalsIgnoreCase("Edge")) {
			driver.set(new EdgeDriver());
		} else if (browsername.equalsIgnoreCase("Firefox")) {
			driver.set(new FirefoxDriver());
		}

		driverinstance = driver.get();
		driverinstance.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driverinstance.manage().window().maximize();

		return driverinstance;
	}

	public static WebDriver getdriverinstance() {
		return driverinstance;
	}
}