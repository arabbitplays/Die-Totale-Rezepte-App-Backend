package oschdi.database;

import oschdi.model.Item;
import oschdi.model.Recipe;
import oschdi.parser.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonDatabase implements HaushaltDatabase {
    private List<Item> items;
    private List<Recipe> recipes;
    private RecipeSaver recipeSaver = new RecipeSaver();
    private ItemSaver itemSaver = new ItemSaver();


    public JsonDatabase() {
        init();
    }

    @Override
    public void init() {
        ItemLoader itemLoader= new ItemLoader(this);
        RecipeLoader recipeLoader = new RecipeLoader(this);
        try {
            items = new ArrayList<>(Arrays.stream(itemLoader.loadItemsFromFile("/items.json")).toList());
            recipes = new ArrayList<>(Arrays.stream(recipeLoader.loadRecipesFromFile("/recipes.json")).toList());
        } catch (IOException | RecipeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        if (containsRecipe(recipe.getName()))
            return false;

        recipes.add(recipe);
        try {
            recipeSaver.safeRecipes(recipes);
        } catch (IOException e) {
            recipes.remove(recipe);
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean deleteRecipe(String name) {
        if (!containsRecipe(name))
            return false;

        recipes.remove(getRecipeByName(name));
        try {
            recipeSaver.safeRecipes(recipes);
        } catch (IOException e) {
            recipes.add(getRecipeByName(name));
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean addItem(Item item) {
        if (containsItem(item.getName()))
            return false;

        items.add(item);
        try {
            itemSaver.safeItems(items);
        } catch (IOException e) {
            items.remove(item);
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean deleteItem(String name) {
        if (!containsItem(name))
            return false;

        items.remove(getItemByName(name));
        try {
            itemSaver.safeItems(items);
        } catch (IOException e) {
            items.add(getItemByName(name));
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public Item getItemByName(String name) {
        for (Item item: items) {
            if (item.getName().equals(name))
                return item;
        }
        throw new IllegalArgumentException(String.format("Item name '%s' not found", name));
    }

    @Override
    public Recipe getRecipeByName(String name) {
        for (Recipe recipe: recipes) {
            if (recipe.getName().equals(name))
                return recipe;
        }
        throw new IllegalArgumentException(String.format("Recipe name '%s' not found", name));
    }

    @Override
    public Recipe[] getRecipes() {
        return recipes.toArray(new Recipe[0]);
    }

    @Override
    public Item[] getItems() {
        return items.toArray(new Item[0]);
    }

    public boolean containsRecipe(String name) {
        for (Recipe recipe: recipes) {
            if (recipe.getName().equals(name))
                return true;
        }
        return false;
    }

    public boolean containsItem(String name) {
        for (Item item: items) {
            if (item.getName().equals(name))
                return true;
        }
        return false;
    }

}
