package oschdi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import oschdi.database.HaushaltDatabase;
import oschdi.parser.ItemLoader;
import oschdi.parser.JsonItem;
import oschdi.shopping.ShoppingList;

@RestController()
@RequestMapping("/item")
@CrossOrigin
public class ItemController {
    private static HaushaltDatabase database;
    private static ShoppingList shoppingList;
    private static ItemLoader itemLoader;

    public static void init(HaushaltDatabase database, ShoppingList shoppingList) {
        ItemController.database = database;
        ItemController.shoppingList = shoppingList;
        ItemController.itemLoader = new ItemLoader(database);
    }

    @GetMapping("/getAllItems")
    public JsonItem[] getAllItems() {
        return itemLoader.convertItemsToJsonItems(database.getItems());
    }

    @PutMapping("/addItems")
    public void addIngredientsToList(@RequestBody String jsonString) {
        try {
            shoppingList.addIngredients(itemLoader.convertJsonItemsToIngredients(itemLoader.loadJsonItemsFromString(jsonString)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/removeItems")
    public void removeIngredientsFromList(@RequestBody String jsonString) {
        try {
            shoppingList.removeIngredients(itemLoader.convertJsonItemsToIngredients(itemLoader.loadJsonItemsFromString(jsonString)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/list")
    public JsonItem[] getShoppingList() {
        return itemLoader.convertIngredientsToJsonItem(shoppingList.getIngredients());
    }

    @PostMapping("")
    public ResponseEntity<String> addItem(@RequestBody String jsonString) {
        try {
            if (database.addItem(itemLoader.loadItemFromJson(jsonString))) {
                return new ResponseEntity<>("", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.resolve(500));
        }
        return new ResponseEntity<>("Item already exists!", HttpStatus.resolve(401));
    }

    @DeleteMapping("/{itemName}")
    public ResponseEntity<String> deleteItemByName(@PathVariable("itemName") String name) {
        try {
            if (database.deleteItem(name)) {
                return new ResponseEntity<>("", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.resolve(500));
        }
        return new ResponseEntity<>("Item name not found!", HttpStatus.resolve(401));
    }
}
