package com.example.seabook_mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class FinalPost extends AppCompatActivity {

    private static final String TAG = "con";

    private Button buttonFinalPost;
    public static final String IP = "192.168.1.16";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_post);

        Intent intent = getIntent();
        String nomeStabilimento = intent.getStringExtra(ActivityPost.EXTRA_TEXT);

        buttonFinalPost = (Button) findViewById(R.id.buttonFinalPost);
        buttonFinalPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityHome();
            }
        });

        new CallAPI().execute(nomeStabilimento);
    }


    private class CallAPI extends AsyncTask<String, Integer, Double> {

        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                postData(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Double result) {
            //pb.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "command sent",
                    Toast.LENGTH_LONG).show();
        }

        protected void onProgressUpdate(Integer... progress) {
            //pb.setProgress(progress[0]);
        }

        public void postData(String valueIWantToSend) throws IOException {

            String jsonInputString = "{\"name\": " + valueIWantToSend + "}";

            URL url = new URL("http://"+IP+":8080/api/v1/stabilimenti/create");
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setRequestProperty("name",valueIWantToSend);

            client.setRequestProperty("Content-Type","application/json");
            client.setDoOutput(true);

            JSONObject jsonBody = new JSONObject();

            try {
                jsonBody.put("name", valueIWantToSend);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try(OutputStream os = client.getOutputStream()) {
                byte[] input = jsonBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            client.getResponseCode();
            //Log.e(TAG, "Response Code :" + client.getResponseCode() + " ");

        }

    }

    private void writeStream(OutputStream out) throws IOException {
        String output = "Hello world";

        out.write(output.getBytes());
        out.flush();
    }

    public void openActivityHome() {
        Intent intent;
        intent = new Intent(this,
                MainActivity.class);
        startActivity(intent);
    }


}