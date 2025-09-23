package RecipeData;

//RecipeUrlInfo is a plain data object (POJO) that holds all scraped recipe fields: identity, name, times, ingredients, method, nutrition, Excel filter matches (LCHF/LFV/Allergy), and classification labels (food/cuisine/category/subcategory/processing). The scraper fills this object and the DB layer reads from it to insert records

public class RecipeUrlInfo {
	//Basic Info
	private String recipeID;
    private String recipeName;
    private String ingredients;
    private String preparationTime;
    private String cookingTime;
    private String numOfServings;
    private String recipeDescription;
    private String preparationMethod;
    private String nutritionValues;
    private String recipeUrl;
    //Filters
    private String lchfEliminate;
    private String lchfToAdd;
    private String lchfRecipesToAvoid;
    private String lchfFoodProcessing;
    private String lfvEliminate;
    private String lfvToAdd;
    private String lfvRecipesToAvoid;
    private String lfvFoodProcessing;
    private String allergyEliminate;
    private String allergyToAdd;
    // === Classification
    private String foodCategory;
    private String cuisineCategory;
    private String recipeCategory;
    private String subCategory;
    private String foodProcessing;
    private String foodProcessingToAvoid;
    // Getters and Setters
    public String getRecipeID() { 
    	return recipeID; }
    public void setRecipeID(String recipeID) { this.recipeID = recipeID; }

    public String getRecipeName() { return recipeName; }
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }

  
    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getPreparationTime() { return preparationTime; }
    public void setPreparationTime(String preparationTime) { this.preparationTime = preparationTime; }

    public String getCookingTime() { return cookingTime; }
    public void setCookingTime(String cookingTime) { this.cookingTime = cookingTime; }

    
    public String getNumOfServings() { return numOfServings; }
    public void setNumOfServings(String numOfServings) { this.numOfServings = numOfServings; }

    public String getRecipeDescription() { return recipeDescription; }
    public void setRecipeDescription(String recipeDescription) { this.recipeDescription = recipeDescription; }

    public String getPreparationMethod() { return preparationMethod; }
    public void setPreparationMethod(String preparationMethod) { this.preparationMethod = preparationMethod; }

    public String getNutritionValues() { return nutritionValues; }
    public void setNutritionValues(String nutritionValues) { this.nutritionValues = nutritionValues; }

    public String getRecipeUrl() { return recipeUrl; }
    public void setRecipeUrl(String recipeUrl) { this.recipeUrl = recipeUrl; }
    
    public String getLchfEliminate() { return lchfEliminate; }
    public void setLchfEliminate(String lchfEliminate) { this.lchfEliminate = lchfEliminate; }

    public String getLchfToAdd() { return lchfToAdd; }
    public void setLchfToAdd(String lchfToAdd) { this.lchfToAdd = lchfToAdd; }
    
    public String getLchfRecipesToAvoid() { return lchfRecipesToAvoid; }
    public void setLchfRecipesToAvoid(String lchfRecipesToAvoid) { this.lchfRecipesToAvoid = lchfRecipesToAvoid; }
    
    public String getLchfFoodProcessing() { return lchfFoodProcessing; }
    public void setLchfFoodProcessing(String lchfFoodProcessing) { this.lchfFoodProcessing = lchfFoodProcessing; }

    public String getLfvEliminate() { return lfvEliminate; }
    public void setLfvEliminate(String lfvEliminate) { this.lfvEliminate = lfvEliminate; }

    public String getLfvToAdd() { return lfvToAdd; }
    public void setLfvToAdd(String lfvToAdd) { this.lfvToAdd = lfvToAdd; }
    
    public String getLfvRecipesToAvoid() { return lfvRecipesToAvoid; }
    public void setLfvRecipesToAvoid(String lfvRecipesToAvoid) { this.lfvRecipesToAvoid = lfvRecipesToAvoid; }
    
    public String getLfvFoodProcessing() { return lfvFoodProcessing; }
    public void setLfvFoodProcessing(String lfvFoodProcessing) { this.lfvFoodProcessing = lfvFoodProcessing; }

    public String getAllergyEliminate() { return allergyEliminate; }
    public void setAllergyEliminate(String allergyEliminate) { this.allergyEliminate = allergyEliminate; }

    public String getAllergyToAdd() { return allergyToAdd; }
    public void setAllergyToAdd(String allergyToAdd) { this.allergyToAdd = allergyToAdd;}
    
    public String getFoodCategory() { return foodCategory; }
    public void setFoodCategory(String foodCategory) { this.foodCategory = foodCategory; }

    public String getCuisineCategory() { return cuisineCategory; }
    public void setCuisineCategory(String cuisineCategory) { this.cuisineCategory = cuisineCategory; }

    public String getRecipeCategory() { return recipeCategory; }
    public void setRecipeCategory(String recipeCategory) { this.recipeCategory = recipeCategory; }

    public String getSubCategory() { return subCategory; }
    public void setSubCategory(String subCategory) { this.subCategory = subCategory; }

    public String getFoodProcessing() { return foodProcessing; }
    public void setFoodProcessing(String foodProcessing) { this.foodProcessing = foodProcessing; }

    public String getFoodProcessingToAvoid() { return foodProcessingToAvoid; }
    public void setFoodProcessingToAvoid(String foodProcessingToAvoid) { this.foodProcessingToAvoid = foodProcessingToAvoid; }
}
