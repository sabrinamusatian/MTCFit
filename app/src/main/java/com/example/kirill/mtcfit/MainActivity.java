package com.example.kirill.mtcfit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Main activity which launches map view and handles Android run-time requesting permission.
 */

public class MainActivity extends Activity {

    Button start_map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start_map = findViewById(R.id.start_map);
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