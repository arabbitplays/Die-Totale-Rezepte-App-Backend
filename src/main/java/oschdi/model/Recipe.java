package oschdi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Recipe implements Identifiable {
    private String name;
    private Ingredient[] ingredients;
    private String[] prep;
    private String[] process;
    private String[] notes;

    public Recipe() {
        prep = new String[0];
        process = new String[0];
        notes = new String[0];
    }

    public Recipe(String name, Ingredient[] ingredients, String[] prep, String[] process, String[] notes) {
        this.name = name;
        this.ingredients = ingredients;
        this.prep = prep;
        this.process = process;
        this.notes = notes;
    }

    public Ingredient[] getIngredientsScaled(float scale) {
        Ingredient[] result = new Ingredient[ingredients.length];
        for (int i = 0; i < ingredients.length; i++) {
            result[i] = new Ingredient(ingredients[i].getItem(),
                    ingredients[i].getAmount() * scale, ingredients[i].getUnit());
        }
        return result;
    }

    public String[] getPrep() {
        return prep;
    }

    public String[] getProcess() {
        return process;
    }

    public String[] getNotes() {
        return notes;
    }

    @Override
    public String getName() {
        return name;
    }

    public Ingredient[] getIngredients() { return ingredients; }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Recipe))
            return false;
        Recipe other = (Recipe) o;
        return this.name.equals(other.getName())
                && Arrays.equals(this.ingredients, other.getIngredients())
                && Arrays.equals(this.prep, other.getPrep())
                && Arrays.equals(this.process, other.getProcess())
                && Arrays.equals(this.notes, other.getNotes());
    }
}
