package kopacz.diabetool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import NullAware.BooleanInterpreter;

/**
 * Created by Kopac on 03.10.2016.
 */

class MergedDatabaseHelper implements DatabaseHelper {
    private static String TAG = MergedDatabaseHelper.class.getSimpleName();
    //private final String DB_QUERY_MASK;
    private static MergedDatabaseHelper instance;
    private SQLiteDatabase dbLocal;

    private QueryPreProcessor qprocessor = new QueryPreProcessor(3);

    public MergedDatabaseHelper(Context context) {
        DatabaseLocalHolder localholder = DatabaseLocalHolder.getInstance(context);

        dbLocal = localholder.getWritableDatabase();

        //String statement = "ATTACH DATABASE '" + localholder.getDBPath() + "' AS 'local'";
        //dbLocal.execSQL(statement);
    }
    static MergedDatabaseHelper getInstance(Context ctx) {
        if (instance == null) {
            instance = new MergedDatabaseHelper(ctx);
        }
        return instance;
    }

    @Override
    public long addProduct(Product entry) {
        entry.resetScale();
        if (dbLocal.isOpen())
        {
            ContentValues cv = entry.toContentValues();
            cv.remove(DatabaseSchema.PRODUCT_ID);
            return dbLocal.insertOrThrow(DatabaseSchema.PRODUCT_LOCAL_TABLE, null, cv);
        }
        else
        {
            return -1;
        }
    }

