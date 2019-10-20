package com.example.location;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Finally extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finally);

        TextView latitude = findViewById(R.id.latitudeText);
        TextView longitude = findViewById(R.id.longitudeText);
        TextView between = findViewById(R.id.betweenCountries);
        TextView density = findViewById(R.id.density);
        Button locateMap = findViewById(R.id.gotomap);

        final SaveSharedPreferences ssp = new SaveSharedPreferences();
        latitude.setText("-9.23");
        longitude.setText("127.56");
        density.setText("2518509.0100000002");
        between.setText("Between Indian And Pacific Ocean");

        locateMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri intentUri = Uri.parse("geo:-9.23,127.56");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW,intentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if(mapIntent.resolveActivity(getPackageManager())!=null){
                    startActivity(mapIntent);
                }
            }
        });


    }
}
