package oschdi.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import oschdi.database.HaushaltDatabase;
import oschdi.model.Ingredient;
import oschdi.model.Item;

import java.io.IOException;
import java.io.InputStream;

public class ItemLoader {
    private ObjectMapper mapper = new ObjectMapper();
    private HaushaltDatabase database;

    public ItemLoader(HaushaltDatabase database) {
        this.database = database;
    }

    public Item[] loadItemsFromFile(String path) throws IOException {
        try {
            InputStream inputStream = getClass().getResourceAsStream(path);
            if (inputStream == null) {
                throw new IOException(String.format("Resource %s not found!", path));
            }
            return convertJsonItemsToItems(mapper.readValue(inputStream, JsonItem[].class));
        } catch (Exception e) {
            throw new IOException(String.format("Loading items from file %s failed!", path));
        }

    }

    public Item loadItemFromJson(String jsonString) throws IOException {
        try {
            return convertJsonItemToItem(mapper.readValue(jsonString, JsonItem.class));
        } catch (Exception e) {
            throw new IOException("Loading item failed!");
        }
    }


    public JsonItem[] loadJsonItemsFromString(String jsonString) {
        try {
            return mapper.readValue(jsonString, JsonItem[].class);
        } catch (Exception e) {
            throw new RuntimeException("Parsing json string failed!");
        }
    }

    private Item[] convertJsonItemsToItems(JsonItem[] jsonItems) {
        Item[] result = new Item[jsonItems.length];
        for (int i = 0; i < jsonItems.length; i++) {
            result[i] = convertJsonItemToItem(jsonItems[i]);
        }
        return result;
    }
    private Item convertJsonItemToItem(JsonItem jsonItem) {
        return new Item(jsonItem.getName(),
                jsonItem.getUnitString(),
                jsonItem.getAmount(),
                jsonItem.isFresh());
    }


    public JsonItem[] convertItemsToJsonItems(Item[] items) {
        JsonItem[] result = new JsonItem[items.length];
        for (int i = 0; i < items.length; i++) {
            result[i] = new JsonItem(items[i].getName(),
                    items[i].getDefaultUnit(),
                    items[i].getDefaultAmount(),
                    items[i].isFresh());
        }
        return result;
    }

    public Ingredient[] convertJsonItemsToIngredients(JsonItem[] jsonItems) {
        Ingredient[] result = new Ingredient[jsonItems.length];
        for (int i = 0; i < jsonItems.length; i++) {
            Item item = database.getItemByName(jsonItems[i].getName());
            result[i] = new Ingredient(item, jsonItems[i].getAmount());
        }
        return result;
    }

    public JsonItem[] convertIngredientsToJsonItem(Ingredient[] ingredients) {
        JsonItem[] result = new JsonItem[ingredients.length];
        for (int i = 0; i < ingredients.length; i++) {
            result[i] = new JsonItem(ingredients[i].getItem().getName(),
                    ingredients[i].getUnit(),
                    ingredients[i].getAmount(),
                    ingredients[i].getItem().isFresh());
        }
        return result;
    }
}
