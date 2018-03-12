package kopacz.diabetool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public abstract class MealComposerActivity extends AppCompatActivity implements ProductDetailsDialogFragment.ProductDetailsListener, CustomAlerts.OnSetWeightListener {
    protected static final int INGREDIENT_SELECT_REQUEST = 1;
    private static final String MEAL = "meal";
    protected EditText mealName;
    //protected Button saveMeal;
    protected ListView entries;
    protected Meal meal = new Meal();
    protected AdapterSimplePortions adapter;
    private int lastClickedItem;
    protected DatabaseHelper dh = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_composer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            meal = (Meal) savedInstanceState.getSerializable(MEAL);
        }

        dh = InstanceFactory.getDatabaseHelper(this);

        mealName = (EditText) findViewById(R.id.meal_name);
        entries = (ListView) findViewById(R.id.mc_ingredients);
        adapter = new AdapterSimplePortions(this, meal.getIngredients());
        entries.setAdapter(adapter);

        registerForContextMenu(entries);
    }

    //    @Override
//    public View onCreateView(String name, Context context, AttributeSet attrs) {
//
//        return super.onCreateView(name, context, attrs);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == INGREDIENT_SELECT_REQUEST) {
                Product result = (Product) data.getSerializableExtra("result");

                ProductDetailsDialogFragment fragment = ProductDetailsDialogFragment.newInstance(result);
                fragment.showWW_WBT(true);
                android.app.FragmentManager fm = getFragmentManager();

                fragment.show(fm, "fragment_scale");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MEAL, meal);
    }

    /*@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v("bugi", "Pre restore" + String.valueOf(meal.getIngredients().size()));
        meal = (Meal) savedInstanceState.getSerializable("meal");
        adapter.notifyDataSetChanged();
        try {
            adapter = new AdapterSimplePortions(this, ((Meal)savedInstanceState.getSerializable("meal")).getIngredients());
        }
        catch (NullPointerException e)
        {
            adapter = new AdapterSimplePortions(this, meal.getIngredients());
        }
        Log.v("bugi", "Post restore" + String.valueOf(meal.getIngredients().size()));
    }*/

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        lastClickedItem = info.position;
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.mc_contextmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mc_contextmenu_editweight:
                CustomAlerts.inputWeightSimple(this, this).show();
                //final Ingredient ingredient = (Ingredient) entries.getItemAtPosition(lastClickedItem);
                /*final FloatInputEditText edittext = new FloatInputEditText(this);
                final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_DefaultAlert);
                builder.setView(edittext);

                builder.setTitle("Input weight");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        meal.getIngredient(lastClickedItem).setScale(Float.parseFloat(edittext.getText().toString()));
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();*/
                break;
            case R.id.mc_contextmenu_delete:
                meal.removeProduct(lastClickedItem);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, R.string.mc_toast_ingredient_deleted, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mc_actions, menu);
        return true;
    }

    @Override
    public void OnWeightAccepted(Product ingredient) {
        meal.addProduct(ingredient);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnWeightDeclined() {

    }

    @Override
    public void OnWeightSet(float weight) {
        meal.getIngredient(lastClickedItem).setScale(weight);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnWeightSetCanceled() {

    }
}