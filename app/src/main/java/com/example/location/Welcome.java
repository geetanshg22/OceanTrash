package com.example.location;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import android.location.LocationListener;

public class Welcome extends AppCompatActivity  {

    // private Button btnGetLocation = null;
    //public EditText editLocation = null;
    //private ProgressBar pb = null;

    private static final String TAG = "Debug";
    private Boolean flag = false;
    // public TextView register = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        FirebaseApp.initializeApp(this);

        Button physicsButton = findViewById(R.id.physicsButton);
        TextView login = findViewById(R.id.welcomeLoginButton);
        TextView register = findViewById(R.id.welcomeRegisterButton);

        final SaveSharedPreferences saveSharedPreferences = new SaveSharedPreferences();
        if (!saveSharedPreferences.getPrefEmail(Welcome.this).isEmpty()) {
            login.setVisibility(View.INVISIBLE);
            register.setText("Log out");
        } else {
            login.setVisibility(View.VISIBLE);
            register.setText("Register");
        }

        physicsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveSharedPreferences.getPrefEmail(Welcome.this).isEmpty()) {
                    Intent intent = new Intent(Welcome.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Welcome.this, sendDetail.class);
                    startActivity(intent);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveSharedPreferences.getPrefEmail(Welcome.this).isEmpty()) {
                    Intent intent = new Intent(Welcome.this, Register.class);
                    startActivity(intent);
                } else {
                    saveSharedPreferences.setPrefEmail(Welcome.this, "");
                    Intent intent = new Intent(Welcome.this, Welcome.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Press home Button",Toast.LENGTH_SHORT).show();
    }
}
