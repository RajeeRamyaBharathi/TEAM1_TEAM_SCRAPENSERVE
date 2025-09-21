package Utilities;

import RecipeData.RecipeUrlInfo;
import java.sql.*;

public class DataBaseConnection {
    private static Connection conn;

    public void connect(String url, String user, String password) {
        try {
            conn = DriverManager.getConnection(url, user, password);
            LoggerLoad.info("Database connected!");
        } catch (SQLException e) {
            LoggerLoad.error("Failed to connect: " + e.getMessage());
        }
    }


    public void createTables() {
    	 try (Statement stmt = conn.createStatement()) {
             // ✅ Recipes Table (Main Info Only)
             stmt.execute("CREATE TABLE IF NOT EXISTS recipes (" +
                 "recipe_id SERIAL PRIMARY KEY,"+
                 "recipe_name TEXT,"+
                "ingredients TEXT,"+
                 "preparation_time TEXT,"+
                 "cooking_time TEXT,"+
                 "num_of_servings TEXT,"+
                 "recipe_description TEXT,"+
                 "preparation_method TEXT,"+
                "nutrition_values TEXT," +  // keep as plain text
                 "recipe_url TEXT UNIQUE)");
             // ✅ LCHF Filters
             stmt.execute("CREATE TABLE IF NOT EXISTS lchf_filters (" +
                     "id SERIAL PRIMARY KEY," +
                     "recipe_url TEXT REFERENCES recipes(recipe_url) ON DELETE CASCADE," +
                     "eliminate TEXT," +
                     "add TEXT," +
                     "recipes_to_avoid TEXT," +
                     "food_processing TEXT)");

             // ✅ LFV Filters
             stmt.execute("CREATE TABLE IF NOT EXISTS lfv_filters (" +
                     "id SERIAL PRIMARY KEY," +
                     "recipe_url TEXT REFERENCES recipes(recipe_url) ON DELETE CASCADE," +
                     "eliminate TEXT," +
                     "add TEXT," +
                     "recipes_to_avoid TEXT," +
                     "food_processing TEXT)");

             // ✅ Allergy Filters
             stmt.execute("CREATE TABLE IF NOT EXISTS allergy_filters (" +
                     "id SERIAL PRIMARY KEY," +
                     "recipe_url TEXT REFERENCES recipes(recipe_url) ON DELETE CASCADE," +
                     "eliminate TEXT," +
                     "add TEXT)");

             LoggerLoad.info("All tables created or already exist.");
         } catch (SQLException e) {
             LoggerLoad.error("Error creating tables: " + e.getMessage());
         }
     }


        

