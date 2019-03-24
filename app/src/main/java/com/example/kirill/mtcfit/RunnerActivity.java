package com.example.kirill.mtcfit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class RunnerActivity extends AppCompatActivity {


    ImageView start_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runner);
        start_map = findViewById(R.id.running);
        start_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_map();
            }
        });

    }

    public void start_map() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}
