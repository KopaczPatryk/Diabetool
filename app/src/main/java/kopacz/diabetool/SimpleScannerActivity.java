package kopacz.diabetool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SimpleScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private static final String TAG = "TAG_ZXING";
    private ZXingScannerView mScannerView;
    private SimpleScannerActivity that = this;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
        mScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If you would like to resume scanning, call this method below:
                mScannerView.resumeCameraPreview(that);
                //Toast.makeText(getApplicationContext(), , Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mScannerView.stopCamera();
    }
    */

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(com.google.zxing.Result result) {
        // Do something with the result here
        //Log.v(TAG, result.getText()); // Prints scan results
        //Log.v(TAG, result.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        //Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
        //mScannerView.resumeCameraPreview(that); //resetuje czytnik
        Intent intent = getIntent();
        intent.putExtra("QR", result.getText());
        setResult(RESULT_OK, intent);
        finish();// zamyka activity
    }
}