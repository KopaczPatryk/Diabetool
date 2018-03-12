package kopacz.diabetool;

import android.database.Cursor;

/**
 * Created by Kopac on 29.06.2016.
 */
interface DatabaseHelper {

    //products
    long addProduct(Product entry);
    boolean editProduct(long id, Product entry);
    //Cursor queryProductsContainingName(String name, boolean privateOnly);
    Cursor queryProductsContainingName(String trim, boolean privateOnly, boolean showFavourites);
    Cursor queryAllProducts();
    Cursor queryProduct(long id);
    long tryDeleteProduct(long id);
    Cursor getUnsentProducts();
    long productSetSent(long id);
    long getProductCount();
    long setProductFavourite(long id, boolean bool);

    //meals
    long addMeal(MealEntry mealEntry);
    long addMeal(Meal meal);
    boolean editMeal(Meal meal);
    Cursor queryMealsContainingName(String trim, boolean privateOnly, boolean showFavourites);
    boolean editMeal(MealEntry mealEntry);
    //Cursor queryMeals(String name);
    long tryDeleteMeal(long id);
    Meal getMeal(MealEntry mo);
    long getMealCount();
    long setMealFavourite(long id, boolean bool);
}
