package com.example.kirill.mtcfit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Main activity which launches map view and handles Android run-time requesting permission.
 */

public class MainActivity extends Activity {

    ImageView start_map;
    ImageView rate;
    ImageView start_friends;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start_map = findViewById(R.id.running);
        start_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_map();
            }
        });
        start_map = findViewById(R.id.rate);
        start_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_rate();
            }
        });
        start_friends = findViewById(R.id.friends);
        start_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFriends();
            }
        });
    }

    public void start_map() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void start_rate() {
        Intent intent = new Intent(this, RateActivity.class);
        startActivity(intent);
    }
    public void getFriends(){
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }
}