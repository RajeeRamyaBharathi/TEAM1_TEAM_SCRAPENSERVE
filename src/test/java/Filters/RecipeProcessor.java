package Filters;

import java.util.List;

import RecipeData.RecipeUrlInfo;
import Utilities.DataBaseConnection;
import Utilities.ExcelReader;
import Utilities.LoggerLoad;

public class RecipeProcessor {
    private final ExcelReader lchfFilter;
    private final ExcelReader lfvFilter;
    private final ExcelReader allergyFilter;

    public RecipeProcessor(String excelPath) {
        LoggerLoad.info("Initializing filters from Excel: " + excelPath);
        this.lchfFilter = new ExcelReader(excelPath, 0, 0, 1);   // LCHF Sheet
        this.lfvFilter  = new ExcelReader(excelPath, 1, 0, 1);   // LFV Sheet
        this.allergyFilter = new ExcelReader(excelPath, 2, 0, 1); // Allergy Sheet
    }

    public void processRecipe(RecipeUrlInfo recipe) {
        String ingredients = recipe.getIngredients();

        List<String> lchfElim = lchfFilter.getMatchedEliminate(ingredients);
        List<String> lchfAdd  = lchfFilter.getMatchedAdd(ingredients);

        List<String> lfvElim = lfvFilter.getMatchedEliminate(ingredients);
        List<String> lfvAdd  = lfvFilter.getMatchedAdd(ingredients);

        List<String> allergyElim = allergyFilter.getMatchedEliminate(ingredients);
        List<String> allergyAdd  = allergyFilter.getMatchedAdd(ingredients);

        recipe.setLchfEliminate(String.join(", ", lchfElim));
        recipe.setLchfToAdd(String.join(", ", lchfAdd));
        recipe.setLfvEliminate(String.join(", ", lfvElim));
        recipe.setLfvToAdd(String.join(", ", lfvAdd));
        recipe.setAllergyEliminate(String.join(", ", allergyElim));
        recipe.setAllergyToAdd(String.join(", ", allergyAdd));

        LoggerLoad.debug("Recipe filtered: " + recipe.getRecipeName());

        DataBaseConnection.insertRecipeData(recipe);
    }
}
