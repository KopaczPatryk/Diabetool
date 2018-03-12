package kopacz.diabetool;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by Kopac on 20.08.2016.
 */
public class FloatFormatter extends DecimalFormat {
    public FloatFormatter ()
    {
        super();
        this.setDecimalSeparatorAlwaysShown(false);
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        this.setDecimalFormatSymbols(decimalFormatSymbols);
    }
}
