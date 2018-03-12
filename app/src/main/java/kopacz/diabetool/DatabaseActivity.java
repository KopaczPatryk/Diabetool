package kopacz.diabetool;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class DatabaseActivity extends AppCompatActivity implements CustomAlerts.OnSetWeightListener {
    private static final int QR_READER_REQUEST = 1;
    private static final int PRODUCT_EDIT_REQUEST = 2;
    private AutoCompleteTextView searchBar;
    private Button searchButton;
    private ListView listView;

    private DatabaseHelper upm;

    private boolean privateOnly = false;
    private boolean showFavourites = false;
    private Product lastClickedEntry = null;

    private float desiredWeight = 100f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        upm = InstanceFactory.getDatabaseHelper(this);
        searchBar = (AutoCompleteTextView) findViewById(R.id.db_search_bar);
        searchButton = (Button) findViewById(R.id.db_search_button);
        listView = (ListView) findViewById(R.id.db_results_list);

        //        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        assert fab != null;
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        registerForContextMenu(listView);

        assert listView != null;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listviewItemClickLogic(view, position);
                if (getCallingActivity() != null)
                {
                    Log.v("bugi", "Calling activity: " + getCallingActivity().toString());
                    Intent returnIntent = getIntent();
                    returnIntent.putExtra("result", lastClickedEntry);
                    setResult(RESULT_OK,returnIntent);
                    finish();
                }
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
                queryResults();
            }
        });
    }

    public void queryResults() {
        Cursor tempCustomCursor = upm.queryProductsContainingName(searchBar.getText().toString().trim(), privateOnly, showFavourites);
        AdapterProductCursor adapter = new AdapterProductCursor(getApplicationContext(), tempCustomCursor);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.db_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.database_action_setWeight:
                CustomAlerts.inputWeightSimple(this, this).show();
                return true;
            case R.id.database_action_add_own:
                Intent intent = new Intent(getApplicationContext(), DatabaseAddActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                //mode.finish(); // Action picked, so close the CAB
                return true;
            case R.id.database_action_showFavourites:
                //mode.setTitle("derp");
                item.setChecked(!item.isChecked());
                showFavourites = item.isChecked();
                queryResults();
                return true;
            case R.id.database_action_showOnlyOwn:
                //mode.setTitle("derp");
                item.setChecked(!item.isChecked());
                privateOnly = item.isChecked();
                queryResults();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void OnWeightSet(float weight) {
        desiredWeight = weight;
        //queryResults();
        //lastClickedEntry.setScale(weight);
    }

    @Override
    public void OnWeightSetCanceled() {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        listviewItemClickLogic(v, info.position);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.db_listview_contextmenu, menu);
        if (lastClickedEntry != null)
        {
            if (lastClickedEntry.isFavourite())
            {
                menu.findItem(R.id.db_contextmenu_set_favourite).setTitle(R.string.database_action_delete_favourite);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (lastClickedEntry != null) {
            switch (item.getItemId()) {
                case R.id.db_contextmenu_set_favourite:
                    upm.setProductFavourite(lastClickedEntry.getId(), !lastClickedEntry.isFavourite());
                    queryResults();
                    break;
                case R.id.db_contextmenu_edit:
                    Intent intent = new Intent(getApplicationContext(), DatabaseEditProduct.class);
                    intent.putExtra(DatabaseEditProduct.PRODUCT_TAG, lastClickedEntry);
                    intent.putExtra(DatabaseEditProduct.PRODUCT_ID_TAG, lastClickedEntry.getId());

                    //Toast.makeText(getApplicationContext(), "intent: " + lastClickedEntry.getId() + ":" + intent.getIntExtra("_id", 9999), Toast.LENGTH_SHORT).show();
                    startActivityForResult(intent, PRODUCT_EDIT_REQUEST);
                    break;
                case R.id.db_contextmenu_delete:
                    CustomAlerts.deleteAlert(this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            upm.tryDeleteProduct(lastClickedEntry.getId());
                            queryResults();
                        }
                    }).show();
                    break;
            }
        }
        else
        {
            Toast.makeText(this, R.string.database_toast_select_product_first, Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }


    private void listviewItemClickLogic(View view, int position) {
        view.setActivated(true);
        view.setSelected(true);
        Cursor tCursor = (Cursor) listView.getItemAtPosition(position);
        Product entry = Product.getDatabaseEntry(tCursor);
        entry.setScale(desiredWeight);
        lastClickedEntry = entry;
        fragmentProductInfo infoFragment = fragmentProductInfo.newInstance(entry, true, true, true, false, true);
        //infoFragment.scaleTo(desiredWeight);
        //String.valueOf(entry.getId())
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
        // chyba lepiej zroić odzielną klase z tymi bajerami w activity
    }


    /*@Override
    public void OnTryDeleteProduct(final Product entry) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        upm.tryDeleteProduct(entry.getId());
                        //cpm.tryDeleteProduct(entry.getId(), entry.getName());
                        searchButton.performClick();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        // do nothing bitch
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }*/

    public void startBarcodeReader(View view) {
        Intent intent = new Intent(DatabaseActivity.this, SimpleScannerActivity.class);
        startActivityForResult(intent, QR_READER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == QR_READER_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK)
            {
                searchBar.setText(data.getExtras().get("QR").toString());
            }
        }
        queryResults();
        super.onActivityResult(requestCode, resultCode, data);
    }
}