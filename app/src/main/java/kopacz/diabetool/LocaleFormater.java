package kopacz.diabetool;

import android.content.Context;

import java.text.DecimalFormat;

/**
 * Created by Kopac on 14.12.2016.
 */

public class LocaleFormater {
    private Context ctx;
    private DecimalFormat decimalFormat;
    public LocaleFormater(FloatFormatter df, Context context){
        decimalFormat = df;
        this.ctx = context;
    }

    public String catchNull(float input, int resId, int failedResId) {
        if (input == -1)
        {
            return ctx.getString(resId, ctx.getString(failedResId));
        }
        else {
            return ctx.getString(resId, decimalFormat.format(input));
        }
    }

    public String catchNull(float input, int resId, int nullResId, int unitResId) {
        if (input == -1)
        {
            return ctx.getString(resId, ctx.getString(nullResId), "");
            //return ctx.getString(nullResId);
        }
        else {
            return ctx.getString(resId, decimalFormat.format(input), ctx.getString(unitResId));
        }
    }
}
