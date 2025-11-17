package oschdi.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import oschdi.model.Ingredient;
import oschdi.model.Recipe;
import oschdi.util.FileUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class RecipeSaver {
    private ObjectMapper mapper = new ObjectMapper();
    String resourcePath = "src/main/resources/recipes.json";

    public void safeRecipes(List<Recipe> recipes) throws IOException {
        ObjectNode root = mapper.createObjectNode();

        ArrayNode recipesNode = mapper.createArrayNode();
        for (Recipe recipe: recipes) {
            ObjectNode recipeNode = mapper.createObjectNode();
            recipeNode.put("name", recipe.getName());
            recipeNode.put("ingredients", getIngredientsNode(recipe.getIngredients()));
            recipeNode.put("prep", mapper.valueToTree(recipe.getPrep()));
            recipeNode.put("process", mapper.valueToTree(recipe.getProcess()));
            recipeNode.put("notes", mapper.valueToTree(recipe.getNotes()));
            recipesNode.add(recipeNode);
        }
        root.put("recipes", recipesNode);

        File file = new File(FileUtil.getResourcePath() + "/recipes.json");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(mapper.writeValueAsString(root));
        fileWriter.close();
    }
     private ArrayNode getIngredientsNode(Ingredient[] ingredients) {
         ArrayNode result = mapper.createArrayNode();
         for (Ingredient ingredient: ingredients) {
             ObjectNode ingredientNode = mapper.createObjectNode();
             ingredientNode.put("name", ingredient.getItem().getName());
             ingredientNode.put("amount", Float.toString(ingredient.getAmount()));
             ingredientNode.put("unit", ingredient.getUnit());
             ingredientNode.put("fresh", ingredient.getItem().isFresh());

             result.add(ingredientNode);
         }
         return result;
     }
}
