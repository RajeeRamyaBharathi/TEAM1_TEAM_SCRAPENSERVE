package Utilities;

//It  loads Config.properties from src/test/resources and returns a Properties object. It also stores a runtime browserType (set/get) so tests can switch browser programmatically. The file read uses a try-with-resources FileInputStream and logs exceptions by printing stack traces

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    // Singleton pattern to load properties only once
	private static String browserType = null;
	public static Properties initializeprop() {
		Properties prop = new Properties();
		File profile = new File(
				System.getProperty("user.dir") + "/src/test/resources/Config.properties");
		try (FileInputStream fis = new FileInputStream(profile)) {
			prop.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	public static void setBrowserType(String browser) {
		browserType = browser;
	}
	public static String getBrowserType() {
		if (browserType != null) {
			return browserType;
		} else {
			throw new RuntimeException(
					"Browser not specified");
		}
	}
}