    public static void insertRecipeData(RecipeUrlInfo recipe) {
        String sql = "INSERT INTO recipes("+
                 "recipe_name,"+
                 "ingredients, preparation_time, cooking_time, num_of_servings, "+
                 " recipe_description, preparation_method, nutrition_values, "+
                 "recipe_url) "+
                 "VALUES (?,?,?,?,?,?,?,?,?)"+
                 "ON CONFLICT (recipe_url) DO NOTHING";
          

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, recipe.getRecipeName());
            ps.setString(2, recipe.getIngredients());
            ps.setString(3, recipe.getPreparationTime());
            ps.setString(4, recipe.getCookingTime());
            ps.setString(5, recipe.getNumOfServings());
            ps.setString(6, recipe.getRecipeDescription());
            ps.setString(7, recipe.getPreparationMethod());
            ps.setString(8, recipe.getNutritionValues()); // plain text storage
            ps.setString(9, recipe.getRecipeUrl());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                LoggerLoad.info("Inserted recipe: " + recipe.getRecipeName());
            } else {
                LoggerLoad.warn("Recipe not inserted (duplicate URL?): " + recipe.getRecipeName());
            }
        } catch (SQLException e) {
            LoggerLoad.error("Error inserting recipe: " + recipe.getRecipeName() + " | " + e.getMessage());
        }
    }

    // ==================================
    // INSERT INTO FILTER TABLES
    // ==================================
    public static void insertLchfFilter(String recipeUrl, String eliminate, String add, String avoid, String process) {
        String sql = "INSERT INTO lchf_filters (recipe_url, eliminate, add, recipes_to_avoid, food_processing) " +
                "VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, recipeUrl);
            ps.setString(2, eliminate);
            ps.setString(3, add);
            ps.setString(4, avoid);
            ps.setString(5, process);
            ps.executeUpdate();
            LoggerLoad.info("Inserted LCHF filter for: " + recipeUrl);
        } catch (SQLException e) {
            LoggerLoad.error("Error inserting LCHF filter: " + e.getMessage());
        }
    }

    public static void insertLfvFilter(String recipeUrl, String eliminate, String add, String avoid, String process) {
        String sql = "INSERT INTO lfv_filters (recipe_url, eliminate, add, recipes_to_avoid, food_processing) " +
                "VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, recipeUrl);
            ps.setString(2, eliminate);
            ps.setString(3, add);
            ps.setString(4, avoid);
            ps.setString(5, process);
            ps.executeUpdate();
            LoggerLoad.info("Inserted LFV filter for: " + recipeUrl);
        } catch (SQLException e) {
            LoggerLoad.error("Error inserting LFV filter: " + e.getMessage());
        }
    }

    public static void insertAllergyFilter(String recipeUrl, String eliminate, String add) {
        String sql = "INSERT INTO allergy_filters (recipe_url, eliminate, add) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, recipeUrl);
            ps.setString(2, eliminate);
            ps.setString(3, add);
            ps.executeUpdate();
            LoggerLoad.info("Inserted Allergy filter for: " + recipeUrl);
        } catch (SQLException e) {
            LoggerLoad.error("Error inserting Allergy filter: " + e.getMessage());
        }
    }
   
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
//                // Build filter summary
//                String filterSummary =
//                        "LCHF elim=" + recipe.getLchfEliminate() +
//                        " | LCHF add=" + recipe.getLchfToAdd() +
//                        " | LFV elim=" + recipe.getLfvEliminate() +
//                        " | LFV add=" + recipe.getLfvToAdd() +
//                        " | Allergy elim=" + recipe.getAllergyEliminate() +
//                        " | Allergy add=" + recipe.getAllergyToAdd();
//
//                // Print in console
//                LoggerLoad.info("Filter check for " + recipe.getRecipeName() + " => " + filterSummary);
//
//                
//                String updateSql = "UPDATE recipes SET recipe_description = recipe_description || '\nFILTER CHECK: " +
//                                   filterSummary + "' WHERE recipe_url = ?";
//                try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
//                    updatePs.setString(1, recipe.getRecipeUrl());
//                    updatePs.executeUpdate();
//                }
//            } else {
//                LoggerLoad.warn("Recipe not inserted (possible duplicate URL): " + recipe.getRecipeName());
//            }

             // checkPs.setString(1, recipe.getRecipeUrl());
//                    ResultSet rs = checkPs.executeQuery();
//                    if (rs.next()) {//                // 🔎 Verify immediately by reading back from DB
//                String checkSql = "SELECT lchf_eliminate, lchf_to_add, lfv_eliminate, lfv_to_add, allergy_eliminate, allergy_to_add " +
////                                  "FROM recipes WHERE recipe_url = ?";
//                try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
//      
//                        LoggerLoad.info("Filter check for " + recipe.getRecipeName() + " => " +
//                            "LCHF elim=" + rs.getString("lchf_eliminate") +
//                            " | LCHF add=" + rs.getString("lchf_to_add") +
//                            " | LFV elim=" + rs.getString("lfv_eliminate") +
//                            " | LFV add=" + rs.getString("lfv_to_add") +
//                            " | Allergy elim=" + rs.getString("allergy_eliminate") +
//                            " | Allergy add=" + rs.getString("allergy_to_add"));
//                    }
//                }
//            }

//            ps.executeUpdate();
//            LoggerLoad.info("Inserted recipe: " + recipe.getRecipeName());
//        } catch (Exception e) {
//            LoggerLoad.error("Error inserting recipe: " + recipe.getRecipeName() + " | " + e.getMessage());
//        }
//    }
//    
   

	public void disconnect() {
        try {
            if (conn != null && !conn.isClosed())
                conn.close();
            LoggerLoad.info("Database disconnected.");
        } catch (Exception e) {
            LoggerLoad.error("Error disconnecting database: " + e.getMessage());
        }
    }

}
