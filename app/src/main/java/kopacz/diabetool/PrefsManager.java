package kopacz.diabetool;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Kopac on 31.10.2016.
 */

class PrefsManager {
    private static String TAG = "prefsy";
    private static String DBU_LAST_ID = "DB_update_lastindex";
    private static String DBU_FULL_DONE = "DB_update_fulldone";
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    private static PrefsManager mInstance;

    public PrefsManager(Context applicationContext) {
        prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        editor = prefs.edit();
    }

    public static PrefsManager getInstance(Context applicationContext) {
        if(mInstance == null) mInstance = new PrefsManager(applicationContext);
        return mInstance;
    }
    synchronized void setLastDownloadedProductIndex(long count)
    {
        Log.v(TAG, "setLastDownloadedProductIndex: " + count);
        editor.putLong(DBU_LAST_ID, count);
    }
    synchronized long getLastDownloadedProductIndex()
    {
        Log.v(TAG, "getLastDownloadedProductIndex: " + prefs.getLong(DBU_LAST_ID, -1));
        return prefs.getLong(DBU_LAST_ID, -1);
    }
    synchronized void setDatabaseUpdateFullDone (boolean bool)
    {
        Log.v(TAG, "setDatabaseUpdateFullDone: " + bool);
        editor.putBoolean(DBU_FULL_DONE, bool);
    }
    synchronized boolean getDatabaseUpdateFullDone ()
    {
        Log.v(TAG, "getDatabaseUpdateFullDone: " + prefs.getBoolean(DBU_FULL_DONE, false));
        return prefs.getBoolean(DBU_FULL_DONE, false);
    }
    synchronized void apply ()
    {
        Log.v(TAG, "applied");
        editor.apply();
    }
}

