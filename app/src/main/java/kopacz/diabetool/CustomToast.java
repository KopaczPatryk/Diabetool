package kopacz.diabetool;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Kopac on 02.09.2016.
 */

//todo przerobić na klasę niestatyczną i dodać interfejs.
public class CustomToast {
    public static Toast infoToast (Context context, String text) // Utworzono. (z kropką)
    {
        return Toast.makeText(context, text, Toast.LENGTH_SHORT);
    }
    public static Toast noticeToast (Context context, String text) // [ ? ] Utworzono. (żółty z kropką)
    {
        return Toast.makeText(context, text, Toast.LENGTH_SHORT);
    }
    public static Toast warningToast (Context context, String text) // [ ! ] Utworzono! (kontrastowy z wykrzyknikiem)
    {
        return Toast.makeText(context, text, Toast.LENGTH_SHORT);
    }
}
