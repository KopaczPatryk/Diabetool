package kopacz.diabetool;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Kopac on 01.09.2016.
 */
public class MealComposerAddActivity extends MealComposerActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_meal:
                if (mealName.getText().toString().length() == 0) {
                    mealName.setError(getString(R.string.mc_toast_meal_needs_name));
                }
                else if (meal.getIngredients().size() == 0) {
                    Toast.makeText(this, R.string.mc_add_ingredients_warning, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    meal.setName(mealName.getText().toString().trim());
                    meal.setDateCreated(System.currentTimeMillis());
                    //Toast.makeText(this, "Meal added", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this, "Meal id: " + , Toast.LENGTH_SHORT).show();
                    dh.addMeal(meal.getMealOutline());
                    finish();
                }
                return true;
            case R.id.add_ingredient:
                Intent intent = new Intent(MealComposerAddActivity.this, DatabaseActivity.class);
                startActivityForResult(intent, INGREDIENT_SELECT_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
