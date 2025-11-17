package oschdi.parser;

/**
 * Used to serialize ingredients
 */
public class JsonItem {
    private String name;
    private String unitString;
    private float amount;
    private boolean fresh;

    public JsonItem() {
        this.name = "";
        this.unitString = "COUNT";
        this.amount = 1; //used as defaultAmount if not specified
        this.fresh = true;
    }

    public JsonItem(String name, String unitString, float amount, boolean fresh) {
        this.name = name;
        this.unitString = unitString;
        this.amount = amount;
        this.fresh = fresh;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitString() {
        return unitString;
    }

    public void setUnitString(String unitString) {
        this.unitString = unitString;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public boolean isFresh() {
        return fresh;
    }

    public void setFresh(boolean fresh) {
        this.fresh = fresh;
    }
}
