package kopacz.diabetool;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Kopac on 02.09.2016.
 */
public class CustomAlerts {
    public interface OnSetWeightListener {
        void OnWeightSet (float weight);
        void OnWeightSetCanceled();
    }

    public static AlertDialog.Builder deleteAlert (Context context, DialogInterface.OnClickListener positiveAction) // Utworzono. (z kropką)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppTheme_DefaultAlert);
        builder.setTitle(context.getString(R.string.dialog_delete_header));
        builder.setMessage(context.getString(R.string.dialog_delete_body));
        builder.setNegativeButton(R.string.universal_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        builder.setPositiveButton(R.string.universal_ok, positiveAction);
        return builder;
    }

    public static AlertDialog.Builder deleteAlert (Context context, DialogInterface.OnClickListener positiveAction, DialogInterface.OnClickListener negativeAction) // Utworzono. (z kropką)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppTheme_DefaultAlert);
        builder.setTitle(context.getString(R.string.dialog_delete_header));
        builder.setMessage(context.getString(R.string.dialog_delete_body));
        builder.setNegativeButton(R.string.universal_cancel, negativeAction);
        builder.setPositiveButton(R.string.universal_ok, positiveAction);
        return builder;
    }
    public static AlertDialog inputWeightSimple (final Context context, CustomAlerts.OnSetWeightListener callbackAcitivty)
    {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AppTheme_DefaultAlert);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.alert_float_prompt, null);
        final FloatInputEditText editText = (FloatInputEditText) view.findViewById(R.id.generic_value);
        editText.setText("100");
        final OnSetWeightListener listener = callbackAcitivty;

        builder1.setView(view);
        builder1.setTitle(context.getString(R.string.dialog_input_weight));
        builder1.setCancelable(true);

        //builder1.setLis
        builder1.setPositiveButton(R.string.universal_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Float tempf = null;
                try {
                    tempf = Float.valueOf(editText.getText().toString());
                } catch (NumberFormatException nf) {
                    Toast.makeText(context, R.string.dialog_input_proper_values_warning, Toast.LENGTH_SHORT).show();
                    listener.OnWeightSetCanceled();
                }
                if (tempf != null)
                listener.OnWeightSet(tempf);
            }
        }).setNegativeButton(R.string.universal_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.OnWeightSetCanceled();
            }
        });
        builder1.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                listener.OnWeightSetCanceled();
            }
        });
        return builder1.create();
    }
}
