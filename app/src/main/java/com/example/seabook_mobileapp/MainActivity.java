package com.example.seabook_mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.api.net.proxy.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText etDate;
    DatePickerDialog.OnDateSetListener setListener;
    private Button buttonMain;
    String jsonFromServer;
    ListView listView;
    Button newbtn;
    public static final String EXTRA_TEXT = "com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_DATE = "com.example.application.example.EXTRA_TEXT";
    public static final String IP = "192.168.1.16";

    private final static String URL = "http://"+IP+":8080/api/v1/stabilimenti/";
    ArrayList<String> tutorialList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.buttonMainPost);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityPost();
            }
        });

        etDate = findViewById(R.id.et_date);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        etDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        new FetchDataTask().execute(URL);
    }

    private class FetchDataTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "con";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(String... params) {

            HttpHandler sh = new HttpHandler();

            String url = "http://"+IP+":8080/api/v1/stabilimenti/";

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

    private void parseJSON(String data){

        try{
            JSONArray jsonMainNode = new JSONArray(data);

            int jsonArrLength = jsonMainNode.length();

            for(int i=0; i < jsonArrLength; i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.getString("name");
                String id = jsonChildNode.getString("id");
                newbtn = new Button(this);
                newbtn.setText("Prenota-"+id);
                newbtn.setMaxWidth(30);
                tutorialList.add(name+"  "+id);
                LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout2);
                layout.addView(newbtn);
                newbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openActivity3(id);
                    }
                });

            }

            // Get ListView object from xml
            listView = (ListView) findViewById(R.id.list);

            // Define a new Adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, tutorialList);

            // Assign adapter to ListView
            listView.setAdapter(adapter);

        }catch(Exception e){
            Log.i("App", "Error parsing data" +e.getMessage());

        }
    }

    public void openActivity3(String id) {
        EditText editText = (EditText) findViewById(R.id.et_date);
        String data = editText.getText().toString();

        Log.e("", "Response from url: " + data);

        String string = data;
        String[] parts = string.split("/");
        String part1 = parts[0]; // 004
        String part2 = parts[1];
        String part3 = parts[2];

        String parsedString = part3 + "-" + part2 + "-" + part1;

        //Log.e("", "Response from url: " + parsedString);

        String URL ="http://"+IP+":8080/api/v1/stabilimenti/"+id+"/lista_Posti/" + parsedString;


        Log.e("", "Response from url: " + URL);

        Intent intent;
        intent = new Intent(this,
                Activity3.class);
        intent.putExtra(EXTRA_TEXT, URL);

        startActivity(intent);
    }

    public void openActivityPost() {
        Intent intent;
        intent = new Intent(this,
                ActivityPost.class);
        startActivity(intent);
    }

}