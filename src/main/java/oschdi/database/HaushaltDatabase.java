package oschdi.database;

import oschdi.model.Item;
import oschdi.model.Recipe;

public interface HaushaltDatabase {
    void init();

    boolean addRecipe(Recipe recipe);
    boolean deleteRecipe(String name);

    boolean addItem(Item item);
    boolean deleteItem(String name);

    Item getItemByName(String name);

    Recipe getRecipeByName(String name);

    Recipe[] getRecipes();

    Item[] getItems();
}
