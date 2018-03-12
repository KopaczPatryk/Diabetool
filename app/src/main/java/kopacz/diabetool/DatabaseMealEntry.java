package kopacz.diabetool;

/**
 * Created by Kopac on 14.07.2016.
 */
class DatabaseMealEntry {
    public static String DB_TABLE = "mymeals";

    public static String COLUMN_ID = "_id";
    public static String COLUMN_MEAL_NAME = "Name";
    public static String COLUMN_INGREDIENTS_IDS = "Ingredients";
    public static String COLUMN_INGREDIENTS_WEIGHTS = "Ingredient_weights";

    private int id;
    private String name;
    private String ingredients;
    private String weights;

    /* for example
    * name: Hamburger
    * ingredients: 1, 2, 3,
    * weights: 15, 30, 20
    * */

    public DatabaseMealEntry(String name, String ingredients, String weights) {
        this.name = name;
        this.ingredients = ingredients;
        this.weights = weights;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
