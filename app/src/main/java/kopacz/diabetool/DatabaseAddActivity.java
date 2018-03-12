package kopacz.diabetool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public  class DatabaseAddActivity extends AppCompatActivity implements fragmentAddCustomProduct.OnProductAcceptedListener {

    private DatabaseHelper upm;

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.database_add_actions, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back arrow

        upm = InstanceFactory.getDatabaseHelper(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        //TODO dorobić to w innych scenach
        // chyba lepiej zroić odzielną klase z tymi bajerami w activity
    }

    @Override
    public void OnProductAccepted(Product entry) {
        //entry.setScale(100f);
        upm.addProduct(entry);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.a_db_add_notification_added), Toast.LENGTH_SHORT).show();
        finish();
    }
}