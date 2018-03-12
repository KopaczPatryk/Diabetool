package kopacz.diabetool;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

/**
 * Created by Kopac on 14.09.2016.
 */
public class Reporter extends Service {
    int c = 0;
    private RequestQueue requestQueue;
    private DatabaseHelper dh;
    private ArrayList<Product> array;

    Handler uiHandler = new Handler();
    Handler backgroundHandler;
    private HandlerThread thread;

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onCreate() {
        thread = new HandlerThread("DBReport", Thread.NORM_PRIORITY);
        thread.start();

        backgroundHandler = new Handler(thread.getLooper());

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        dh = InstanceFactory.getDatabaseHelper(getApplicationContext());
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        backgroundHandler.post(new ReportRunnable());
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "service done: " + c, Toast.LENGTH_SHORT).show();
    }
    private class ReportRunnable implements Runnable {
        @Override
        public void run() {
            c = 0;
            //Toast.makeText(this, "Reporting products", Toast.LENGTH_SHORT).show();
            Cursor cursor = dh.getUnsentProducts();
            if (cursor != null)
            {
                array = Product.decomposeCursorToArrayList(cursor);
                for (int i = 0; i < array.size(); i++)
                {
                    //dh.productSetSent(array.get(i).getId());
                    final int finalI = i;
                    StringRequest stringRequest = VolleyHelper.reportProduct(array.get(i), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dh.productSetSent(array.get(finalI).getId());
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), getString(R.string.reporter_toast_successfull_product_upload) + array.get(finalI).getName(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            //Toast.makeText(getApplicationContext(), "sent: " + c, Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(getApplicationContext(), "fail: " + c, Toast.LENGTH_SHORT).show();
                        }
                    });
                    c++;
                    requestQueue.add(stringRequest);
                }
            }
        }
    }
}