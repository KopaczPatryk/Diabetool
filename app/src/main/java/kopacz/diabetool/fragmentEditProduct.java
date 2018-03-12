package kopacz.diabetool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import NullAware.FloatParser;

/**
 * Created by Kopac on 30.05.2016.
 */
public class fragmentEditProduct extends ProductInteractor {
    //private OnProductAcceptedListener listener;

    private long id; //id produktu który ma być aktualizowany

    //konstruktor który zapisuje stan by można było go wczytać w funkcji onCreate() po wywołaniu konstruktora bez argumentów
    public static fragmentEditProduct newInstance(Product entry) {
        return new fragmentEditProduct();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        readProduct();
        return view;
    }

    protected void readProduct() {
        product = (Product) getActivity().getIntent().getSerializableExtra(DatabaseEditProduct.PRODUCT_TAG);

        /*product.resetScale();
        //id = getActivity().getIntent().getLongExtra(DatabaseEditProduct.PRODUCT_ID_TAG, -1);
        name.setText(product.getName());
        energyValue.setText(String.valueOf(product.getEnergyValue()));
        proteins.setText(String.valueOf(product.getProteins()));
        fat.setText(String.valueOf(product.getFat()));
        carbohydrates.setText(String.valueOf(product.getCarbohydrates()));
        roughage.setText(String.valueOf(product.getRoughage()));
        sugar.setText(String.valueOf(product.getSugar()));
        caffeine.setText(String.valueOf(product.getCaffeine()));
        QR.setText(String.valueOf(product.getBarcode()));
        perGrams.setText(String.valueOf(product.getBaseWeight()));
*/

        product.resetScale();
        FloatFormatter ff = new FloatFormatter();
        //id = product.getId();
        name.setText(product.getName());
        Log.v("formater", ff.format(product.getEnergyValue()));
        energyValue.setText(isNull(ff.format(product.getEnergyValue())));
        proteins.setText(isNull(ff.format(product.getProteins())));
        fat.setText(isNull(ff.format(product.getFat())));
        carbohydrates.setText(isNull(ff.format(product.getCarbohydrates())));
        roughage.setText(isNull(ff.format(product.getRoughage())));
        sugar.setText(isNull(ff.format(product.getSugar())));
        caffeine.setText(isNull(ff.format(product.getCaffeine())));
        QR.setText(isNull(String.valueOf(product.getBarcode())));
        perGrams.setText(ff.format(product.getBaseWeight()));
    }

    @Override
    protected void performCallback() {
        Product tempEntry = new Product();
        //tempEntry.setId(id);
        tempEntry.setName(name.getText().toString());
        tempEntry.setEnergyValue(FloatParser.parse(energyValue.getText().toString()));
        tempEntry.setProteins(FloatParser.parse(proteins.getText().toString()));
        tempEntry.setFat(FloatParser.parse((fat.getText().toString())));
        tempEntry.setCarbohydrates(FloatParser.parse(carbohydrates.getText().toString()));
        tempEntry.setRoughage(FloatParser.parse(roughage.getText().toString()));
        tempEntry.setSugar(FloatParser.parse(sugar.getText().toString()));
        tempEntry.setCaffeine(FloatParser.parse(caffeine.getText().toString()));
        tempEntry.setBarcode(QR.getText().toString());
        tempEntry.setBaseWeight(Float.parseFloat(perGrams.getText().toString()));
        listener.OnProductAccepted(tempEntry);
    }
}