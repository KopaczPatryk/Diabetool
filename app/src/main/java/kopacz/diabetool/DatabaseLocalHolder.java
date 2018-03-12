package kopacz.diabetool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kopac on 12.05.2016.
 */
public class DatabaseLocalHolder extends SQLiteOpenHelper implements DatabaseHolder {
    //zmienne
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "diabetool_local.db";
    //The Android's default system path of your application database.
    //private static final String DB_PATH = Environment.getExternalStorageDirectory().getPath();
    //singleton
    private static DatabaseLocalHolder instance;

    private DatabaseLocalHolder(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        //DB_PATH = myContext.getFilesDir().getPath();
    }
    synchronized static DatabaseLocalHolder getInstance(Context ctx) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html)
         */
        if (instance == null) {
            instance = new DatabaseLocalHolder(ctx.getApplicationContext());
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String LOCAL_TABLE_CREATION_STRING = "CREATE TABLE " + DatabaseSchema.PRODUCT_LOCAL_TABLE
            + " ("
            + DatabaseSchema.PRODUCT_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "
            + DatabaseSchema.PRODUCT_RID + " INTEGER DEFAULT 0, "
            + DatabaseSchema.PRODUCT_SENT + " INTEGER NOT NULL DEFAULT 0, "
            + DatabaseSchema.PRODUCT_FAVOURITE + " INTEGER DEFAULT 0, "
            + DatabaseSchema.DATE_CREATED + " INTEGER DEFAULT 0, "

            + DatabaseSchema.PRODUCT_NAME + " TEXT, "
            + DatabaseSchema.PRODUCT_ENERGYVALUE + " REAL DEFAULT NULL, "
            + DatabaseSchema.PRODUCT_PROTEINS + " REAL DEFAULT NULL, "
            + DatabaseSchema.PRODUCT_FAT + " REAL DEFAULT NULL, "
            + DatabaseSchema.PRODUCT_CARBOHYDRATES + " REAL DEFAULT NULL, "
            + DatabaseSchema.PRODUCT_ROUGHAGE + " REAL DEFAULT NULL, "
            + DatabaseSchema.PRODUCT_SUGAR + " REAL DEFAULT NULL, "
            + DatabaseSchema.PRODUCT_CAFFEINE + " REAL DEFAULT NULL, "
            + DatabaseSchema.PRODUCT_BARCODE + " TEXT DEFAULT NULL, "
            + DatabaseSchema.PRODUCT_WEIGHT + " REAL DEFAULT 100"
            + ")";
        db.execSQL(LOCAL_TABLE_CREATION_STRING);
        //DEFAULTING AUTOINCREMENTS TO 250K
        /*
        ContentValues cv = new Product(250000,0,0,0,0,"",0,0,0,0).toContentValues();
        db.insert(DatabaseSchema.PRODUCT_LOCAL_TABLE, null, cv);
        db.delete(DatabaseSchema.PRODUCT_LOCAL_TABLE, null, null);
*/
        String mealTable = "CREATE TABLE " + DatabaseSchema.MEAL_LOCAL_TABLE
            + " ("
            + DatabaseSchema.MEAL_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "
            + DatabaseSchema.MEAL_RID + " INTEGER DEFAULT 0, "
            + DatabaseSchema.MEAL_SENT + " NUMERIC NOT NULL DEFAULT 0, "
            + DatabaseSchema.MEAL_FAVOURITE + " INTEGER DEFAULT 0, "
            + DatabaseSchema.DATE_CREATED + " INTEGER DEFAULT 0, "

            + DatabaseSchema.MEAL_NAME + " TEXT, "
            + DatabaseSchema.MEAL_INGREDIENT_IDS + " TEXT, "
            + DatabaseSchema.MEAL_INGREDIENT_WEIGHTS + " TEXT, "
            + DatabaseSchema.MEAL_WEIGHT + " REAL DEFAULT 0"
            + ")";
        db.execSQL(mealTable);

        /*
        ContentValues mcv = new ContentValues();
        mcv.put(DatabaseSchema.PRODUCT_ID, 250000);
        mcv.put(DatabaseSchema.MEAL_NAME, "");
        mcv.put(DatabaseSchema.MEAL_INGREDIENT_IDS, "");
        mcv.put(DatabaseSchema.MEAL_INGREDIENT_WEIGHTS, "");
        mcv.put(DatabaseSchema.MEAL_WEIGHT, "");
        mcv.put(DatabaseSchema.MEAL_SENT, 0);
        db.insert(DatabaseSchema.MEAL_LOCAL_TABLE, null, mcv);
        db.delete(DatabaseSchema.MEAL_LOCAL_TABLE, null, null);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion)
        {
            switch (upgradeTo)
            {
                case 2:
                    //db.execSQL(SQLiteUpdateSet.V2_AddMealtable);
                    break;
            }
            upgradeTo++;
        }
    }

    @Override
    public String getDBPath() {
        return null;
        //return myContext.getDatabasePath(DB_NAME).getPath();
    }
}