package kopacz.diabetool;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Kopac on 14.09.2016.
 */
public class DatabaseUpdaterService extends Service {
    private static String TAG = "DBUpdater";

    PrefsManager prefsManager;
    Handler uiHandler = new Handler();
    Handler backgroundHandler;
    private HandlerThread thread;

    private int batchLimit = 100;
    //private int batchOffset = 0;
    private RequestQueue requestQueue;
    private DatabaseHelper dh;
    private Context ctx;
    private long productDownloadCounter = 0;
    boolean isDownloading = false;

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "DB Updater oncreate");
        ctx = getApplicationContext();

        prefsManager = PrefsManager.getInstance(ctx);
        thread = new HandlerThread("DBUpdate", Thread.NORM_PRIORITY);
        thread.start();

        backgroundHandler = new Handler(thread.getLooper());
        requestQueue = Volley.newRequestQueue(ctx);
        dh = InstanceFactory.getDatabaseHelper(getApplicationContext());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "DB Updater onStartCommand");
        backgroundHandler.post(new updateRunnable());
        // If we get killed, after returning from here, restart
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "DB Updater ondestroy");

        //Toast.makeText(this, "service done: " + c, Toast.LENGTH_SHORT).show();
    }
    private synchronized void queueUpdate()
    {
        //Toast.makeText(ctx, "DUS request: " + productDownloadCounter, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = VolleyHelper.queryProducts(batchLimit, (int) productDownloadCounter, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String myResponse = HelperClass.unwrapJSONfromInterfaceSrc(response);
                            JSONObject object = new JSONObject(myResponse);
                            final JSONArray products = object.getJSONArray("products");

                            int arraySize = products.length();
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ctx, getString(R.string.db_updater_downloading_batch_of_products) + products.length(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            //Log.v("dbu", "DUS lenght: " + products.length());
                            Log.v(TAG, "Batch: " + productDownloadCounter);

                            for (int i = 0; i < products.length(); i++) {
                                JSONObject productJSON = (JSONObject) products.get(i);
                                Product p = new Product();
                                p.setRid(productJSON.getLong("I"));

                                p.setName(productJSON.getString("N"));
                                p.setEnergyValue(Float.parseFloat(productJSON.getString("E")));
                                p.setProteins(Float.parseFloat(productJSON.getString("P")));
                                p.setFat(Float.parseFloat(productJSON.getString("F")));
                                p.setCarbohydrates(Float.parseFloat(productJSON.getString("C")));
                                p.setRoughage(Float.parseFloat(productJSON.getString("R")));
                                p.setSugar(Float.parseFloat(productJSON.getString("S")));
                                p.setCaffeine(Float.parseFloat(productJSON.getString("CA")));
                                p.setBarcode(productJSON.getString("B"));
                                p.setBaseWeight(100f);
                                //
                                p.setDateCreated(productJSON.getLong("D"));

                                p.setSent(true);
                                productDownloadCounter++;
                                prefsManager.setLastDownloadedProductIndex(productDownloadCounter);
                                prefsManager.apply();

                                dh.addProduct(p);
                            }
                            if (arraySize == batchLimit) {
                                //batchOffset += batchLimit;
                                queueUpdate();
                            } else { //ostatni produkt pobrany
                                prefsManager.setDatabaseUpdateFullDone(true); //pobrano wszystkie produkty z publica
                                prefsManager.apply();
                                isDownloading = false;
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ctx, getString(R.string.db_updater_downloading_done), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            Log.v(TAG, Arrays.toString(e.getStackTrace()));
                            //e.printStackTrace();
                        }
                    }
                };
                backgroundHandler.post(runnable);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ctx, R.string.db_updater_connection_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);

    }
    private class updateRunnable implements Runnable {
        @Override
        public void run() {
            Log.v(TAG, "Updater działa na wątku: " + thread.getLooper().getThread().toString());

            try {
                //Toast.makeText(getApplicationContext(), "service starting", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "DB Updater runnable starting");
//
//                    editor.remove("DB_update_fulldone");
//                    editor.remove("DB_update_lastindex");
//                    editor.commit();
                Log.v(TAG, "przed petla");
                Log.v(TAG, "full done update: " + prefsManager.getDatabaseUpdateFullDone());

                if (!prefsManager.getDatabaseUpdateFullDone() && !isDownloading)
                {
                    isDownloading = true;
                    Log.v(TAG, "W pętli pobierania rekordów");
                    //Toast.makeText(ctx, "DUS queue", Toast.LENGTH_SHORT).show();
                    productDownloadCounter = prefsManager.getLastDownloadedProductIndex();
                    queueUpdate();
                }
            } catch (Exception e) {
                //TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
