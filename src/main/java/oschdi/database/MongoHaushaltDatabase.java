package oschdi.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import oschdi.model.Identifiable;
import oschdi.model.Item;
import oschdi.model.Recipe;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MongoHaushaltDatabase implements HaushaltDatabase {
    private MongoCollection<Document> recipeCollection, itemCollection;
    private ObjectMapper mapper = new ObjectMapper();

    public MongoHaushaltDatabase() {
        init();
    }

    @Override
    public void init() {
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString("mongodb://mongodb:27017"))
                        .build());

        String dbName = "dietotalerezepteapp";
        String[] collectionsToEnsure = {"rezepte", "items"};

        MongoDatabase db = mongoClient.getDatabase(dbName);

        MongoIterable<String> existingCollections = db.listCollectionNames();
        for (String collName : collectionsToEnsure) {
            boolean exists = false;
            for (String existing : existingCollections) {
                if (existing.equals(collName)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                db.createCollection(collName);
                System.out.println("Created collection: " + collName);
            }
        }

        recipeCollection = db.getCollection("rezepte");
        itemCollection = db.getCollection("items");
    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        try {
            insertOrUpdate(recipeCollection, recipe);
        } catch (JsonProcessingException e) {
            System.out.println(String.format("Failed to serialize recipe %s", recipe.getName()));
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteRecipe(String name) {
        Bson filter = Filters.eq("name", name);
        DeleteResult result = recipeCollection.deleteOne(filter);
        return result.getDeletedCount() != 0;
    }

    @Override
    public boolean addItem(Item item) {
        try {
            insertOrUpdate(itemCollection, item);
        } catch (Exception e) {
            System.out.println(String.format("Failed to serialize item %s", item.getName()));
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteItem(String name) {
        Bson filter = Filters.eq("name", name);
        DeleteResult result = itemCollection.deleteOne(filter);
        return result.getDeletedCount() != 0;
    }

    @Override
    public Item getItemByName(String name) {
        Bson filter = Filters.eq("name", name);
        Document itemDoc = itemCollection.find(filter).first();
        if (Objects.isNull(itemDoc)) {
            System.out.println(String.format("Item %s could not be found", name));
            return null;
        }
        try {
            return mapper.readValue(itemDoc.toJson(), Item.class);
        } catch (JsonProcessingException e) {
            System.out.println(String.format("Failed to deserialize item %s", name));
        }
        return null;
    }

    @Override
    public Recipe getRecipeByName(String name) {
        Bson filter = Filters.eq("name", name);
        Document recipeDoc = recipeCollection.find(filter).first();
        if (Objects.isNull(recipeDoc)) {
            System.out.println(String.format("Recipe %s could not be found", name));
            return null;
        }
        try {
            return mapper.readValue(recipeDoc.toJson(), Recipe.class);
        } catch (JsonProcessingException e) {
            System.out.println(String.format("Failed to deserialize recipe %s", name));
        }
        return null;
    }

    @Override
    public Recipe[] getRecipes() {
        List<Recipe> result = new ArrayList<>();

        Bson filter = Filters.exists("_id");
        for (Document recipeDoc: recipeCollection.find(filter)) {
            try {
                result.add(mapper.readValue(recipeDoc.toJson(), Recipe.class));
            } catch (JsonProcessingException e) {
                System.out.println(String.format("Failed to deserialize recipe %s", recipeDoc.get("name")));
            }
        }
        return result.toArray(new Recipe[result.size()]);
    }

    @Override
    public Item[] getItems() {
        List<Item> result = new ArrayList<>();

        Bson filter = Filters.exists("_id");
        for (Document itemDoc: itemCollection.find(filter)) {
            try {
                result.add(mapper.readValue(itemDoc.toJson(), Item.class));
            } catch (JsonProcessingException e) {
                System.out.println(String.format("Failed to deserialize item %s", itemDoc.get("name")));
            }
        }
        return result.toArray(new Item[result.size()]);
    }

    private void insertOrUpdate(MongoCollection<Document> col, Identifiable identifiable) throws JsonProcessingException {
        Bson filter = Filters.eq("name", identifiable.getName());
        Document doc = Document.parse(mapper.writeValueAsString(identifiable));
        if (col.find(filter).first() == null) {
            col.insertOne(doc);
        } else {
            col.replaceOne(filter, doc);
        }
    }
}