    @Override
    public boolean editProduct(long id, Product entry) {
        entry.resetScale();
        ContentValues cv = entry.toContentValues();
        cv.remove(DatabaseSchema.PRODUCT_ID);
        cv.remove(DatabaseSchema.PRODUCT_RID);
        dbLocal.update(DatabaseSchema.PRODUCT_LOCAL_TABLE, cv, DatabaseSchema.PRODUCT_ID + "=" + id, null);
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Cursor queryProductsContainingName(String name, boolean privateOnly, boolean showFavourites) {
        if (dbLocal.isOpen()) {
            try {
                qprocessor.preProcess(name);
                String query = qprocessor.getLikesQueryIncludingBarcode(DatabaseSchema.PRODUCT_NAME, DatabaseSchema.PRODUCT_BARCODE);
                Log.v("preprocek", "--" + query);
                //Log.v("preprocek2", "--" + "".substring(0,4));

                //f,f
                // f, t - fav
                //t, f - us
                //t,t - b
                //Log.v("debuggs", " " + privateOnly + ", " + showFavourties);

                //return dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.PRODUCT_LOCAL_TABLE + " WHERE ((" + DatabaseSchema.PRODUCT_RID + " = " + BooleanInterpreter.toLong(!privateOnly) + " | " + DatabaseSchema.PRODUCT_FAVOURITE + "=" + BooleanInterpreter.toLong(showFavourties) + ") AND `Nazwa` LIKE '%" + name + "%') ORDER BY " + DatabaseSchema.PRODUCT_NAME + " COLLATE NOCASE ASC", null);

                if (!privateOnly & !showFavourites) { //wszystkie
                    return dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.PRODUCT_LOCAL_TABLE + " WHERE " + /*DatabaseSchema.PRODUCT_NAME +*/ " (" + query + ") ORDER BY " + DatabaseSchema.PRODUCT_NAME + " COLLATE NOCASE ASC", null);
                }
                else if (!privateOnly & showFavourites) { //ulubione bez customów
                    //return dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.PRODUCT_LOCAL_TABLE + " WHERE (" + DatabaseSchema.PRODUCT_RID + " = -1 AND `Nazwa` LIKE '%" + name + "%') ORDER BY " + DatabaseSchema.PRODUCT_NAME + " COLLATE NOCASE ASC", null);
                    return dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.PRODUCT_LOCAL_TABLE + " WHERE (" + DatabaseSchema.PRODUCT_FAVOURITE + " = " + BooleanInterpreter.toLong(showFavourites) + " AND (" + query + ")) ORDER BY " + DatabaseSchema.PRODUCT_NAME + " COLLATE NOCASE ASC", null);
                }
                else if (privateOnly & !showFavourites) //csutomy bez ulubionych
                {
                    return dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.PRODUCT_LOCAL_TABLE + " WHERE (" + DatabaseSchema.PRODUCT_RID + " = " + BooleanInterpreter.toLong(!privateOnly) + " AND (" + query + ")) ORDER BY " + DatabaseSchema.PRODUCT_NAME + " COLLATE NOCASE ASC", null);
                    //return dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.PRODUCT_LOCAL_TABLE + " WHERE ((" + DatabaseSchema.PRODUCT_RID + " = " + BooleanInterpreter.toLong(!privateOnly) + " | " + DatabaseSchema.PRODUCT_FAVOURITE + "=" + BooleanInterpreter.toLong(showFavourties) + ") AND `Nazwa` LIKE '%" + name + "%') ORDER BY " + DatabaseSchema.PRODUCT_NAME + " COLLATE NOCASE ASC", null);
                }
                else if (privateOnly & showFavourites) // ulubione customy
                {
                    return dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.PRODUCT_LOCAL_TABLE + " WHERE (" + DatabaseSchema.PRODUCT_RID + " = " + BooleanInterpreter.toLong(!privateOnly) + " AND " + DatabaseSchema.PRODUCT_FAVOURITE + " = " + BooleanInterpreter.toLong(showFavourites) + " AND (" + query + ")) ORDER BY " + DatabaseSchema.PRODUCT_NAME + " COLLATE NOCASE ASC", null);
                }
            }
            catch (Exception e)
            {
                Log.v(TAG, Arrays.toString(e.getStackTrace()));
            }
        }
        else
        {
            return null;
        }
        return null;
    }

    @Override
    public Cursor queryAllProducts() {
        return null;
    }

    @Override
    public Cursor queryProduct(long id) {
        Cursor cursor = dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.PRODUCT_LOCAL_TABLE + " WHERE " + DatabaseSchema.PRODUCT_ID + "=" + id, null);
        cursor.moveToFirst();
        return cursor;
    }

    @Override
    public long tryDeleteProduct(long id) {
        if (dbLocal.isOpen()) {
            return dbLocal.delete(DatabaseSchema.PRODUCT_LOCAL_TABLE, DatabaseSchema.PRODUCT_ID + "=" + String.valueOf(id), null);
        }
        else {
            return -1;
        }
    }

    long clearDatabase() {
        if (dbLocal.isOpen()) {
            long sum = 0; // sum of rows deleted database-wide

            sum += dbLocal.delete(DatabaseSchema.MEAL_LOCAL_TABLE, "1=1" , null);
            sum += dbLocal.delete(DatabaseSchema.PRODUCT_LOCAL_TABLE, "1=1", null);
            return sum;
        }
        else {
            return -1;
        }
    }

    @Override
    public Cursor getUnsentProducts() {
        return dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.PRODUCT_LOCAL_TABLE + " WHERE " + DatabaseSchema.PRODUCT_SENT + "=0", null);
    }

    @Override
    public long productSetSent(long id) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseSchema.PRODUCT_SENT, 1);
        return dbLocal.update(DatabaseSchema.PRODUCT_LOCAL_TABLE, cv, DatabaseSchema.PRODUCT_ID +"="+ id, null);
    }

    @Override
    public long getProductCount() {
        if (dbLocal.isOpen()) {
            Cursor temp = dbLocal.rawQuery("SELECT " + DatabaseSchema.PRODUCT_ID + " FROM " + "pl_products", null);
            long count = temp.getCount();
            temp.close();
            return count;
        }
        else {
            return -1;
        }
    }

    @Override
    public long setProductFavourite(long id, boolean bool) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseSchema.PRODUCT_FAVOURITE, BooleanInterpreter.toLong(bool));
        return dbLocal.update(DatabaseSchema.PRODUCT_LOCAL_TABLE, cv, DatabaseSchema.PRODUCT_ID + "=" + id, null);
    }

    /* ---------------------------------------------------------------------- */
    /* ---------------------------------------------------------------------- */
    /* ------------------------------POSIŁKI--------------------------------- */
    /* ---------------------------------------------------------------------- */
    /* ---------------------------------------------------------------------- */
    @Override
    public long addMeal(MealEntry mealEntry) {
        ContentValues cv = new ContentValues();
        //cv.put(DatabaseSchema.MEAL_ID, mealEntry.getId());
        cv.put(DatabaseSchema.MEAL_RID, mealEntry.getRid());
        cv.put(DatabaseSchema.MEAL_NAME, mealEntry.getName());
        cv.put(DatabaseSchema.DATE_CREATED, mealEntry.getDateCreatedRaw());
        cv.put(DatabaseSchema.MEAL_INGREDIENT_IDS, mealEntry.getIngredientids());
        cv.put(DatabaseSchema.MEAL_INGREDIENT_WEIGHTS, mealEntry.getIngredientWeights());
        cv.put(DatabaseSchema.MEAL_WEIGHT, mealEntry.getOveralWeight());
        cv.put(DatabaseSchema.MEAL_SENT, mealEntry.isSent());
        //dbShipped.beginTransaction();
        if (dbLocal.isOpen())
        {
            return dbLocal.insertOrThrow(DatabaseSchema.MEAL_LOCAL_TABLE, null, cv);
        }
        else
        {
            return -1;
        }
    }

    @Override
    public long addMeal(Meal meal) {
        return addMeal(meal.getMealOutline());
    }

    @Override
    public boolean editMeal(MealEntry mealEntry) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseSchema.MEAL_ID, mealEntry.getId());
        cv.put(DatabaseSchema.MEAL_NAME, mealEntry.getName());
        cv.put(DatabaseSchema.MEAL_INGREDIENT_IDS, mealEntry.getIngredientids());
        cv.put(DatabaseSchema.MEAL_INGREDIENT_WEIGHTS, mealEntry.getIngredientWeights());
        cv.put(DatabaseSchema.MEAL_WEIGHT, mealEntry.getOveralWeight());
        cv.put(DatabaseSchema.MEAL_SENT, mealEntry.isSent());

        //dbShipped.update(DatabaseSchema.MEAL_SHIPPED_TABLE, cv, DatabaseSchema.MEAL_ID + "=" + mealEntry.getId(), null);
        try {
            dbLocal.update(DatabaseSchema.MEAL_LOCAL_TABLE, cv, DatabaseSchema.MEAL_ID + "=" + mealEntry.getId(), null);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean editMeal(Meal meal) {
        return editMeal(meal.getMealOutline());
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Cursor queryMealsContainingName(String trim, boolean privateOnly, boolean showFavourites) {
        Log.v("MergedDatabaseHelper", "Mealse queried");
        qprocessor.preProcess(trim);
        String query = qprocessor.getLikesQuery(DatabaseSchema.MEAL_NAME);

        if (dbLocal.isOpen()) {
            try {
                //f, f
                //f, t - fav
                //t, f - us
                //t, t - b
                //Log.v("debuggs", " " + privateOnly + ", " + showFavourties);

                //return dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.PRODUCT_LOCAL_TABLE + " WHERE ((" + DatabaseSchema.PRODUCT_RID + " = " + BooleanInterpreter.toLong(!privateOnly) + " | " + DatabaseSchema.PRODUCT_FAVOURITE + "=" + BooleanInterpreter.toLong(showFavourties) + ") AND `Nazwa` LIKE '%" + name + "%') ORDER BY " + DatabaseSchema.PRODUCT_NAME + " COLLATE NOCASE ASC", null);
                //return dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.MEAL_LOCAL_TABLE, null);

                if (!privateOnly & !showFavourites) { //wszystkie
                    return dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.MEAL_LOCAL_TABLE + " WHERE " + /*DatabaseSchema.MEAL_NAME*/ " (" + query + ") ORDER BY " + DatabaseSchema.MEAL_NAME + " COLLATE NOCASE ASC", null);
                    //return dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.MEAL_LOCAL_TABLE + " WHERE " + DatabaseSchema.MEAL_NAME + " LIKE '%" + trim + "%' ORDER BY " + DatabaseSchema.MEAL_NAME + " COLLATE NOCASE ASC", null);
                }
                else if (!privateOnly & showFavourites) { //ulubione bez customów
                    return dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.MEAL_LOCAL_TABLE + " WHERE (" + DatabaseSchema.MEAL_FAVOURITE + " = " + BooleanInterpreter.toLong(showFavourites) + " AND (" + query + ")) ORDER BY " + DatabaseSchema.MEAL_NAME + " COLLATE NOCASE ASC", null);
                    //return dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.MEAL_LOCAL_TABLE + " WHERE (" + DatabaseSchema.MEAL_FAVOURITE + " = " + BooleanInterpreter.toLong(showFavourites) + " AND " + DatabaseSchema.MEAL_NAME + " LIKE '%" + trim + "%') ORDER BY " + DatabaseSchema.MEAL_NAME + " COLLATE NOCASE ASC", null);
                }
                else if (privateOnly & !showFavourites) //csutomy bez ulubionych
                {
                    return dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.MEAL_LOCAL_TABLE + " WHERE (" + DatabaseSchema.MEAL_RID + " = " + BooleanInterpreter.toLong(!privateOnly) + " AND (" + query + ")) ORDER BY " + DatabaseSchema.MEAL_NAME + " COLLATE NOCASE ASC", null);
                    //return dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.MEAL_LOCAL_TABLE + " WHERE (" + DatabaseSchema.MEAL_RID + " = " + BooleanInterpreter.toLong(!privateOnly) + " AND " + DatabaseSchema.MEAL_NAME + " LIKE '%" + trim + "%') ORDER BY " + DatabaseSchema.MEAL_NAME + " COLLATE NOCASE ASC", null);
                }
                else if (privateOnly & showFavourites) // ulubione customy
                {
                    return dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.MEAL_LOCAL_TABLE + " WHERE (" + DatabaseSchema.MEAL_RID + " = " + BooleanInterpreter.toLong(!privateOnly) + " AND " + DatabaseSchema.MEAL_FAVOURITE + " = " + BooleanInterpreter.toLong(showFavourites) + " AND (" + query + ")) ORDER BY " + DatabaseSchema.MEAL_NAME + " COLLATE NOCASE ASC", null);
                }
            }
            catch (Exception e)
            {
                Log.v(TAG, Arrays.toString(e.getStackTrace()));
            }
        }
        else
        {
            return null;
        }
        return null;

        //oryginał
        //return dbLocal.rawQuery("SELECT " + "*" + " FROM " + DatabaseSchema.MEAL_LOCAL_TABLE + " WHERE " + DatabaseSchema.MEAL_NAME + " LIKE '%" + name + "%' ORDER BY " + DatabaseSchema.MEAL_NAME + " COLLATE NOCASE ASC", null);
    }

    @Override
    public long tryDeleteMeal(long id) {
        if (dbLocal.isOpen()) {
            return dbLocal.delete(DatabaseSchema.MEAL_LOCAL_TABLE, DatabaseSchema.MEAL_ID + "=" + String.valueOf(id), null);
        }
        else {
            return -1;
        }
    }

    @Override
    public Meal getMeal(MealEntry mo) {
        Meal meal = new Meal();
        meal.setId(mo.getId());
        meal.setName(mo.getName());
        Cursor c;
        if (dbLocal.isOpen())
        {
            c = dbLocal.rawQuery("SELECT * FROM " + DatabaseSchema.PRODUCT_LOCAL_TABLE + " WHERE " + DatabaseSchema.PRODUCT_ID + " REGEXP '" + mo.getIngredientids() + "' ORDER BY " + DatabaseSchema.PRODUCT_NAME + " COLLATE NOCASE ASC", null);
            //Log.v(TAG, mo.getIngredientids());
        }
        else
        {
            return null;
        }

        ArrayList<Product> products = Product.decomposeCursorToArrayList(c);
        meal.setProducts(products);
        Log.v("MergedDatabaseHelper", "" + meal.getIngredients().size());
        String[] weights = mo.getIngredientWeights().split("\\|");
        for (int i = 0; i < products.size(); i++)
        {
            meal.getProduct(i).setScale(Float.parseFloat(weights[i]));
            //Log.v("przypis", "" + weights[i]);

            //Product ingredient = new Product(products.get(i), Float.parseFloat(weights[i]));
            //Product product = products.get(i);
            //product.setBaseWeight(Float.parseFloat(weights[i]));
            //meal.addProduct(product);
        }
        return meal;
    }

    public long getMealCount() {
        if (dbLocal.isOpen()) {
            Cursor temp = dbLocal.rawQuery("SELECT " + DatabaseSchema.MEAL_ID + " FROM " + DatabaseSchema.MEAL_LOCAL_TABLE, null);
            long count = temp.getCount();
            temp.close();
            return count;
        }
        else {
            return -1;
        }
    }

    @Override
    public long setMealFavourite(long id, boolean bool) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseSchema.MEAL_FAVOURITE, BooleanInterpreter.toLong(bool));
//        if (bool)
//        {
//            cv.put(DatabaseSchema.MEAL_FAVOURITE, 1);
//        }
//        else {
//            cv.put(DatabaseSchema.MEAL_FAVOURITE, 0);
//        }
        return dbLocal.update(DatabaseSchema.MEAL_LOCAL_TABLE, cv, DatabaseSchema.MEAL_ID + "=" + id, null);
    }

    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = dbLocal;
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } /*catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }*/ catch(Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}
