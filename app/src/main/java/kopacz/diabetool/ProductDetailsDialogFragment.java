package kopacz.diabetool;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Kopac on 22.08.2016.
 */
public class ProductDetailsDialogFragment extends DialogFragment {
    private static final String EDIBLE_TAG = "edibl";
    UnifiedCalculator calc = InstanceFactory.getUnifiedCalculator();

    private TextView header;
    private TextView energyValue;
    private TextView proteins;
    private TextView fat;
    private TextView carbohydrates;
    private TextView roughage;
    private TextView sugar;

    private FloatInputEditText input;

    private Button cancel;
    private Button ok;

    private TextView WW;
    private TextView WBT;

    private Product ingredient = new Product();
    private boolean ww_wbtVisible = false;
    private LinearLayout ww_wbt_container;

    private static String WW_WBT_VISIBLE_TAG = "aa";
    public interface ProductDetailsListener {
        void OnWeightAccepted(Product ingredient);
        void OnWeightDeclined();
    }
    ProductDetailsListener listener;

    public static ProductDetailsDialogFragment newInstance(Product product) {
        ProductDetailsDialogFragment frag = new ProductDetailsDialogFragment();
        frag.setProduct(product);
        return frag;
    }

    public void setProduct (Product product)
    {
        ingredient = product;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
        {
            ww_wbtVisible = savedInstanceState.getBoolean(WW_WBT_VISIBLE_TAG, ww_wbtVisible);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(WW_WBT_VISIBLE_TAG, ww_wbtVisible);
        outState.putSerializable(EDIBLE_TAG, ingredient);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.product_details_dialog, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (savedInstanceState != null)
        {
            ingredient = (Product) savedInstanceState.getSerializable(EDIBLE_TAG);
        }
        header = (TextView) view.findViewById(R.id.f_productinfo_name);
        header.setText(getString(R.string.f_productinfo_name, ingredient.getName()));

        energyValue = (TextView) view.findViewById(R.id.f_productinfo_energyvalue);
        proteins = (TextView) view.findViewById(R.id.f_productinfo_proteins);
        fat = (TextView) view.findViewById(R.id.f_productinfo_fat);
        carbohydrates = (TextView) view.findViewById(R.id.f_productinfo_carbohydrates);
        roughage = (TextView) view.findViewById(R.id.f_productinfo_roughage);
        sugar = (TextView) view.findViewById(R.id.f_productinfo_sugar);

        input = (FloatInputEditText) view.findViewById(R.id.f_input);

        cancel = (Button) view.findViewById(R.id.cancel);
        ok = (Button) view.findViewById(R.id.accept);

        ww_wbt_container = (LinearLayout) view.findViewById(R.id.f_productinfo_ww_wbt_container);
        WW = (TextView) view.findViewById(R.id.f_productinfo_ww);
        WBT = (TextView) view.findViewById(R.id.f_productinfo_wbt);

        ingredient.setScale(Float.valueOf(input.getText().toString()));

        setVisibility(ww_wbt_container, ww_wbtVisible);

        recalculateVariables();

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    ingredient.setScale(Float.valueOf(input.getText().toString()));
                    recalculateVariables();
                }
                catch (NumberFormatException e)
                {
                    ingredient.setScale(0f);
                    recalculateVariables();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingredient.getBaseWeight() == 0)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Weight must be greater than 0", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    listener.OnWeightAccepted(ingredient);
                    dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnWeightDeclined();
                dismiss();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (ProductDetailsListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ClassCastException");
        }
    }
    private void recalculateVariables()
    {
        FloatFormatter df = new FloatFormatter();
        LocaleFormater lf = new LocaleFormater(df, getActivity());
        energyValue.setText(lf.catchNull(ingredient.getEnergyValue(), R.string.f_productinfo_energyvalue, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_kcal_short_dotted));
        proteins.setText(lf.catchNull(ingredient.getProteins(), R.string.f_productinfo_proteins, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_gram_short_dotted));
        fat.setText(lf.catchNull(ingredient.getFat(), R.string.f_productinfo_fat, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_gram_short_dotted));
        carbohydrates.setText(lf.catchNull(ingredient.getCarbohydrates(), R.string.f_productinfo_carbohydrates, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_gram_short_dotted));
        roughage.setText(lf.catchNull(ingredient.getRoughage(), R.string.f_productinfo_roughage, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_gram_short_dotted));
        sugar.setText(lf.catchNull(ingredient.getSugar(), R.string.f_productinfo_sugar, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_gram_short_dotted));

        /*energyValue.setText(getString(R.string.f_productinfo_energyvalue, DatabaseValuesInterpreter.interpret(df.format(ingredient.getEnergyValue()))));
        proteins.setText(getString(R.string.f_productinfo_proteins, DatabaseValuesInterpreter.interpret(df.format(ingredient.getProteins()))));
        fat.setText(getString(R.string.f_productinfo_fat, DatabaseValuesInterpreter.interpret(df.format(ingredient.getFat()))));
        carbohydrates.setText(getString(R.string.f_productinfo_carbohydrates, DatabaseValuesInterpreter.interpret(df.format(ingredient.getCarbohydrates()))));
        roughage.setText(getString(R.string.f_productinfo_roughage, DatabaseValuesInterpreter.interpret(df.format(ingredient.getRoughage()))));
        sugar.setText(getString(R.string.f_productinfo_sugar, DatabaseValuesInterpreter.interpret(df.format(ingredient.getSugar()))));*/

        WW.setText(getString(R.string.f_productinfo_ww, df.format(calc.ww(ingredient.getCarbohydrates()))));
        WBT.setText(getString(R.string.f_productinfo_wbt, df.format(calc.wbt(ingredient.getProteins(), ingredient.getFat()))));

    }
    public void showWW_WBT(boolean b)
    {
        ww_wbtVisible = b;
    }

    private void setVisibility(View tv, boolean bool)
    {
        if (bool)
            tv.setVisibility(View.VISIBLE);
        else
            tv.setVisibility(View.GONE);
    }
}