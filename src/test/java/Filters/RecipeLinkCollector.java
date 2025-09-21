package Filters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import Utilities.LoggerLoad;

public class RecipeLinkCollector {

    public Set<String> getRecipeLinksFromCategories(WebDriver driver, String[] categoryUrls) {
        Set<String> recipeLinks = new HashSet<>();

        for (String url : categoryUrls) {
            try {
                driver.get(url);
                // small wait - replace with explicit waits if needed
                Thread.sleep(1500);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                LoggerLoad.error("Error loading category page: " + url + " | " + e.getMessage());
                continue;
            }

            // Adjust this selector based on site structure for category pages
            List<WebElement> links = driver.findElements(By.xpath(".//h5/a"));

            for (WebElement link : links) {
                String href = link.getAttribute("href");
                if (href != null && href.contains("r")) {
                    recipeLinks.add(href);
                }
            }

            System.out.println("Found " + links.size() + " links in category page: " + url);
        }

        return recipeLinks;
    }
}
