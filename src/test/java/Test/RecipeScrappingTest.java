package Test;

//It is an end-to-end TestNG test: it sets up a headless browser and DB, collects recipe links from configured category pages, scrapes recipe details, runs Excel-based filter logic to classify and insert recipes into filter tables, and finally tears down resources. It’s built to be resilient — individual page failures are logged and skipped

import Browser.BrowserFactory;
import Utilities.ConfigReader;
import Filters.RecipeProcessor;
import Filters.RecipeLinkCollector;
import Filters.RecipeScrapper;
import RecipeData.RecipeUrlInfo;
import Utilities.DataBaseConnection;
import Utilities.LoggerLoad;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.util.Properties;
import java.util.Set;

public class RecipeScrappingTest {
    private WebDriver driver;
    private RecipeLinkCollector linkCollector;
    private RecipeScrapper scraper;
    private RecipeProcessor processor;
    private DataBaseConnection db;

    @BeforeClass
    public void setup() {
        Properties prop = ConfigReader.initializeprop();
        boolean headless = true;
        driver = BrowserFactory.createDriver(headless);
        linkCollector = new RecipeLinkCollector();
        scraper = new RecipeScrapper(driver);
        //  Initialize processor with Excel path
        processor = new RecipeProcessor("src/test/resources/TestData/IngredientsAndComorbidities-ScrapperHackathon.xlsx");
        // Connect DB and create ALL tables
        db = new DataBaseConnection();
        db.connect(prop.getProperty("dburl"), prop.getProperty("dbusername"), prop.getProperty("dbpassword"));
        db.createTables();   
    }

    @Test
    public void scrapeAndFilterRecipes() {
        Properties prop = ConfigReader.initializeprop();
        String[] categoryUrls = prop.getProperty("categoryurl").split(",");
        Set<String> links = linkCollector.getRecipeLinksFromCategories(driver, categoryUrls);
        int recipeId = 1;
        for (String url : links) {
            try {
                //Scrape recipe details
                RecipeUrlInfo recipe = scraper.scrapeRecipe(url);
                recipe.setRecipeID("R" + recipeId++);
                //Process recipe → classify into one of 6 tables
                processor.processRecipe(recipe);
                //Log classification info
                LoggerLoad.info("==== Recipe Classification ====");
                LoggerLoad.info("Name: " + recipe.getRecipeName());
                LoggerLoad.info("Food Category: " + recipe.getFoodCategory());
                LoggerLoad.info("Cuisine Category: " + recipe.getCuisineCategory());
                LoggerLoad.info("Recipe Category: " + recipe.getRecipeCategory());
                LoggerLoad.info("Sub Category: " + recipe.getSubCategory());
                LoggerLoad.info("Food Processing: " + recipe.getFoodProcessing());
                LoggerLoad.info("Food Processing To Avoid: " + recipe.getFoodProcessingToAvoid());
                LoggerLoad.info("================================");
            } catch (Exception e) {
                System.out.println("Error processing: " + url);
                e.printStackTrace();
            }
        }
    }
    @AfterClass
    public void teardown() {
        db.disconnect();
        if (driver != null) driver.quit();
    }
}
