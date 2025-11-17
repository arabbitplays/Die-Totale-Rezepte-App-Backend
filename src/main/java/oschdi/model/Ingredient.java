package oschdi.model;

public class Ingredient {
    private Item item;
    private float amount;
    private String unit;

    public Ingredient() {

    }

    public Ingredient(Item item, float amount, String unit) {
        this.item = item;
        this.amount = amount;
        this.unit = unit;
    }

    public Ingredient(Item item, float amount) {
        this.item = item;
        this.amount = amount;
        this.unit = item.getDefaultUnit();
    }

    public String getUnit() { return unit; }

    public Item getItem() {
        return item;
    }

    public float getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Ingredient))
            return false;
        Ingredient other = (Ingredient) o;
        return this.item.equals(other.getItem())
                && this.amount == other.getAmount()
                && this.unit.equals(other.getUnit());
    }
}
