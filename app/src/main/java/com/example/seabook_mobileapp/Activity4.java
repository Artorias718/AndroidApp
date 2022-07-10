package com.example.seabook_mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class Activity4 extends AppCompatActivity {

    private Button button5;
    String[] items = {"Carta di credito","Paypal","Bonifico online"};
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);
        spinner = findViewById(R.id.spiiner);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(Activity4.this, R.layout.list_item,items);
        adapter.setDropDownViewResource(R.layout.list_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value=parent.getItemAtPosition(position).toString();
                Toast.makeText(Activity4.this, value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityPayment();
            }
        });

    }

    public void openActivityPayment() {
        Intent intent;
        intent = new Intent(Activity4.this,
                Payment.class);
        startActivity(intent);
    }
}