package kopacz.diabetool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity {
    EditText carbohydrates;
    EditText proteins;
    EditText fat;

    TextView ww;
    TextView wbt;

    UnifiedCalculator calc = InstanceFactory.getUnifiedCalculator();

    private void recalcWW() {
        ww.setText(String.format("WW: %s", calc.ww(Float.parseFloat(carbohydrates.getText().toString()))));
    }

    private void recalcWBT() {
        wbt.setText(String.format("WBT: %s", calc.wbt(Float.parseFloat(proteins.getText().toString()), Float.parseFloat(fat.getText().toString()))));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        carbohydrates = (EditText) findViewById(R.id.calc_carbohydrates);
        proteins = (EditText) findViewById(R.id.calc_proteins);
        fat = (EditText) findViewById(R.id.calc_fat);

        ww = (TextView) findViewById(R.id.calc_WW);
        wbt = (TextView) findViewById(R.id.calc_WBT);

        carbohydrates.addTextChangedListener(new watcher());
        proteins.addTextChangedListener(new watcher());
        fat.addTextChangedListener(new watcher());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private class watcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                recalcWW();
                recalcWBT();
            } catch (NumberFormatException e) {

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}