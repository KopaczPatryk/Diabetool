package kopacz.diabetool;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Kopac on 24.11.2016.
 */
public class MealEntryTest {
    Product p1 = new Product(1, 10, true, 500, true, "Grejp", 1, 2, 3, 4, 5, 6, 7, 80, 100, "123456");
    Product p2 = new Product(2, 12, false, 400, true, "Banana", 7, 6, 5, 4, 3, 2, 1, 90, 100, "654321");

    @Test
    public void PrzypisIdTest() throws Exception {
        Meal meal = new Meal();
        meal.addProduct(p1);
        meal.addProduct(p2);
        meal.setName("Japko");

        MealEntry mealOutline = meal.getMealOutline();

        String ingredientids = mealOutline.getIngredientids();

        String s = p1.getId() + MealEntry.SEPARATOR + p2.getId();

        assertEquals(ingredientids, s);
//        String[] split = ingredientids.split(MealEntry.SEPARATOR);
//        ArrayList<Integer> wagi = null;
//        for (String id : split) {
//            wagi.add(Integer.parseInt(id));
//        }
        //

    }
}