package Filters;

//This class visits category pages using Selenium, extracts recipe links with XPath, and stores them in a Set to avoid duplicates. It uses error handling with logging to skip failed pages. A small wait ensures the page loads before collecting links

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
                Thread.sleep(1500);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                LoggerLoad.error("Error loading category page: " + url + " | " + e.getMessage());
                continue;
            }
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




