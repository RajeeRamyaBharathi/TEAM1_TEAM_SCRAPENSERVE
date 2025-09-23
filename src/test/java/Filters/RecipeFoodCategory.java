package Filters;

//This class classifies recipes into categories based on ingredients and recipe names. It checks if food is Non-Veg, Eggitarian, Vegan, Jain, etc. It also identifies whether a dish is Breakfast, Lunch, Dinner, Snack, or Dessert. Additionally, it provides helper methods to detect Cuisine (like South Indian, North Indian, Indo-Chinese), SubCategory (Soup, Rice, Curry, etc.), and cooking style or processing (Fried, Boiled, Steamed, etc.), including what should be avoided. Essentially, it acts as a rule-based classifier for recipes.”

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeFoodCategory {
	public String getfoodcategory(String ingredient) {
        String foodcategory = null;
        boolean iseggiterian = false;
        boolean isjainfood = true;
        boolean isvegan = true;

        ArrayList<String> nonvegchecklist = new ArrayList<>(Arrays.asList("fish","chicken","meat","mutton","ham","sausage","tuna","sardines","salmon","mackerel"));
        ArrayList<String> nonveganchecklist = new ArrayList<>(Arrays.asList("milk","yogurt","curd","butter","ghee","cheese","paneer","malai","raitha","dahi"));
        ArrayList<String> nonjainchecklist = new ArrayList<>(Arrays.asList("potato","onion","garlic","mushrooms","cauli","carrot","beet","yeast","ginger","radish","vinegar"));
        boolean isnonveg = false;
        for (String nonveg : nonvegchecklist) {
            if (ingredient.toLowerCase().contains(nonveg))
                isnonveg = true;
        }
        if (ingredient.toLowerCase().contains("egg"))
            iseggiterian = true;
        for (String nonjainfood : nonjainchecklist) {
            if (ingredient.toLowerCase().contains(nonjainfood))
                isjainfood = false;
        }
        for (String nonveganlist : nonveganchecklist) {
            if (ingredient.toLowerCase().contains(nonveganlist))
                isvegan = false;
        }
        if (isnonveg)
            foodcategory = "Non Vegetarian";
        else if (iseggiterian)
            foodcategory = "Eggitarian";
        else if (!isvegan && !isjainfood)
            foodcategory = "Vegetarian";
        else if (isvegan && isjainfood)
            foodcategory = "Vegan Jain food";
        else if (isvegan && !isjainfood)
            foodcategory = "Vegan";
        else if (!isvegan && isjainfood)
            foodcategory = "Jain food";
        return foodcategory;
    }

    public String getrecipecategory(String recipename) {
        String recipecategory = null;
        ArrayList<String> breakfastchecklist = new ArrayList<>(Arrays.asList("idly","dosa","chilla","puri","upma","poha","pancake","toast","sandwich"));
        ArrayList<String> lunchchecklist = new ArrayList<>(Arrays.asList("rice","roti","curry","sambar","pulav","biriyani"));
        ArrayList<String> dinnerchecklist = new ArrayList<>(Arrays.asList("parotta","paratha","salad","pasta","kebab"));
        ArrayList<String> snackchecklist = new ArrayList<>(Arrays.asList("chakli","vada","pakora","chaat","samosa"));
        ArrayList<String> dessertchecklist = new ArrayList<>(Arrays.asList("kheer","cake","payasam","sweet","rasgulla"));

        String name = recipename.toLowerCase();
        if (breakfastchecklist.stream().anyMatch(name::contains)) recipecategory = "Breakfast";
        else if (lunchchecklist.stream().anyMatch(name::contains)) recipecategory = "Lunch";
        else if (dinnerchecklist.stream().anyMatch(name::contains)) recipecategory = "Dinner";
        else if (snackchecklist.stream().anyMatch(name::contains)) recipecategory = "Snack";
        else if (dessertchecklist.stream().anyMatch(name::contains)) recipecategory = "Dessert";
        else recipecategory = "Other";
        return recipecategory;
    }
    
    public String getCuisine(String recipename, String ingredients) {
        if (recipename.toLowerCase().contains("idly") || recipename.toLowerCase().contains("dosa"))
            return "South Indian";
        if (recipename.toLowerCase().contains("paneer") || ingredients.toLowerCase().contains("paneer"))
            return "North Indian";
        if (recipename.toLowerCase().contains("momos") || recipename.toLowerCase().contains("noodles"))
            return "Indo-Chinese";
        return "Other";
    }

    public String getSubCategory(String recipename) {
        if (recipename.toLowerCase().contains("soup")) return "Soup";
        if (recipename.toLowerCase().contains("rice")) return "Rice";
        if (recipename.toLowerCase().contains("curry")) return "Curry";
        return "General";
    }

    public String getFoodProcessing(String methodText) {
        if (methodText.toLowerCase().contains("fried")) return "Fried";
        if (methodText.toLowerCase().contains("boiled")) return "Boiled";
        if (methodText.toLowerCase().contains("steamed")) return "Steamed";
        if (methodText.toLowerCase().contains("baked")) return "Baked";
        return "Raw/Other";
    }

    public String getFoodProcessingToAvoid(String methodText) {
        if (methodText.toLowerCase().contains("deep fry")) return "Avoid Deep Fried";
        if (methodText.toLowerCase().contains("processed")) return "Avoid Processed";
        return "None";
    }

}
