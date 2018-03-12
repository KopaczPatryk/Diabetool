package kopacz.diabetool;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import NullAware.BooleanParser;

public class Product extends Edible implements Serializable, Packable {
    //private String name;
    //private float weight; //waga produktu (wartość bazowa - ma nie być zmieniana przy skalowaniu wagi)
    //private float scaleWeight; // waga do której produkt ma być skalowany, domyślnie równe wadze (wtedy wartości się skracają)
    private String barcode;

    public Product() {
        super();
        //this.weight = 100f;
        //this.scaleWeight = 100f;
        this.barcode = "";
    }

    public Product(long id, long rid, boolean sent, long date_created, boolean favourite, String name, float energyValue, float proteins, float fat, float carbohydrates, float roughage, float sugar, float caffeine, float weight, float weightScale, String barcode) {
        super(id, rid, sent, date_created, favourite, name, energyValue, proteins, fat, carbohydrates, roughage, sugar, caffeine, weightScale, weight);
        //this.weight = weight;
        //this.scaleWeight = weight;
        //this.setBaseWeight(weight);
        //this.setScale(weight);
        this.barcode = barcode;
    }

    public Product(Product product) {
        //super(product.getId(), product.getRid(), product.isSent(), product.getDateCreatedRaw(), product.isFavourite());
        super(product.getId(), product.getRid(), product.isSent(), product.getDateCreatedRaw(), product.isFavourite(),
                product.getName(), product.getEnergyValue(), product.getProteins(), product.getFat(), product.getCarbohydrates(), product.getRoughage(),
                product.getSugar(), product.getCaffeine(), product.getScale(), product.getBaseWeight());
        this.barcode = product.getBarcode();
        //this.weight = product.getBaseWeight();
    }

    public Product(Bundle bundle) { //todo skasować bo to do dupy - serializable
        this.setId(bundle.getLong(DatabaseSchema.PRODUCT_ID));
        this.setRid(bundle.getLong(DatabaseSchema.PRODUCT_RID));
        this.setName(bundle.getString(DatabaseSchema.PRODUCT_NAME));
        this.setEnergyValue(bundle.getFloat(DatabaseSchema.PRODUCT_ENERGYVALUE));
        this.setProteins(bundle.getFloat(DatabaseSchema.PRODUCT_PROTEINS));
        this.setFat(bundle.getFloat(DatabaseSchema.PRODUCT_FAT));
        this.setCarbohydrates(bundle.getFloat(DatabaseSchema.PRODUCT_CARBOHYDRATES));
        this.setRoughage(bundle.getFloat(DatabaseSchema.PRODUCT_ROUGHAGE));
        this.setSugar(bundle.getFloat(DatabaseSchema.PRODUCT_SUGAR));
        this.setCaffeine(bundle.getFloat(DatabaseSchema.PRODUCT_CAFFEINE));
        this.setBarcode(bundle.getString(DatabaseSchema.PRODUCT_BARCODE));
        this.setSent(bundle.getBoolean(DatabaseSchema.PRODUCT_SENT));
    }

    static Product getDatabaseEntry(Cursor cursor)
    {
        return decomposeCursor(cursor);
    }

