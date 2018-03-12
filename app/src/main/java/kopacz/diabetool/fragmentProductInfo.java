package kopacz.diabetool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class fragmentProductInfo extends Fragment {

    private static final String ENTRY_TAG = "a";
    /*public interface OnProductDeleteListener {
            void OnTryDeleteProduct(Product entry);
        }
        private OnProductDeleteListener listener;*/
    UnifiedCalculator calc = InstanceFactory.getUnifiedCalculator();

    //product values
    private TextView name;
    private TextView energyValue;
    private TextView proteins;
    private TextView fat;
    private TextView carbohydrates;
    private TextView roughage;
    private TextView sugar;
    private TextView caffeine; //ma odpowiednik w stylu odkomentować
    private TextView barcode;
    private TextView WW;
    private TextView WBT;

    private LinearLayout header;
    private LinearLayout values;
    private LinearLayout ww_wbt;
    private LinearLayout barcode_container;
    private LinearLayout icons;

    private boolean headerVisible = false;
    private boolean valuesVisible = false;
    private boolean ww_wbtVisible = false;
    private boolean barcodeVisible = false;
    private boolean iconsVisible = false;

    private SquareImageButton iconEnergy;
    private SquareImageButton iconProteins;
    private SquareImageButton iconFat;
    private SquareImageButton iconHC;
    private SquareImageButton iconRoughage;
    private SquareImageButton iconSugar;
    private SquareImageButton iconCaffeine;

    private Edible entry;

    //konstruktor który zapisuje stan by można było go wczytać w funkcji onCreate() po wywołaniu konstruktora bez argumentów
    public static fragmentProductInfo newInstance(Edible entry, boolean showheader, boolean showValues, boolean showBarcode, boolean showIcons, boolean showWW_WBT) {
        fragmentProductInfo fragmentInfo = new fragmentProductInfo();
        fragmentInfo.entry = entry;
        //fragmentInfo.setArguments(entry.getBundle());
        fragmentInfo.showHeader(showheader);
        fragmentInfo.showValues(showValues);
        fragmentInfo.showBarcode(showBarcode);
        fragmentInfo.showIcons(showIcons);
        fragmentInfo.showWW_WBT(showWW_WBT);
        return fragmentInfo;
    }
    /*public static fragmentProductInfo newInstance(Meal meal)
    {
        fragmentProductInfo fpi = new fragmentProductInfo();
        fpi.entry = meal;
        //fpi.
        //fpi.setArguments(meal.getBundle());
        return fpi;
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_product_info, container, false);

        name = (TextView) view.findViewById(R.id.f_productinfo_name);
        energyValue = (TextView) view.findViewById(R.id.f_productinfo_energyvalue);
        proteins = (TextView) view.findViewById(R.id.f_productinfo_proteins);
        fat = (TextView) view.findViewById(R.id.f_productinfo_fat);
        carbohydrates = (TextView) view.findViewById(R.id.f_productinfo_carbohydrates);
        roughage = (TextView) view.findViewById(R.id.f_productinfo_roughage);
        sugar = (TextView) view.findViewById(R.id.f_productinfo_sugar);
        //caffeine = (TextView) view.findViewById(R.id.f_productinfo_caffeine);
        barcode = (TextView) view.findViewById(R.id.f_productinfo_barcode);

        WW = (TextView) view.findViewById(R.id.f_productinfo_ww);
        WBT = (TextView) view.findViewById(R.id.f_productinfo_wbt);

        header = (LinearLayout) view.findViewById(R.id.c_header);
        values = (LinearLayout) view.findViewById(R.id.c_values);
        ww_wbt = (LinearLayout) view.findViewById(R.id.f_productinfo_ww_wbt_container);
        barcode_container = (LinearLayout) view.findViewById(R.id.c_barcode);
        icons = (LinearLayout) view.findViewById(R.id.c_icons);

        if (savedInstanceState != null)
        {
            entry = (Edible) savedInstanceState.getSerializable(ENTRY_TAG);
        }

        setVisibility(header, headerVisible);
        setVisibility(values, valuesVisible);
        setVisibility(ww_wbt, ww_wbtVisible);
        setVisibility(barcode_container, barcodeVisible);
        setVisibility(icons, iconsVisible);

        iconEnergy = (SquareImageButton) view.findViewById(R.id.ic_energyvalue);
        iconProteins = (SquareImageButton) view.findViewById(R.id.ic_proteins);
        iconFat = (SquareImageButton) view.findViewById(R.id.ic_fat);
        iconHC = (SquareImageButton) view.findViewById(R.id.ic_hydrocarbonates);
        iconRoughage = (SquareImageButton) view.findViewById(R.id.ic_roughage);
        iconSugar = (SquareImageButton) view.findViewById(R.id.ic_sugar);
        iconCaffeine = (SquareImageButton) view.findViewById(R.id.ic_caffeine);
        if (iconsVisible)
        {
            //set icon visibility
            setVisibility(iconEnergy, entry.getEnergyValue());
            setVisibility(iconProteins, entry.getProteins());
            setVisibility(iconFat, entry.getFat());
            setVisibility(iconHC, entry.getCarbohydrates());
            setVisibility(iconRoughage, entry.getRoughage());
            setVisibility(iconSugar, entry.getSugar());
        }

        //setVisibility(iconCaffeine, entry.getCaffeine());
        name.setText(getString(R.string.f_productinfo_name, entry.getName()));

        //Locale current = getResources().getConfiguration().locale;
        FloatFormatter df = new FloatFormatter();
        LocaleFormater lf = new LocaleFormater(df, getActivity());
        //en, prot, fat, wegl,blon, cuk

        energyValue.setText(lf.catchNull(entry.getEnergyValue(), R.string.f_productinfo_energyvalue, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_kcal_short_dotted));
        proteins.setText(lf.catchNull(entry.getProteins(), R.string.f_productinfo_proteins, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_gram_short_dotted));
        fat.setText(lf.catchNull(entry.getFat(), R.string.f_productinfo_fat, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_gram_short_dotted));
        carbohydrates.setText(lf.catchNull(entry.getCarbohydrates(), R.string.f_productinfo_carbohydrates, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_gram_short_dotted));
        roughage.setText(lf.catchNull(entry.getRoughage(), R.string.f_productinfo_roughage, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_gram_short_dotted));
        sugar.setText(lf.catchNull(entry.getSugar(), R.string.f_productinfo_sugar, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_gram_short_dotted));
        //sugar.setText(lf.catchNull(entry.getSugar(), R.string.f_productinfo_sugar, R.string.f_productinfo_unknwn_contents, R.string.f_productinfo_unit_gram_short_dotted));

        /*
        energyValue.setText(getString(R.string.f_productinfo_energyvalue, DatabaseValuesInterpreter.interpret(df.format(entry.getEnergyValue()))));
        proteins.setText(getString(R.string.f_productinfo_proteins, DatabaseValuesInterpreter.interpret(df.format(entry.getProteins()))));
        fat.setText(getString(R.string.f_productinfo_fat, DatabaseValuesInterpreter.interpret(df.format(entry.getFat()))));
        carbohydrates.setText(getString(R.string.f_productinfo_carbohydrates, DatabaseValuesInterpreter.interpret(df.format(entry.getCarbohydrates()))));
        roughage.setText(getString(R.string.f_productinfo_roughage, DatabaseValuesInterpreter.interpret(df.format(entry.getRoughage()))));
        sugar.setText(getString(R.string.f_productinfo_sugar, DatabaseValuesInterpreter.interpret(df.format(entry.getSugar()))));
        */
        WW.setText(lf.catchNull(calc.ww(entry.getCarbohydrates()), R.string.f_productinfo_ww, R.string.f_productinfo_unknwn_contents));
        WBT.setText(lf.catchNull(calc.wbt(entry.getProteins(), entry.getFat()), R.string.f_productinfo_wbt, R.string.f_productinfo_unknwn_contents));
        //WW.setText(getString(R.string.f_productinfo_ww, df.format(calc.ww(entry.getCarbohydrates()))));
        //WBT.setText(getString(R.string.f_productinfo_wbt, df.format(calc.wbt(entry.getProteins(), entry.getFat()))));
        //Toast.makeText(getContext(), "Barcode: " + String.valueOf(bundle.getLong(DatabaseSchema.PRODUCT_BARCODE)), Toast.LENGTH_SHORT).show();

        if (entry instanceof Packable)
        {
            Packable p = (Packable) entry;
            if (p.getBarcode().equals("-1") || p.getBarcode().equals(""))
            {
                barcode.setText(getString(R.string.f_productinfo_barcode_unspecified));
            }
            else
            {
                barcode.setText(getString(R.string.f_productinfo_barcode, p.getBarcode()));
            }
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ENTRY_TAG, entry);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    public void showHeader(boolean b)
    {
        headerVisible = b;
    }
    public void showValues(boolean b)
    {
        valuesVisible = b;
    }
    public void showBarcode(boolean b)
    {
        barcodeVisible = b;
    }
    public void showIcons(boolean b)
    {
        iconsVisible = b;
    }
    public void showWW_WBT(boolean b)
    {
        ww_wbtVisible = b;
    }

    private void setVisibility(View tv, boolean bool) {
        if (bool)
            tv.setVisibility(View.VISIBLE);
        else
            tv.setVisibility(View.GONE);
    }
    private void setVisibility(View tv, float value) {
        if (value > 0)
            tv.setVisibility(View.VISIBLE);
        else
            tv.setVisibility(View.GONE);
    }
    /*public void scaleTo(float weight) {
        entry.setScale(weight);
    }*/
}