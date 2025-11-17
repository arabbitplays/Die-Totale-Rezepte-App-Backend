package oschdi.shopping;

import oschdi.database.HaushaltDatabase;
import oschdi.model.*;

import java.util.*;

public class ShoppingList {
    private Map<String, Float> itemMap;
    private HaushaltDatabase database;

    public ShoppingList(HaushaltDatabase database) {
        itemMap = new HashMap<>();
        this.database = database;
    }

    public void addRecipe(Recipe recipe, float scale) {
        addIngredients(recipe.getIngredientsScaled(scale));
    }

    public void addRecipe(Recipe recipe) {
        addRecipe(recipe, 1);
    }

    public void addIngredients(Ingredient[] ingredients) {
        for (Ingredient ingredient: ingredients) {
            String name = ingredient.getItem().getName();
            if (itemMap.containsKey(name)) {
                itemMap.put(name,
                        itemMap.get(name) + ingredient.getAmount());
            } else {
                itemMap.put(name, ingredient.getAmount());
            }
        }
    }

    public void removeIngredients(Ingredient[] ingredients) {
        for (Ingredient ingredient: ingredients) {
            String name = ingredient.getItem().getName();

            if (itemMap.containsKey(name)) {
                itemMap.put(name,
                        itemMap.get(name) - ingredient.getAmount());
                if (itemMap.get(name) <= 0) {
                    itemMap.remove(name);
                }
            }
        }
    }

    public Ingredient[] getIngredients() {
        Ingredient[] result = new Ingredient[itemMap.size()];
        int i = 0;
        for (String itemName: itemMap.keySet()) {
            result[i] = new Ingredient(database.getItemByName(itemName), itemMap.get(itemName));
            i++;
        }
        return result;
    }
}