    @NonNull
    private static Product decomposeCursor(Cursor cursor) {
        String nofID = DatabaseSchema.PRODUCT_ID;
        String nofRID = DatabaseSchema.PRODUCT_RID;
        String nofSent = DatabaseSchema.PRODUCT_SENT;
        String nofDateCreated = DatabaseSchema.DATE_CREATED;
        String nofFavourite = DatabaseSchema.PRODUCT_FAVOURITE;

        String nofName = DatabaseSchema.PRODUCT_NAME;
        String nofEnergyValue = DatabaseSchema.PRODUCT_ENERGYVALUE;
        String nofProteins = DatabaseSchema.PRODUCT_PROTEINS;
        String nofFat = DatabaseSchema.PRODUCT_FAT;
        String nofCarbohydrates = DatabaseSchema.PRODUCT_CARBOHYDRATES;
        String nofRoughage = DatabaseSchema.PRODUCT_ROUGHAGE;
        String nofSugar = DatabaseSchema.PRODUCT_SUGAR;
        String nofCaffeine = DatabaseSchema.PRODUCT_CAFFEINE;
        String nofQR = DatabaseSchema.PRODUCT_BARCODE;
        String nofWeight = DatabaseSchema.PRODUCT_WEIGHT;

        //cache of positions
        int iofID = cursor.getColumnIndex(nofID);
        int iofRID = cursor.getColumnIndex(nofRID);
        int iofSent = cursor.getColumnIndex(nofSent);
        int iofDateCreated = cursor.getColumnIndex(nofDateCreated);
        int iofFavourite = cursor.getColumnIndex(nofFavourite);

        int iofName = cursor.getColumnIndex(nofName);
        int iofEnergyValue = cursor.getColumnIndex(nofEnergyValue);
        int iofProteins = cursor.getColumnIndex(nofProteins);
        int iofFat = cursor.getColumnIndex(nofFat);
        int iofCarbohydrates = cursor.getColumnIndex(nofCarbohydrates);
        int iofRoughage = cursor.getColumnIndex(nofRoughage);
        int iofSugar = cursor.getColumnIndex(nofSugar);
        int iofCaffeine = cursor.getColumnIndex(nofCaffeine);
        int iofQR = cursor.getColumnIndex(nofQR);
        int iofWeight = cursor.getColumnIndex(nofWeight);

        Product tempEntry = new Product();
        tempEntry.setId(cursor.getLong(iofID));
        tempEntry.setRid(cursor.getLong(iofRID));
        tempEntry.setSent(BooleanParser.parse(cursor.getInt(iofSent)));
        tempEntry.setDateCreated(iofDateCreated);
        tempEntry.setFavourite(BooleanParser.parse(cursor.getInt(iofFavourite)));

        tempEntry.setName(cursor.getString(iofName));

        tempEntry.setEnergyValue(cursor.getFloat(iofEnergyValue));
        tempEntry.setProteins(cursor.getFloat(iofProteins));
        tempEntry.setFat(cursor.getFloat(iofFat));
        tempEntry.setCarbohydrates(cursor.getFloat(iofCarbohydrates));
        tempEntry.setRoughage(cursor.getFloat(iofRoughage));
        tempEntry.setSugar(cursor.getFloat(iofSugar));
        tempEntry.setCaffeine(cursor.getFloat(iofCaffeine));

        tempEntry.setBarcode(cursor.getString(iofQR));
        tempEntry.setBaseWeight(cursor.getFloat(iofWeight));
        //cursor.close(); // chyba lepiej nie zamykać bo skasuje cursor który jest gdzieś używany
        return tempEntry;
    }

