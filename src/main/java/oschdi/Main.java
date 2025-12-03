package oschdi;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import oschdi.controller.ItemController;
import oschdi.controller.RecipeController;
import oschdi.database.HaushaltDatabase;
import oschdi.database.JsonDatabase;
import oschdi.database.MongoHaushaltDatabase;
import oschdi.shopping.ShoppingList;

@SpringBootApplication(
    exclude = {
        org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration.class,
        org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration.class
})
public class Main {

    public static void main(String[] args) throws Exception {
        HaushaltDatabase database = new MongoHaushaltDatabase();
        // HaushaltDatabase database = new JsonDatabase();
        RecipeController.setDatabase(database);
        ShoppingList shoppingList = new ShoppingList(database);
        ItemController.init(database, shoppingList);
        SpringApplication.run(Main.class, args);
    }

}
