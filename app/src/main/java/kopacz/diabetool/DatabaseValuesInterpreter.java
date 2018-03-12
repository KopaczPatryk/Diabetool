package kopacz.diabetool;

/**
 * Created by Kopac on 11.10.2016.
 */

public class DatabaseValuesInterpreter {
    /*Context ctx;

    public DatabaseValuesInterpreter (Context context) {
        this.ctx = context;
    }

    public String catchNull(float input, int resId, int failedResId) {
        if (input == -1)
        {
            return ctx.getString(failedResId);
        }
        else {
            return ctx.getString(resId, input);
        }

    }*/
    public static String interpret(String string)
    {
        switch (string)
        {
            case "-1":
                return "--- ";
            default:
                return string;
        }
    }
}
