package com.example.seabook_mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class Activity3 extends AppCompatActivity {

    private Button buttonMain2;
    private RadioButton radiobtn;
    String jsonFromServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        Intent intent = getIntent();
        String URL = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        String date = intent.getStringExtra(MainActivity.EXTRA_DATE);


        buttonMain2 = (Button) findViewById(R.id.buttonMain2);
        buttonMain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity4();
            }
        });

        new FetchDataTask().execute(URL);
    }

    private class FetchDataTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "con";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Activity3.this,"Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(String... params) {

            HttpHandler sh = new HttpHandler();

            Intent intent = getIntent();
            String url = intent.getStringExtra(MainActivity.EXTRA_TEXT);

            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);

            jsonFromServer = jsonStr;
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String books = jsonObj.toString();
                } catch (final JSONException e) {
                    //  Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
                return jsonFromServer;

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(String dataFetched) {
            //parse the JSON data and then display
            parseJSON(dataFetched);
        }

    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public static boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }

    private void parseJSON(String data){

        try{
            JSONArray jsonMainNode = new JSONArray(data);

            int jsonArrLength = jsonMainNode.length();


            for(int i=0; i < jsonArrLength; i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String id = jsonChildNode.getString("id");
                radiobtn = new RadioButton(this);
                radiobtn.setText(id);

                String isB = jsonChildNode.getString("isBooked");


                if(equals(isB,"true")){radiobtn.setText(id + " (Prenotato)");}
                LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout3);
                layout.addView(radiobtn);

            }

        }catch(Exception e){
            Log.i("App", "Error parsing data" +e.getMessage());

        }
    }

    public void openActivity4() {
        Intent intent;
        intent = new Intent(Activity3.this,
                Activity4.class);
        startActivity(intent);
    }
}