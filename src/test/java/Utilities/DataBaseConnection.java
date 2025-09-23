package Utilities;

//This  class manages database operations. It connects to PostgreSQL, creates the required filter tables and a combined view, inserts recipe records while avoiding duplicates, and disconnects safely.

import RecipeData.RecipeUrlInfo;
import java.sql.*;

public class DataBaseConnection {
    private static Connection conn;
    // Connect to DB
    public void connect(String url, String user, String password) {
        try {
            conn = DriverManager.getConnection(url, user, password);
            LoggerLoad.info("Database connected!");
        } catch (SQLException e) {
            LoggerLoad.error("Failed to connect: " + e.getMessage());
        }
    }
    // Create 6 separate filter tables (each with full recipe schema)
    public void createTables() {
        String[] tables = {
            "lchf_eliminate", "lchf_add",
            "lfv_eliminate", "lfv_add",
            "allergy_eliminate", "allergy_add"
        };
        for (String table : tables) {
            String sql = "CREATE TABLE IF NOT EXISTS " + table + " (" +
                    "recipe_id SERIAL PRIMARY KEY," +
                    "recipe_name TEXT," +
                    "ingredients TEXT," +
                    "preparation_time TEXT," +
                    "cooking_time TEXT," +
                    "num_of_servings TEXT," +
                    "recipe_description TEXT," +
                    "preparation_method TEXT," +
                    "nutrition_values TEXT," +
                    "recipe_url TEXT UNIQUE," +
                    "food_category TEXT," +
                    "cuisine_category TEXT," +
                    "recipe_category TEXT," +
                    "sub_category TEXT," +
                    "food_processing TEXT," +
                    "food_processing_to_avoid TEXT" +
                    ")";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                LoggerLoad.info("Table '" + table + "' created or already exists.");
            } catch (Exception e) {
                LoggerLoad.error("Error creating table " + table + ": " + e.getMessage());
            }
        }
        createView();
    }
 // Create view combining all filter tables
    public void createView() {
        String sql = "CREATE OR REPLACE VIEW all_filtered_recipes AS " +
                "SELECT 'LCHF Eliminate' AS filter_type, * FROM lchf_eliminate " +
                "UNION ALL " +
                "SELECT 'LCHF Add' AS filter_type, * FROM lchf_add " +
                "UNION ALL " +
                "SELECT 'LFV Eliminate' AS filter_type, * FROM lfv_eliminate " +
                "UNION ALL " +
                "SELECT 'LFV Add' AS filter_type, * FROM lfv_add " +
                "UNION ALL " +
                "SELECT 'Allergy Eliminate' AS filter_type, * FROM allergy_eliminate " +
                "UNION ALL " +
                "SELECT 'Allergy Add' AS filter_type, * FROM allergy_add";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            LoggerLoad.info("View 'all_filtered_recipes' created/updated.");
        } catch (Exception e) {
            LoggerLoad.error("Error creating view: " + e.getMessage());
        }
    }
    // Insert recipe into a given filter table
    public static void insertIntoFilterTable(String tableName, RecipeUrlInfo recipe) {
        String sql = "INSERT INTO " + tableName + " (" +
                "recipe_name, ingredients, preparation_time, cooking_time, num_of_servings, " +
                "recipe_description, preparation_method, nutrition_values, recipe_url,"+
                "food_category, cuisine_category, recipe_category, sub_category, " +
                "food_processing, food_processing_to_avoid) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                "ON CONFLICT (recipe_url) DO NOTHING";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, recipe.getRecipeName());
            ps.setString(2, recipe.getIngredients());
            ps.setString(3, recipe.getPreparationTime());
            ps.setString(4, recipe.getCookingTime());
            ps.setString(5, recipe.getNumOfServings());
            ps.setString(6, recipe.getRecipeDescription());
            ps.setString(7, recipe.getPreparationMethod());
            ps.setString(8, recipe.getNutritionValues());
            ps.setString(9, recipe.getRecipeUrl());
            ps.setString(10, recipe.getFoodCategory());
            ps.setString(11, recipe.getCuisineCategory());
            ps.setString(12, recipe.getRecipeCategory());
            ps.setString(13, recipe.getSubCategory());
            ps.setString(14, recipe.getFoodProcessing());
            ps.setString(15, recipe.getFoodProcessingToAvoid());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                LoggerLoad.info("Inserted recipe into " + tableName + ": " + recipe.getRecipeName());
            } else {
                LoggerLoad.warn("Duplicate skipped in " + tableName + ": " + recipe.getRecipeName());
            }
        } catch (Exception e) {
            LoggerLoad.error("Error inserting into " + tableName +
                    " for recipe: " + recipe.getRecipeName() + " | " + e.getMessage());
        }
    }
    // Disconnect DB
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
