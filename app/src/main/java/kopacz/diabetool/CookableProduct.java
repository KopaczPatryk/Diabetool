package kopacz.diabetool;

/**
 * Created by Kopac on 08.11.2016.
 */

public interface CookableProduct extends CookedProduct {
    void setName(String name);
    void setEnergyValue(float energyValue);
    void setFat(float fat);
    void setProteins(float proteins);
    void setQr(String qr);
    void setRoughage(float roughage);
    void setSugar(float sugar);
    void setCaffeine(float caffeine);
    void setCarbohydrates(float carbohydrates);
    void setWeight(float weight);
}
