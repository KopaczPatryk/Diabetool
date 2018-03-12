package kopacz.diabetool;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Kopac on 28.11.2016.
 */

public abstract class ProductInteractor extends Fragment {
    protected OnProductAcceptedListener listener;

    protected Product product;

    protected EditText name;
    protected EditText energyValue;
    protected EditText proteins;
    protected EditText fat;
    protected EditText carbohydrates;
    protected EditText roughage;
    protected EditText sugar;
    protected EditText caffeine;
    protected EditText perGrams;
    protected EditText QR;

    protected Button barcode;
    protected Button accept;
    protected static final int BARCODE_READER_REQUEST = 1;  // The request code

    interface OnProductAcceptedListener {
        void OnProductAccepted(Product product);
    }

    //konstruktor który zapisuje stan by można było go wczytać w funkcji onCreate() po wywołaniu konstruktora bez argumentów
//    public static fragmentEditProduct newInstance() {
//        return new fragmentEditProduct();
//    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //super.onAttach(getContext());
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listener = (OnProductAcceptedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnProductAcceptedListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        //super.onAttach(getContext());
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            listener = (OnProductAcceptedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnProductAcceptedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_product_edit, container, false);

        //przypis kontrolek
        name = (EditText) view.findViewById(R.id.iName);
        energyValue = (EditText) view.findViewById(R.id.iEnergyValue);
        proteins = (EditText) view.findViewById(R.id.iProteins);
        fat = (EditText) view.findViewById(R.id.iFat);
        carbohydrates = (EditText) view.findViewById(R.id.iCarbohydrates);
        roughage = (EditText) view.findViewById(R.id.iRoughage);
        sugar = (EditText) view.findViewById(R.id.iSugar);
        caffeine = (EditText) view.findViewById(R.id.iCaffeine);
        perGrams = (EditText) view.findViewById(R.id.iPerGrams);
        QR = (EditText) view.findViewById(R.id.barcode_text);

        //Bundle productBundle = getActivity().getIntent().getExtras();
        //if (product == null)
        //{
            product = (Product) getActivity().getIntent().getSerializableExtra("product");
        //}
        if (product != null)
        {
            FloatFormatter ff = new FloatFormatter();
            //id = product.getId();
            name.setText(product.getName());
            Log.v("formater", ff.format(product.getEnergyValue()));
            energyValue.setText(ff.format(product.getEnergyValue()));
            proteins.setText(ff.format(product.getProteins()));
            fat.setText(ff.format(product.getFat()));
            carbohydrates.setText(ff.format(product.getCarbohydrates()));
            roughage.setText(ff.format(product.getRoughage()));
            sugar.setText(ff.format(product.getSugar()));
            caffeine.setText(ff.format(product.getCaffeine()));
            QR.setText(String.valueOf(product.getBarcode()));
            perGrams.setText(ff.format(product.getBaseWeight()));
        }

        barcode = (Button) view.findViewById(R.id.button_barcode);
        accept = (Button) view.findViewById(R.id.buttonAddProduct);
        final Resources resources = getResources();
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int errors = 0; //if edittext has no value it raises error count, cant proceed if its greater than zero

                if (name.getText().length() == 0)
                {
                    name.setError(resources.getString(R.string.f_product_custom_add_warning_empty));
                    errors++;
                }
                /*
                if (energyValue.getText().length() == 0)
                {
                    energyValue.setError(resources.getString(R.string.f_product_custom_add_warning_empty));
                    errors++;
                }
                if (proteins.getText().length() == 0)
                {
                    proteins.setError(resources.getString(R.string.f_product_custom_add_warning_empty));
                    errors++;
                }
                if (fat.getText().length() == 0)
                {
                    fat.setError(resources.getString(R.string.f_product_custom_add_warning_empty));
                    errors++;
                }
                if (carbohydrates.getText().length() == 0)
                {
                    carbohydrates.setError(resources.getString(R.string.f_product_custom_add_warning_empty));
                    errors++;
                }
                if (roughage.getText().length() == 0)
                {
                    roughage.setError(resources.getString(R.string.f_product_custom_add_warning_empty));
                    errors++;
                }
                if (sugar.getText().length() == 0)
                {
                    sugar.setError(resources.getString(R.string.f_product_custom_add_warning_empty));
                    errors++;
                }
                if (caffeine.getText().length() == 0)
                {
                    caffeine.setError(resources.getString(R.string.f_product_custom_add_warning_empty));
                    errors++;
                }*/
                if (perGrams.getText().length() == 0)
                {
                    caffeine.setError(resources.getString(R.string.f_product_custom_add_warning_empty));
                    errors++;
                }//error check statements
                if (errors == 0)
                {
                    performCallback();
                }
            }
        });

        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SimpleScannerActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST);
            }
        });
        return view;
    }
    abstract void performCallback();
//    protected void performCallback()
//    {
//        Product tempEntry = new Product();
//        tempEntry.setDateCreated(System.currentTimeMillis());
//        tempEntry.setName(name.getText().toString());
//        tempEntry.setEnergyValue(Float.parseFloat(energyValue.getText().toString()));
//        tempEntry.setProteins(Float.parseFloat(proteins.getText().toString()));
//        tempEntry.setFat(Float.parseFloat((fat.getText().toString())));
//        tempEntry.setCarbohydrates(Float.parseFloat(carbohydrates.getText().toString()));
//        tempEntry.setRoughage(Float.parseFloat(roughage.getText().toString()));
//        tempEntry.setSugar(Float.parseFloat(sugar.getText().toString()));
//        tempEntry.setCaffeine(Float.parseFloat(caffeine.getText().toString()));
//        tempEntry.setBarcode(QR.getText().toString());
//        listener.OnProductAccepted(tempEntry);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode)
        {
            case RESULT_OK:
                switch (requestCode)
                {
                    case BARCODE_READER_REQUEST:
                        if (data.getStringExtra("QR") != null)
                        {
                            QR.setText(data.getStringExtra("QR"));
                        }
                        break;
                    default:
                        break;
                }
                break;
            case RESULT_CANCELED:
                Toast.makeText(getActivity(), R.string.product_interaction_cancelled_not_saved, Toast.LENGTH_SHORT).show();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    protected String isNull (String stringBeingChecked)
    {
        if (!stringBeingChecked.equals("-1"))
        {
            return stringBeingChecked;
        }
        return "";
    }
}
