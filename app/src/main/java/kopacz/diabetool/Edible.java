package kopacz.diabetool;

/**
 * Created by Kopac on 20.11.2016.
 */

@SuppressWarnings({"WeakerAccess", "DefaultFileTemplate"})
abstract class Edible extends DatabaseEntry {
    private String name;
    private float energyValue;
    private float proteins;
    private float fat;
    private float carbohydrates;
    private float roughage;
    private float sugar;
    private float caffeine;

    protected float weightScale;
    protected float weight;

    public Edible () {
        super();
        this.name = "";
        this.energyValue = -1f;
        this.proteins = -1f;
        this.fat = -1f;
        this.carbohydrates = -1f;
        this.roughage = -1f;
        this.sugar = -1f;
        this.caffeine = -1f;

        this.weight = 100f;
        this.weightScale = 100f;
    }

    public Edible (long id, long rid, boolean sent, long date_created, boolean favourite, String name, float energyValue, float proteins, float fat, float carbohydrates, float roughage, float sugar, float caffeine) {
        super(id, rid, sent, date_created, favourite);
        this.name = name;
        this.energyValue = energyValue;
        this.proteins = proteins;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.roughage = roughage;
        this.sugar = sugar;
        this.caffeine = caffeine;
        this.weight = 100;
        this.weightScale = 100;
    }

    public Edible(long id, long rid, boolean sent, long date_created, boolean favourite, String name, float energyValue, float proteins, float fat, float carbohydrates, float roughage, float sugar, float caffeine, float weightScale, float weight) {
        super(id, rid, sent, date_created, favourite);
        this.name = name;
        this.energyValue = energyValue;
        this.proteins = proteins;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.roughage = roughage;
        this.sugar = sugar;
        this.caffeine = caffeine;
        this.weightScale = weightScale;
        this.weight = weight;
    }

    public String getName () {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getEnergyValue() {
        return HelperClass.proportionNullAware(energyValue, weight, weightScale);
    }

    public void setEnergyValue(float energyValue) {
        this.energyValue = energyValue;
    }

    public float getProteins() {
        return HelperClass.proportionNullAware(proteins, weight, weightScale);
    }

    public void setProteins(float proteins) {
        this.proteins = proteins;
    }

    public float getFat() {
        return HelperClass.proportionNullAware(fat, weight, weightScale);
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getCarbohydrates() {
        return HelperClass.proportionNullAware(carbohydrates, weight, weightScale);
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public float getRoughage() {
        return HelperClass.proportionNullAware(roughage, weight, weightScale);
    }

    public void setRoughage(float roughage) {
        this.roughage = roughage;
    }

    public float getSugar() {
        return HelperClass.proportionNullAware(sugar, weight, weightScale);
    }

    public void setSugar(float sugar) {
        this.sugar = sugar;
    }

    public float getCaffeine() {
        return HelperClass.proportionNullAware(caffeine, weight, weightScale);
    }

    public void setCaffeine(float caffeine) {
        this.caffeine = caffeine;
    }

    //waga dla której są obliczane wartości
    public void setBaseWeight(float weight) {
        this.weight = weight;
    }

    public float getBaseWeight() {
        return this.weight;
    }

    public void setScale(float grams) {
        this.weightScale = grams;
    }

    public float getScale () {
        return this.weightScale;
    }

    public void resetScale() {
        this.weightScale = weight;
    }
}