    static ArrayList<Product> decomposeCursorToArrayList(Cursor cursor) {
        //cache of ids
        String nofID = DatabaseSchema.PRODUCT_ID;
        String nofRID = DatabaseSchema.PRODUCT_RID;
        String nofSent = DatabaseSchema.PRODUCT_SENT;
        String nofDateCreated = DatabaseSchema.DATE_CREATED;
        String nofFavourite = DatabaseSchema.PRODUCT_FAVOURITE;

        String nofName = DatabaseSchema.PRODUCT_NAME;
        String nofEnergyValue = DatabaseSchema.PRODUCT_ENERGYVALUE;
        String nofProteins = DatabaseSchema.PRODUCT_PROTEINS;
        String nofFat = DatabaseSchema.PRODUCT_FAT;
        String nofCarbohydrates = DatabaseSchema.PRODUCT_CARBOHYDRATES;
        String nofRoughage = DatabaseSchema.PRODUCT_ROUGHAGE;
        String nofSugar = DatabaseSchema.PRODUCT_SUGAR;
        String nofCaffeine = DatabaseSchema.PRODUCT_CAFFEINE;
        String nofQR = DatabaseSchema.PRODUCT_BARCODE;
        String nofWeight = DatabaseSchema.PRODUCT_WEIGHT;

        //cache of positions
        int iofID = cursor.getColumnIndex(nofID);
        int iofRID = cursor.getColumnIndex(nofRID);
        int iofSent = cursor.getColumnIndex(nofSent);
        int iofDateCreated = cursor.getColumnIndex(nofDateCreated);
        int iofFavourite = cursor.getColumnIndex(nofFavourite);

        int iofName = cursor.getColumnIndex(nofName);
        int iofEnergyValue = cursor.getColumnIndex(nofEnergyValue);
        int iofProteins = cursor.getColumnIndex(nofProteins);
        int iofFat = cursor.getColumnIndex(nofFat);
        int iofCarbohydrates = cursor.getColumnIndex(nofCarbohydrates);
        int iofRoughage = cursor.getColumnIndex(nofRoughage);
        int iofSugar = cursor.getColumnIndex(nofSugar);
        int iofCaffeine = cursor.getColumnIndex(nofCaffeine);
        int iofQR = cursor.getColumnIndex(nofQR);
        int iofWeight = cursor.getColumnIndex(nofWeight);

        ArrayList<Product> products = new ArrayList<>();

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Product tempEntry = new Product();

            tempEntry.setId(cursor.getLong(iofID));
            tempEntry.setRid(cursor.getLong(iofRID));
            tempEntry.setSent(BooleanParser.parse(cursor.getInt(iofSent)));
            tempEntry.setDateCreated(cursor.getLong(iofDateCreated));
            tempEntry.setFavourite(BooleanParser.parse(cursor.getInt(iofFavourite)));

            tempEntry.setName(cursor.getString(iofName));
            tempEntry.setEnergyValue(cursor.getFloat(iofEnergyValue));
            tempEntry.setProteins(cursor.getFloat(iofProteins));
            tempEntry.setFat(cursor.getFloat(iofFat));
            tempEntry.setCarbohydrates(cursor.getFloat(iofCarbohydrates));
            tempEntry.setRoughage(cursor.getFloat(iofRoughage));
            tempEntry.setSugar(cursor.getFloat(iofSugar));
            tempEntry.setCaffeine(cursor.getFloat(iofCaffeine));
            tempEntry.setBarcode(cursor.getString(iofQR));
            tempEntry.setBaseWeight(cursor.getFloat(iofWeight));
            tempEntry.setScale(cursor.getFloat(iofWeight));

            products.add(tempEntry);
            cursor.moveToNext();
        }
        //cursor.close(); // chyba lepiej nie zamykać bo skasuje cursor który jest gdzieś używany
        return products;
    }

    public Bundle getBundle () { //todo też wywalić
        Bundle bundle = new Bundle();
        bundle.putLong(DatabaseSchema.PRODUCT_ID, getId());
        bundle.putLong(DatabaseSchema.PRODUCT_RID, getRid());
        bundle.putString(DatabaseSchema.PRODUCT_NAME, getName());

        bundle.putFloat(DatabaseSchema.PRODUCT_ENERGYVALUE, getEnergyValue());
        bundle.putFloat(DatabaseSchema.PRODUCT_PROTEINS, getProteins());
        bundle.putFloat(DatabaseSchema.PRODUCT_FAT, getFat());
        bundle.putFloat(DatabaseSchema.PRODUCT_CARBOHYDRATES, getCarbohydrates());
        bundle.putFloat(DatabaseSchema.PRODUCT_ROUGHAGE, getRoughage());
        bundle.putFloat(DatabaseSchema.PRODUCT_SUGAR, getSugar());
        bundle.putFloat(DatabaseSchema.PRODUCT_CAFFEINE, getCaffeine());

        bundle.putString(DatabaseSchema.PRODUCT_BARCODE, barcode);
        bundle.putBoolean(DatabaseSchema.PRODUCT_SENT, isSent());
        return bundle;
    }

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        //todo chyba zbędne
        resetScale();
        cv.put(DatabaseSchema.PRODUCT_ID, getId());
        cv.put(DatabaseSchema.PRODUCT_RID, getRid());
        cv.put(DatabaseSchema.PRODUCT_SENT, isSent());
        cv.put(DatabaseSchema.DATE_CREATED, getDateCreatedRaw());
        cv.put(DatabaseSchema.PRODUCT_FAVOURITE, isFavourite());

        cv.put(DatabaseSchema.PRODUCT_NAME, getName());
        cv.put(DatabaseSchema.PRODUCT_ENERGYVALUE, getEnergyValue());
        cv.put(DatabaseSchema.PRODUCT_PROTEINS, getProteins());
        cv.put(DatabaseSchema.PRODUCT_FAT, getFat());
        cv.put(DatabaseSchema.PRODUCT_CARBOHYDRATES, getCarbohydrates());
        cv.put(DatabaseSchema.PRODUCT_ROUGHAGE, getRoughage());
        cv.put(DatabaseSchema.PRODUCT_SUGAR, getSugar());
        cv.put(DatabaseSchema.PRODUCT_CAFFEINE, getCaffeine());
        cv.put(DatabaseSchema.PRODUCT_BARCODE, getBarcode());
        cv.put(DatabaseSchema.PRODUCT_WEIGHT, getBaseWeight());
        return cv;
    }

    @Override
    public String getBarcode() {
        return barcode;
    }

    @Override
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

//    @Override
//    public float getEnergyValue() {
//        return HelperClass.proportionNullAware(super.getEnergyValue(), weight, scaleWeight);
//    }
//
//    @Override
//    public float getProteins() {
//        return HelperClass.proportionNullAware(super.getProteins(), weight, scaleWeight);
//    }
//
//    @Override
//    public float getFat() {
//        return HelperClass.proportionNullAware(super.getFat(), weight, scaleWeight);
//    }
//
//    @Override
//    public float getCarbohydrates() {
//        return HelperClass.proportionNullAware(super.getCarbohydrates(), weight, scaleWeight);
//    }
//
//    @Override
//    public float getRoughage() {
//        //return getRoughage();
//        //return HelperClass.proportionNullAware(super.getRoughage(), weight, scaleWeight);
//    }
//
//    @Override
//    public float getSugar() {
//        return HelperClass.proportionNullAware(super.getSugar(), weight, scaleWeight);
//    }
//
//    @Override
//    public float getCaffeine() {
//        return HelperClass.proportionNullAware(super.getCaffeine(), weight, scaleWeight);
//    }
}
