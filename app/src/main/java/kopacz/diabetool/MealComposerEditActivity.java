package kopacz.diabetool;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Kopac on 01.09.2016.
 */
public class MealComposerEditActivity extends MealComposerActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_meal_composer);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

//        mealName = (EditText) findViewById(R.id.meal_name);
//        entries = (ListView) findViewById(R.id.mc_ingredients);

//        meal = (Meal) savedInstanceState.getSerializable("meal");
        //MealEntry editedMeal = (MealEntry) savedInstanceState.getSerializable("meal");
//        if(savedInstanceState != null)
//        {
//            meal = (Meal) savedInstanceState.getSerializable("meal");
//        }
//        else {
////            MealEntry editedMeal = (MealEntry) getIntent().getSerializableExtra("meal");
//        }
        MealEntry editedMeal = (MealEntry) getIntent().getSerializableExtra("meal");
        //duh = DatabaseUnifiedHelper.getInstance(this);

        meal = dh.getMeal(editedMeal);
//        int size = meal.getIngredients().size();

//        Toast.makeText(getApplicationContext(), "Meal" + size, Toast.LENGTH_SHORT).show();


        adapter = new AdapterSimplePortions(this, meal.getIngredients());
        entries.setAdapter(adapter);

//        assert entries != null;
//        entries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.v("bugi", "clicked on listview");
//            }
//        });
        mealName.setText(meal.getName());

        registerForContextMenu(entries);
        /*saveMeal = (Button) findViewById(R.id.save_meal);
        entries = (ListView) findViewById(R.id.mc_ingredients);
        Log.v("bugi", "Pre create meal size" + String.valueOf(meal.getIngredients().size()));
        if(savedInstanceState != null)
        {
            meal = (Meal) savedInstanceState.getSerializable("meal");
        }
        dh = DatabaseUnifiedHelper.getInstance(this);
        adapter = new AdapterSimplePortions(this, meal.getIngredients());
        Log.v("bugi", "Post create meal size" + String.valueOf(meal.getIngredients().size()));
        assert entries != null;
        entries.setAdapter(adapter);
//        entries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.v("bugi", "clicked on listview");
//            }
//        });
        */
    }

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
                    Toast.makeText(this, R.string.mc_toast_meal_edited, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this, "MO:id: " + meal.getMealOutline().getId(), Toast.LENGTH_SHORT).show();
                    dh.editMeal(meal.getMealOutline());
                    finish();
                }
                return true;
            case R.id.add_ingredient:
                Intent intent = new Intent(MealComposerEditActivity.this, DatabaseActivity.class);
                startActivityForResult(intent, INGREDIENT_SELECT_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}