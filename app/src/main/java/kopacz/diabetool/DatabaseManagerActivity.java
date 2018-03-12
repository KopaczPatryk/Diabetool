package kopacz.diabetool;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

/**
 * Created by Kopac on 04.01.2017.
 */
public class DatabaseManagerActivity extends AppCompatActivity {
    Button clearDBButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_db);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        clearDBButton = (Button) findViewById(R.id.clear_db);
        clearDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context ctx = getApplicationContext();
                MergedDatabaseHelper instance = MergedDatabaseHelper.getInstance(ctx);
                instance.clearDatabase();
                PrefsManager.getInstance(ctx).setDatabaseUpdateFullDone(false);
                PrefsManager.getInstance(ctx).setLastDownloadedProductIndex(0);
                PrefsManager.getInstance(ctx).apply();

            }
        });
    }

}
