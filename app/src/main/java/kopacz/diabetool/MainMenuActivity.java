package kopacz.diabetool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {
    MergedDatabaseHelper databaseHelper;
    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, DatabaseUpdaterService.class));
        startService(new Intent(this, Reporter.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        databaseHelper = MergedDatabaseHelper.getInstance(this);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.database_action_add_own:
                /*fragmentAddCustomProduct fragment = new fragmentAddCustomProduct();
                setBottomFragment(fragment, true);*/
                //Intent activity = new Intent(DatabaseActivity.this, DatabaseAddActivity.class);
                //startActivity(activity);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                //mode.finish(); // Action picked, so close the CAB
                return true;
            case R.id.database_action_showOnlyOwn:
                //mode.setTitle("derp");
                item.setChecked(!item.isChecked());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openDB(View view) {
        Intent activityDB = new Intent(MainMenuActivity.this, DatabaseActivity.class);
        startActivity(activityDB);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void openCalc(View view) {
        Intent activityCalc = new Intent(MainMenuActivity.this, CalculatorActivity.class);
        startActivity(activityCalc);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void openMealComposer(View view)
    {
        Intent intent = new Intent(MainMenuActivity.this, MealsDatabaseActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void openTutorials(View view) {
        Intent intent = new Intent(this, AndroidDatabaseManager.class);
        startActivity(intent);
    }

    public void openFaq (View view)
    {
        Intent intent = new Intent(this, AndroidDatabaseManager.class);
        startActivity(intent);
    }

    public void openSettings(View view)
    {
        /*databaseHelper.clearDatabase();
        PrefsManager.getInstance(this).setDatabaseUpdateFullDone(false);
        PrefsManager.getInstance(this).setLastDownloadedProductIndex(0);
        PrefsManager.getInstance(this).apply();*/

        Intent intent = new Intent(MainMenuActivity.this, DatabaseManagerActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
