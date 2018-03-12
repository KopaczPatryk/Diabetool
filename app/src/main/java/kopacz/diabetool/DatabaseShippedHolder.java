package kopacz.diabetool;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Kopac on 12.05.2016.
 */
public class DatabaseShippedHolder extends SQLiteAssetHelper{
    private static final String DATABASE_SHIPPED_NAME = "diabetool_db.db";
    //private static final int DATABASE_VERSION = 1;
    //private static final String DB_NAME = "pl_products";
    private static final int DB_VERSION = 1;

    private static DatabaseShippedHolder instance;

    private DatabaseShippedHolder(Context context) {
        super(context, DATABASE_SHIPPED_NAME, null, DB_VERSION);
    }

    public static DatabaseShippedHolder getInstance(Context ctx) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html)
         */
        if (instance == null) {
            instance = new DatabaseShippedHolder(ctx.getApplicationContext());
        }
        return instance;
    }
}
