package com.example.seabook_mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityPost extends AppCompatActivity {

    public static final String EXTRA_TEXT = "com.example.application.example.EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Button button = (Button) findViewById(R.id.buttonPost);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityFinalPost();
            }
        });
    }

    public void openActivityFinalPost() {
        EditText editTextPost = (EditText) findViewById(R.id.editTextPost);
        String text = editTextPost.getText().toString();
        Intent intent;
        intent = new Intent(this,
                FinalPost.class);
        intent.putExtra(EXTRA_TEXT, text);
        startActivity(intent);
    }

}