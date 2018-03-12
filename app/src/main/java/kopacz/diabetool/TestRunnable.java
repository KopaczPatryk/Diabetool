package kopacz.diabetool;

import android.util.Log;

/**
 * Created by Kopac on 15.10.2016.
 */
class TestRunnable implements Runnable {
    int counter = 0;
    @Override
    public void run() {
        Log.v("bugi", "Początek TestRunnable " + counter);
        funkcja();
        funkcja();
        funkcja();
        funkcja();
        funkcja();
        //SystemClock.sleep(1000);
    }
    private void funkcja ()
    {
        try {
            Thread.sleep(1000);
            Log.v("bugi", "" + counter++);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
