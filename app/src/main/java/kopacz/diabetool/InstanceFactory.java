package kopacz.diabetool;

import android.content.Context;

/**
 * Created by Kopac on 08.07.2016.
 */
public class InstanceFactory {
    private static InstanceFactory instance;
    private Context context;

    private InstanceFactory(Context applicationContext) {
        this.context = applicationContext;
    }

    public static InstanceFactory getInstance(Context ctx) {
        if (instance == null) {
            instance = new InstanceFactory(ctx.getApplicationContext());
        }
        return instance;
    }

    public static UnifiedCalculator getUnifiedCalculator() {
        return new Calculator();
    }

    public static DatabaseHelper getDatabaseHelper (Context context)
    {
        return MergedDatabaseHelper.getInstance(context);
    }
}
