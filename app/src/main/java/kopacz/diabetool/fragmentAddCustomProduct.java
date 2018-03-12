package kopacz.diabetool;

import NullAware.FloatParser;

/**
 * Created by Kopac on 30.05.2016.
 */
public class fragmentAddCustomProduct extends ProductInteractor {
    //konstruktor który zapisuje stan by można było go wczytać w funkcji onCreate() po wywołaniu konstruktora bez argumentów
    public static fragmentAddCustomProduct newInstance() {
        return new fragmentAddCustomProduct();
    }

    @Override
    protected void performCallback() {
        Product tempEntry = new Product();
        tempEntry.setDateCreated(System.currentTimeMillis());
        tempEntry.setName(name.getText().toString());

        tempEntry.setEnergyValue(FloatParser.parse(energyValue.getText().toString()));
        tempEntry.setProteins(FloatParser.parse(proteins.getText().toString()));
        tempEntry.setFat(FloatParser.parse(fat.getText().toString()));
        tempEntry.setCarbohydrates(FloatParser.parse(carbohydrates.getText().toString()));
        tempEntry.setRoughage(FloatParser.parse(roughage.getText().toString()));
        tempEntry.setSugar(FloatParser.parse(sugar.getText().toString()));
        tempEntry.setCaffeine(FloatParser.parse(caffeine.getText().toString()));
        /*
        tempEntry.setEnergyValue(Float.parseFloat(energyValue.getText().toString()));
        tempEntry.setProteins(Float.parseFloat(proteins.getText().toString()));
        tempEntry.setFat(Float.parseFloat((fat.getText().toString())));
        tempEntry.setCarbohydrates(Float.parseFloat(carbohydrates.getText().toString()));
        tempEntry.setRoughage(Float.parseFloat(roughage.getText().toString()));
        tempEntry.setSugar(Float.parseFloat(sugar.getText().toString()));
        tempEntry.setCaffeine(Float.parseFloat(caffeine.getText().toString()));*/
        tempEntry.setBarcode(QR.getText().toString());
        tempEntry.setBaseWeight(Float.parseFloat(perGrams.getText().toString()));
        listener.OnProductAccepted(tempEntry);
    }
}
