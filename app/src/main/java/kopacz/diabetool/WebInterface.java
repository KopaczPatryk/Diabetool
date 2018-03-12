package kopacz.diabetool;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class WebInterface extends AsyncTask<String, Void, String> {
    private String OutputData = "";
    private String size;
    private Integer batchSize = 50;
    // Required initialization

    //private final HttpClient Client = new DefaultHttpClient();
    private String Content;
    private String Error = null;
    //public ProgressDialog Dialog = new ProgressDialog(DatabaseActivity.this);
    String data = "";
    //TextView uiUpdate = (TextView) findViewById(R.id.output);
    //TextView jsonParsed = (TextView) findViewById(R.id.jsonParsed);
    int sizeData = 0;
    //EditText serverText = (EditText) findViewById(R.id.serverText);


    protected void onPreExecute() {
        // NOTE: You can call UI Element here.

        //Start Progress Dialog (Message)
        //Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
        try {
            //Dialog.setMessage("Please wait..");
            //Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //Dialog.show();
        } catch (Exception e)
        {
            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.v("bugi", "problem z dialogiem");
        }


//            try {
//
//                // Set Request parameter
//                data += "&" + URLEncoder.encode("data", "UTF-8") + "=" + serverText.getText();
//
//            } catch (UnsupportedEncodingException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
    }

    // Call after onPreExecute method
    protected String doInBackground(String... urls) {

        //*********** Make Post Call To Web Server **********
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL(urls[0]);

            // Send POST data request

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("batch_size", batchSize.toString());
            conn.setRequestProperty("data","UTF-8");
            //conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            //conn.setRequestProperty("Content-Length","" + Integer.toString(dataUrlParameters.getBytes().length));
            //conn.setRequestProperty("Content-Language", "en-US");

            conn.setDoInput(true);
            conn.setDoOutput(true);

            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            //wr.write(data);
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line).append("");
            }

            // Append Server Response To Content String
            Content = sb.toString();
            Content = HelperClass.stripHTML(Content);
        } catch (Exception ex) {
            Error = ex.getMessage();
            Log.v("bugi", "error3");
        } finally {
            try {

                reader.close();
            } catch (Exception ex) {
                Log.v("bugi", "error2");
            }
        }

        //*****************************************************
        if (Error != null) {

            Log.v("bugi", "error");

        } else {

            // Show Response Json On Screen (activity)
            //uiUpdate.setText(Content);

            //****************** Start Parse Response JSON Data *************
            //String OutputData = "";

            try {
                JSONArray mainArray = new JSONArray(Content);
                JSONObject infoObject = mainArray.optJSONObject(0);
                JSONObject dataObject = mainArray.optJSONObject(1);
                JSONArray infoArray = infoObject.optJSONArray("products_size");

                //***** Returns the value mapped by name if it exists and is a JSONArray. ***
                //*******  Returns null otherwise.  *******
                JSONArray dataArray = dataObject.optJSONArray("products_response");
                //Toast.makeText(DatabaseActivity.this, infoArray.getJSONObject(0).optString("COUNT(*)"), Toast.LENGTH_LONG).show();
                size = infoArray.getJSONObject(1).optString("post");

                //Log.v("bugi", infoArray.getJSONObject(0).optString("COUNT(*)"));
                //Toast.makeText(DatabaseActivity.this, infoArray.getJSONObject(0).optString("COUNT(*)"), Toast.LENGTH_LONG).show();

                //Log.v("bugi", Content);

                if(dataArray == null)
                {
                    Log.v("bugi", "jsonMainNode is null");
                }

                //*********** Process each JSON Node ************

                int lengthJsonArr = dataArray.length();

                for (int i = 0; i < lengthJsonArr; i++) {
                    //****** Get Object for each JSON node.***********
                    JSONObject jsonChildNode = dataArray.getJSONObject(i);
                    //******* Fetch node values **********
                    String id = jsonChildNode.optString("id");
                    String login = jsonChildNode.optString("password");
                    String rep = jsonChildNode.optString("rep");

//                        if (i == 1){
//                            Toast.makeText(DatabaseActivity.this, login, Toast.LENGTH_SHORT).show();
//                        }

                    OutputData += "Id: " + id + "\n" +
                            "Login: " + login + "\n" +
                            "Rep: " + rep + "\n";

                    //OutputData += id + ", ";
                }

                //Toast.makeText(DatabaseActivity.this), "Error - Connection Timeout", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("bugi", e.getMessage());
            }
        }
        return null;

    }

    protected void onPostExecute(String unused) {
        //Toast.makeText(getApplicationContext(), Content, Toast.LENGTH_SHORT).show();

//            super.onPostExecute(s);
//            Dialog.dismiss();
//            serverText.setText(s);

        // NOTE: You can call UI Element here.

        // Close progress dialog

        //Dialog.dismiss();
        //Toast.makeText(DatabaseActivity.this, size, Toast.LENGTH_SHORT).show();
        //****************** End Parse Response JSON Data *************
        //Show Parsed Output on screen (activity)
        //jsonParsed.setText(OutputData);
    }
}