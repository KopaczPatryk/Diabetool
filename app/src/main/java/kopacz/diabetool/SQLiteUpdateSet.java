package kopacz.diabetool;

/**
 * Created by Kopac on 18.08.2016.
 */
public class SQLiteUpdateSet {
    public static String V2_AddMealtable = "CREATE TABLE " + DatabaseSchema.MEAL_LOCAL_TABLE + " (" + DatabaseSchema.MEAL_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "
            + DatabaseSchema.MEAL_NAME + " TEXT,"
            + DatabaseSchema.MEAL_INGREDIENT_IDS + " TEXT,"
            + DatabaseSchema.MEAL_INGREDIENT_WEIGHTS + " TEXT,"
            + DatabaseSchema.MEAL_WEIGHT + " REAL,"
            + DatabaseSchema.MEAL_SENT + " INTEGER NOT NULL DEFAULT 0)";
}
