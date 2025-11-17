package oschdi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import oschdi.database.HaushaltDatabase;
import oschdi.model.Recipe;
import oschdi.parser.RecipeLoader;

@RestController()
@RequestMapping("/recipe")
@CrossOrigin()
public class RecipeController {
    private static HaushaltDatabase database;
    private static RecipeLoader recipeLoader;
    public static void setDatabase(HaushaltDatabase database) {
        RecipeController.database = database;
        recipeLoader = new RecipeLoader(database);
    }

    @GetMapping("/getAllNames")
    public String[] getAllRecipeNames() {
        Recipe[] recipes = database.getRecipes();
        String[] result = new String[recipes.length];
        for (int i = 0; i < recipes.length; i++) {
            result[i] = recipes[i].getName();
        }
        return result;
    }

    @GetMapping("/{recipeName}")
    public Recipe getRecipeByName(@PathVariable("recipeName") String name) {
        return database.getRecipeByName(name);
    }

    @PostMapping("")
    public ResponseEntity<String> addRecipe(@RequestBody String jsonString) {
        try {
            if (database.addRecipe(recipeLoader.loadRecipeFromJson(jsonString))) {
                return new ResponseEntity<>("", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.resolve(500));
        }
        return new ResponseEntity<>("Recipe already exists!", HttpStatus.resolve(420));
    }

    @DeleteMapping("/{recipeName}")
    public ResponseEntity<String> deleteRecipeByName(@PathVariable("recipeName") String name) {
        try {
            if (database.deleteRecipe(name)) {
                return new ResponseEntity<>("", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.resolve(500));
        }
        return new ResponseEntity<>("Recipe name not found!", HttpStatus.resolve(420));
    }
}
