package com.novacompany.iotworkshoptest;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String ON = "ON";
    private static final String OFF = "OFF";
    private static final String SKY_DARK = "#183984";
    private static final String SKY_LIGHT = "#a3bbf4";

    DatabaseReference switcherRef, ledRef;

    View background;
    ImageView light;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        switcherRef = database.getReference("switcher");
        ledRef = database.getReference("led");
        switcherRef.addValueEventListener(switcherValueListener);
        ledRef.addValueEventListener(ledValueListener);
//        switcherRef.setValue(0);
//        ledRef.setValue(0);

        background = findViewById(R.id.activity_main);
        light = (ImageView) findViewById(R.id.imageView);
        ToggleButton button = (ToggleButton) findViewById(R.id.toggleButton);
        button.setOnCheckedChangeListener(btnListener);
    }

    ValueEventListener switcherValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            if(dataSnapshot != null && dataSnapshot.getChildren() != null && dataSnapshot.getValue() != null) {
                int value = dataSnapshot.getValue(Integer.class);
                boolean isOn = value == 1;
                int newImage = isOn ? R.drawable.ic_sun_144dp : R.drawable.ic_moon_144dp;
                int newColor = isOn ? Color.parseColor(SKY_LIGHT) : Color.parseColor(SKY_DARK);
                background.setBackgroundColor(newColor);
                light.setImageResource(newImage);
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
            Log.w(TAG, "Failed to read value.", error.toException());
        }
    };

    ValueEventListener ledValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            if(dataSnapshot == null || dataSnapshot.getChildren() == null || dataSnapshot.getValue() == null) {

            } else {
                int value = dataSnapshot.getValue(Integer.class);
                Log.d(TAG, "Value is: " + value);
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
            Log.w(TAG, "Failed to read value.", error.toException());
        }
    };

    CompoundButton.OnCheckedChangeListener btnListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            ledRef.setValue(b ? 1 : 0);
        }
    };
}
