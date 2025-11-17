package oschdi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item implements Identifiable {
    private String name;
    private String defaultUnit;
    private float defaultAmount;
    private boolean fresh = true;

    public Item() {
    }

    public Item(String name, String defaultUnit) {
        this.name = name;
        this.defaultUnit = defaultUnit;
    }
    public Item(String name, String defaultUnit, boolean fresh) {
        this.name = name;
        this.defaultUnit = defaultUnit;
        this.fresh = fresh;
    }

    public Item(String name, String defaultUnit, float defaultAmount, boolean fresh) {
        this.name = name;
        this.defaultUnit = defaultUnit;
        this.defaultAmount = defaultAmount;
        this.fresh = fresh;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Item))
            return false;
        Item other = (Item) o;
        return this.name.equals(other.getName())
                && this.defaultUnit.equals(other.getDefaultUnit());
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultUnit() {
        return defaultUnit;
    }

    public void setDefaultUnit(String defaultUnit) {
        this.defaultUnit = defaultUnit;
    }

    public float getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(float defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public boolean isFresh() {
        return fresh;
    }

    public void setFresh(boolean fresh) {
        this.fresh = fresh;
    }
}
