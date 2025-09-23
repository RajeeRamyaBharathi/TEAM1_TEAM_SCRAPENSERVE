package Filters;

//This class is responsible for classifying a recipe and storing it in the correct database table. First, it reads filter rules from Excel for three categories: LCHF, LFV, and Allergy. Then, it uses RecipeFoodCategory to classify the recipe into food type, cuisine, category, etc. Based on ingredient matches, it inserts the recipe into the correct filter table (Eliminate/Add). If no matches are found, it uses fallback rules. This way, the class connects Excel-based filter rules with database storage in a structured way.”

import java.util.List;

import RecipeData.RecipeUrlInfo;
import Utilities.ExcelReader;
import Utilities.LoggerLoad;
import Utilities.DataBaseConnection;

public class RecipeProcessor {
    private final ExcelReader lchfFilter;
    private final ExcelReader lfvFilter;
    private final ExcelReader allergyFilter;
    private final RecipeFoodCategory categoryHelper;

    public RecipeProcessor(String excelPath) {
    	
        // LCHF sheet = index 0 → Eliminate, Add, RecipesToAvoid, FoodProcessing
        this.lchfFilter = new ExcelReader(excelPath, 0, 0, 1, 2, 3);
        // LFV sheet = index 1 → Eliminate, Add, RecipesToAvoid, FoodProcessing
        this.lfvFilter  = new ExcelReader(excelPath, 1, 0, 1, 2, 3);
        // Allergy sheet = index 2 → Only Eliminate & Add (so pass -1 for avoid/process)
        this.allergyFilter = new ExcelReader(excelPath, 2, 0, 1, -1, -1);
        this.categoryHelper = new RecipeFoodCategory();
    }
    
    public void processRecipe(RecipeUrlInfo recipe) {
        String ingredients = recipe.getIngredients();
        String recipeName = recipe.getRecipeName();
        //Classification 
        recipe.setFoodCategory(categoryHelper.getfoodcategory(ingredients));
        recipe.setRecipeCategory(categoryHelper.getrecipecategory(recipeName));
        recipe.setCuisineCategory("Indian"); // Example default (extend later)
        recipe.setSubCategory("General");    // Extend based on mapping
        recipe.setFoodProcessing("Raw/Steamed"); 
        recipe.setFoodProcessingToAvoid("Processed");
        boolean inserted = false;

        //LCHF Check 
        List<String> lchfElim = lchfFilter.getMatchedEliminate(ingredients);
        List<String> lchfAdd  = lchfFilter.getMatchedAdd(ingredients);
        if (!lchfElim.isEmpty()) {
            DataBaseConnection.insertIntoFilterTable("lchf_eliminate", recipe);
            LoggerLoad.info("Recipe inserted to LCHF Eliminate: " + recipe.getRecipeName());
            inserted = true;
        } else if (!lchfAdd.isEmpty()) {
            DataBaseConnection.insertIntoFilterTable("lchf_add", recipe);
            LoggerLoad.info("Recipe inserted to LCHF Add: " + recipe.getRecipeName());
            inserted = true;
        } else {
            DataBaseConnection.insertIntoFilterTable("lchf_eliminate", recipe); // fallback
            LoggerLoad.info("Recipe inserted to LCHF Eliminate (no add match): " + recipe.getRecipeName());
            inserted = true;
        }

        //LFV Check
        List<String> lfvElim = lfvFilter.getMatchedEliminate(ingredients);
        List<String> lfvAdd  = lfvFilter.getMatchedAdd(ingredients);
        if (!lfvElim.isEmpty()) {
            DataBaseConnection.insertIntoFilterTable("lfv_eliminate", recipe);
            LoggerLoad.info("Recipe inserted to LFV Eliminate: " + recipe.getRecipeName());
        } else if (!lfvAdd.isEmpty()) {
            DataBaseConnection.insertIntoFilterTable("lfv_add", recipe);
            LoggerLoad.info("Recipe inserted to LFV Add: " + recipe.getRecipeName());
        } else {
            DataBaseConnection.insertIntoFilterTable("lfv_eliminate", recipe); // fallback
            LoggerLoad.info("Recipe inserted to LFV Eliminate (no add match): " + recipe.getRecipeName());
        }

        //Allergy Check
        List<String> allergyElim = allergyFilter.getMatchedEliminate(ingredients);
        List<String> allergyAdd  = allergyFilter.getMatchedAdd(ingredients);

        if (!allergyElim.isEmpty()) {
            DataBaseConnection.insertIntoFilterTable("allergy_eliminate", recipe);
            LoggerLoad.info("Recipe inserted to Allergy Eliminate: " + recipe.getRecipeName());
        } else if (!allergyAdd.isEmpty()) {
            DataBaseConnection.insertIntoFilterTable("allergy_add", recipe);
            LoggerLoad.info("Recipe inserted to Allergy Add: " + recipe.getRecipeName());
        } else {
            DataBaseConnection.insertIntoFilterTable("allergy_eliminate", recipe); // fallback
            LoggerLoad.info("Recipe inserted to Allergy Eliminate (no add match): " + recipe.getRecipeName());
        }

        // If nothing matched
        if (!inserted) {
            LoggerLoad.warn("Recipe did not match any filter: " + recipe.getRecipeName());
        }
    }
}
