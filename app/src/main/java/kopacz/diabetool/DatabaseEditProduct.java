package kopacz.diabetool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class DatabaseEditProduct extends AppCompatActivity implements fragmentEditProduct.OnProductAcceptedListener {
    public static String PRODUCT_TAG = "produkt";
    public static String PRODUCT_ID_TAG = "produktid";

    private DatabaseHelper upm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        upm = InstanceFactory.getDatabaseHelper(this);
        setContentView(R.layout.activity_database_edit_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        fragmentEditProduct fragmentEditProduct = (kopacz.diabetool.fragmentEditProduct) getFragmentManager().findFragmentById(R.id.editfragment);
//        //Bundle bundle = new Bundle();
//
//        //bundle.putSerializable(PRODUCT_TAG, getIntent().getSerializableExtra(PRODUCT_TAG));
//        fragmentEditProduct.setArguments(getIntent().getExtras());
//    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void OnProductAccepted(Product product) {
        long id = getIntent().getLongExtra(PRODUCT_ID_TAG, -1);

        upm.editProduct(id, product);
        finish();
    }
}
