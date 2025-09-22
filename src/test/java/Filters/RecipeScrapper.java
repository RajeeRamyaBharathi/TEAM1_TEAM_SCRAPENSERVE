package Filters;

import RecipeData.RecipeUrlInfo;
import Utilities.LoggerLoad;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RecipeScrapper {
    private WebDriver driver;
    private WebDriverWait wait;

    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);

    public RecipeScrapper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    }

    public RecipeUrlInfo scrapeRecipe(String url) {
        RecipeUrlInfo recipe = new RecipeUrlInfo();
        try {
            driver.get(url);

            // Recipe name
            WebElement recipeNameElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[contains(text(),'Recipe')] | //p[contains(text(),'You are here')]/span[last()]")
                )
            );

            recipe.setRecipeUrl(url);

            // Extract Recipe ID from URL (last number before 'r')
            String recipeID = url.replaceAll(".*/(\\d+)r.*", "$1");
            recipe.setRecipeID(recipeID);

            recipe.setRecipeName(getTextSafe(recipeNameElement, "Recipe Name"));

            // Description
            WebElement descElem = safeFind("//div[@id='aboutrecipe']//p[1]");
            recipe.setRecipeDescription(getTextSafe(descElem, "Description"));

            // Prep Time
            WebElement prepElem = safeFind("//h6[contains(text(),'Preparation Time')]//strong");
            recipe.setPreparationTime(getTextSafe(prepElem, "Prep Time"));

            // Cooking Time
            WebElement cookElem = safeFind("//h6[contains(text(),'Cooking Time')]//strong");
            recipe.setCookingTime(getTextSafe(cookElem, "Cook Time"));

            // Servings
            WebElement serveElem = safeFind("//h6[contains(text(),'Makes')]//strong");
            recipe.setNumOfServings(getTextSafe(serveElem, "Servings"));

            // Ingredients
            List<WebElement> ingredientElems = driver.findElements(By.xpath("//div[@id='ingredients']"));
            StringBuilder ingredients = new StringBuilder();
            for (WebElement ing : ingredientElems) {
                ingredients.append(ing.getText().trim()).append("\n");
            }
            recipe.setIngredients(ingredients.length() > 0 ? ingredients.toString().trim() : "Not found");

            // Preparation Method
            List<WebElement> methodElems = driver.findElements(By.xpath("//div[@id='method'] | //div[@id='methods']"));
            StringBuilder method = new StringBuilder();
            for (WebElement step : methodElems) {
                method.append(step.getText().trim()).append("\n");
            }
            recipe.setPreparationMethod(method.length() > 0 ? method.toString().trim() : "Not found");

            // Nutrition table (structured + string)
            List<WebElement> rows = driver.findElements(By.xpath("//table[@id='rcpnutrients']//tr"));
            StringBuilder nutrition = new StringBuilder();
            Map<String, String> nutritionMap = new HashMap<>();

            for (WebElement row : rows) {
                List<WebElement> cols = row.findElements(By.tagName("td"));
                if (cols.size() >= 2) {
                    String nutrient = cols.get(0).getText().trim();
                    String value;
                    try {
                        WebElement span = cols.get(1).findElement(By.tagName("span"));
                        value = span.getText().trim();
                    } catch (Exception e) {
                        value = cols.get(1).getText().trim();
                    }

                    if (!nutrient.isEmpty() && !value.isEmpty()) {
                        nutrition.append(nutrient).append(": ").append(value).append("\n");
                        nutritionMap.put(nutrient, value);
                    }
                }
            }

            recipe.setNutritionValues(nutrition.length() > 0 ? nutrition.toString().trim() : "Not found");
           // recipe.setNutritionMap(nutritionMap);

            LoggerLoad.info("Finished scraping: " + recipe.getRecipeName());
        } catch (Exception e) {
            LoggerLoad.error("Failed to scrape recipe from URL: " + url + " | " + e.getMessage());
        }
        return recipe;
    }

    /**
     * Utility to get safe element text with logging
     */
    private String getTextSafe(WebElement element, String fieldName) {
        try {
            if (element == null) return "Not found";
            wait.until(ExpectedConditions.visibilityOf(element));
            String text = element.getText().trim();
            LoggerLoad.info(fieldName + " => " + text);
            return text.isEmpty() ? "Not found" : text;
        } catch (Exception e) {
            LoggerLoad.warn("Could not find field: " + fieldName + " | " + e.getMessage());
            return "Not found";
        }
    }

    /**
     * Utility to safely locate element (returns null if not found)
     */
    private WebElement safeFind(String xpath) {
        try {
            return driver.findElement(By.xpath(xpath));
        } catch (Exception e) {
            return null;
        }
    }
}
