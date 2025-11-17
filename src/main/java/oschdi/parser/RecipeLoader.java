package oschdi.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import oschdi.database.HaushaltDatabase;
import oschdi.model.Ingredient;
import oschdi.model.Item;
import oschdi.model.Recipe;
import oschdi.util.FileUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class RecipeLoader {
    private HaushaltDatabase database;

    public RecipeLoader(HaushaltDatabase database) {
        this.database = database;
    }

    public Recipe[] loadRecipesFromFile(String path) throws IOException, RecipeException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream inputStream = new FileInputStream(FileUtil.getResourcePath() + path);
            return getRecipesFromMap(mapper.readValue(inputStream, Map.class));
        } catch (Exception e) {
            throw new IOException("Loading json file failed!");
        }
    }

    public Recipe loadRecipeFromJson(String jsonString) throws RecipeException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return getRecipeFromMap(mapper.readValue(jsonString, Map.class));
        } catch (JsonProcessingException e) {
            throw new RecipeException(e.getMessage());
        }
    }

    private Recipe[] getRecipesFromMap(Map<?, ?> map) throws RecipeException {
        List<Map<?, ?>> recipes = (List<Map<?, ?>>) map.get("recipes");
        Recipe[] result = new Recipe[recipes.size()];

        for (int j = 0; j < recipes.size(); j++) {
            result[j] = getRecipeFromMap(recipes.get(j));
        }
        return result;
    }

    private Recipe getRecipeFromMap(Map<?, ?> map) throws RecipeException {
        List<Map<String, String>> jsonItems = (List<Map<String, String>>)map.get("ingredients");
        Ingredient[] ingredients = new Ingredient[jsonItems.size()];
        for(int i = 0; i < jsonItems.size(); i++) {
            Map<String, String> ingredientMap = jsonItems.get(i);

            Item item = database.getItemByName(ingredientMap.get("name"));
            if (item == null)
                throw new RecipeException(String.format("Item %s is not existing",
                        ingredientMap.get("name")));

            // use default unit if no other is specified
            if (ingredientMap.containsKey("unit"))
                ingredients[i] = new Ingredient(item, Float.parseFloat(ingredientMap.get("amount")),
                        ingredientMap.get("unit"));
            else
                ingredients[i] = new Ingredient(item, Float.parseFloat(ingredientMap.get("amount")));
        }

        String[] prep = convertToStringArray((List<String>)map.get("prep"));
        String[] process = convertToStringArray((List<String>)map.get("process"));
        String[] notes = convertToStringArray((List<String>)map.get("notes"));

        prep = resolveItemReferences(prep, ingredients);
        process = resolveItemReferences(process, ingredients);

        return new Recipe((String)map.get("name"), ingredients, prep, process, notes);
    }

    private String[] convertToStringArray(List<String> input) {
        return input.toArray(new String[0]);
    }

    private String[] resolveItemReferences(String[] input, Ingredient[] ingredients) {
        String[] output = new String[input.length];
        for (int i = 0; i < input.length; i++) {
            String[] splitParts = input[i].split("__");
            String result = splitParts[0];
            for (int j = 1; j < splitParts.length; j += 2) {
                try {
                    result += getIngredientString(ingredients, splitParts[j]);
                } catch (RecipeException e) {
                    //TODO Error Handling
                    System.out.println(e);
                }
                if (j + 1  < splitParts.length)
                    result += splitParts[j+1];
            }
            output[i] = result;
        }
        return output;
    }

    private String getIngredientString(Ingredient[] ingredients, String name) throws RecipeException {
        for (Ingredient ingredient: ingredients) {
            if (ingredient.getItem().getName().equals(name)) {
                String addOn = ingredient.getUnit();
                if (addOn.equals(""))
                    return ingredient.getAmount() + " " + name;
                else
                    return ingredient.getAmount() + " " + addOn + " " + name;
            }
        }
        throw new RecipeException(String.format("Ingredient %s not found in list of ingredients!", name));
    }
}
