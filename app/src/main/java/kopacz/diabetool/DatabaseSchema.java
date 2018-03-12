package kopacz.diabetool;

/**
 * Created by Kopac on 15.08.2016.
 */
@SuppressWarnings("WeakerAccess")
public class DatabaseSchema {
    //tabele
    public static final String PRODUCT_LOCAL_TABLE = "myproducts";
    public static final String MEAL_LOCAL_TABLE = "mymeals";
    public static final String MEAL_SHIPPED_TABLE = "pl_meals";

    //products
    public static final String PRODUCT_SHIPPED_TABLE = "pl_products";
    public static final String PRODUCT_ID = "_id"; //id issued by local database
    public static final String PRODUCT_RID = "rid"; //remote id, id issued by remote database, must be unique in product table at all times
    public static final String PRODUCT_NAME = "Nazwa";
    public static final String PRODUCT_ENERGYVALUE = "Energia";
    public static final String PRODUCT_PROTEINS = "Białko";
    public static final String PRODUCT_FAT = "Tłuszcz";
    public static final String PRODUCT_CARBOHYDRATES = "Węglowodany";
    public static final String PRODUCT_ROUGHAGE = "Błonnik";
    public static final String PRODUCT_SUGAR = "Cukier";
    public static final String PRODUCT_CAFFEINE = "Kofeina";
    public static final String PRODUCT_BARCODE = "QR";
    public static final String PRODUCT_SENT = "sent";
    public static final String PRODUCT_FAVOURITE = "pfav";

    public static final String PRODUCT_WEIGHT = "pWeight";

    //meals
    public static final String MEAL_ID = "_id";
    public static final String MEAL_RID = "rid"; //remote id, id issued by remote database, must be unique in meal table at all times
    public static final String MEAL_NAME = "mName";
    public static final String MEAL_INGREDIENT_IDS = "productIDs";
    public static final String MEAL_INGREDIENT_WEIGHTS = "productWeights";
    public static final String MEAL_WEIGHT = "mWeight";
    public static final String MEAL_SENT = "mSent";
    public static final String MEAL_FAVOURITE = "mfav";

    //databaseEntry
    public static final String DATE_CREATED = "datec";
}
