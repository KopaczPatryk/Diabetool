package kopacz.diabetool;

/**
 * Created by Kopac on 08.11.2016.
 */

interface CookedProduct {
    String getName();
    float getEnergyValue();
    float getFat();
    float getProteins();
    String getQr();
    float getRoughage();
    float getSugar();
    float getCaffeine();
    float getCarbohydrates();
    float getWeight();
    void scaleTo ();
}
