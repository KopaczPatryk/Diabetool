package kopacz.diabetool;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MealsDatabaseActivity extends AppCompatActivity {
    private static final int MEAL_EDIT_REQUEST = 2;
    private static final String TAG_MEAL = "meal";
    private AutoCompleteTextView searchBar;
    private Button searchButton;
    private ListView listView;

    private DatabaseHelper upm;

    private boolean privateOnly = false;
    private boolean showFavourites = false;
    private MealEntry lastClickedEntry = null;

    private float desiredWeight = 100f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_database);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        upm = InstanceFactory.getDatabaseHelper(this);
        searchBar = (AutoCompleteTextView) findViewById(R.id.db_search_bar);
        searchButton = (Button) findViewById(R.id.db_search_button);
        listView = (ListView) findViewById(R.id.db_results_list);

        registerForContextMenu(listView);

        assert listView != null;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listviewItemClickLogic(view, position);
            }
        });
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 6) //zdarzenie dla przycisku 'gotowe'
                {
                    searchButton.performClick();
                }
                return false;
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.v("MealsActivity", "search button clicked");
                queryResults();
            }
        });
    }

    public void queryResults() {
        Cursor tempCustomCursor = upm.queryMealsContainingName(searchBar.getText().toString().trim(), privateOnly, showFavourites);
        AdapterMealOutlineCursor adapter = new AdapterMealOutlineCursor(getApplicationContext(), tempCustomCursor);
        //Toast.makeText(this, "count: " + tempCustomCursor.getCount(), Toast.LENGTH_SHORT).show();
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meal_db_actions, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.meal_database_action_setWeight:
                CustomAlerts.inputWeightSimple(this, this).show();
                return true;*/
            case R.id.compose_own:
                Intent activity = new Intent(MealsDatabaseActivity.this, MealComposerAddActivity.class);
                startActivity(activity);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.database_meals_action_showOnlyOwn:
                item.setChecked(!item.isChecked());
                privateOnly = item.isChecked();
                queryResults();
                return true;
            case R.id.database_meals_action_showFavourites:
                item.setChecked(!item.isChecked());
                showFavourites = item.isChecked();
                queryResults();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        listviewItemClickLogic(v, info.position);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meal_db_listview_contextmenu, menu);

        if (lastClickedEntry != null)
        {
            if (lastClickedEntry.isFavourite())
            {
                menu.findItem(R.id.db_contextmenu_set_favourite).setTitle(R.string.meal_db_action_delete_favourite);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (lastClickedEntry != null) {
            switch (item.getItemId()) {
                case R.id.db_contextmenu_set_favourite:
                    upm.setMealFavourite(lastClickedEntry.getId(), !lastClickedEntry.isFavourite());
                    queryResults();
                    //Intent intent = new Intent(getApplicationContext(), DatabaseEditProduct.class);
                    //intent.putExtras(lastClickedEntry.getBundle());
                    //Toast.makeText(getApplicationContext(), "Funkcja jeszcze niedostępna", Toast.LENGTH_SHORT).show();
                    //startActivityForResult(intent, PRODUCT_EDIT_REQUEST);
                    break;
                case R.id.meal_db_contextmenu_openmeal:
                    //Intent intent = new Intent(getApplicationContext(), DatabaseEditProduct.class);
                    //intent.putExtras(lastClickedEntry.getBundle());
                    Toast.makeText(getApplicationContext(), "Funkcja jeszcze niedostępna", Toast.LENGTH_SHORT).show();
                    //startActivityForResult(intent, PRODUCT_EDIT_REQUEST);
                    break;
                case R.id.meal_db_contextmenu_edit:
                    Intent intent = new Intent(getApplicationContext(), MealComposerEditActivity.class);
                    intent.putExtra(TAG_MEAL, lastClickedEntry);

                    //Toast.makeText(getApplicationContext(), "Ids: " + lastClickedEntry.getIngredientids(), Toast.LENGTH_SHORT).show();
                    startActivityForResult(intent, MEAL_EDIT_REQUEST);
                    break;
                case R.id.meal_db_contextmenu_delete:
                    CustomAlerts.deleteAlert(this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            upm.tryDeleteMeal(lastClickedEntry.getId());
                            queryResults();
                        }
                    }).show();
                    break;
            }
        }
        else
        {
            Toast.makeText(this, "Najpierw wybierz produkt z listy", Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }

    private void listviewItemClickLogic(View view, int position) {
        view.setActivated(true);
        view.setSelected(true);
        Cursor tCursor = (Cursor) listView.getItemAtPosition(position);
        lastClickedEntry = MealEntry.decomposeCursor(tCursor);
        fragmentProductInfo infoFragment = fragmentProductInfo.newInstance(upm.getMeal(lastClickedEntry), false, true, false, false, true);

        //Toast.makeText(getApplicationContext(), entry.getId() + ":" + lastClickedEntry.getId(), Toast.LENGTH_SHORT).show();
        setBottomFragment(infoFragment, false);
    }

    private void setBottomFragment(Fragment fragment, boolean addToBackStack) {
        LinearLayout frame = (LinearLayout) findViewById(R.id.db_fragment_frame);

        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        assert frame != null;
        if (frame.getChildCount() > 0) {
            transaction.replace(R.id.db_fragment_frame, fragment);
        } else {
            transaction.add(R.id.db_fragment_frame, fragment);
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        //TODO dorobić to w innych scenach
        // chyba lepiej zroić odzielną klase z tymi bajerami w activity
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        /*if (requestCode == MEAL_EDIT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK)
            {
                searchBar.setText(data.getExtras().get("QR").toString());
            }
        }
        searchButton.performClick();*/
        super.onActivityResult(requestCode, resultCode, data);
    }


}
