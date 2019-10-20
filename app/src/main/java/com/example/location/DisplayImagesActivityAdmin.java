package com.example.location;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayImagesActivityAdmin extends AppCompatActivity {

    // Creating DatabaseReference.
    DatabaseReference databaseReference;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter ;

    // Creating Progress dialog
    ProgressDialog progressDialog;

    // Creating List of ImageUploadInfo class.
    List<ImageUploadInfo> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        // Assign id to RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(this);

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Loading Images From Firebase.");

        // Showing progress dialog.
        progressDialog.show();
        list = new ArrayList<>();

        // Setting up Firebase image upload folder path in databaseReference.
        // The path is already defined in MainActivity.
        databaseReference = FirebaseDatabase.getInstance().getReference(sendDetail.Database_Path);
        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);

                    list.add(imageUploadInfo);
                }
                adapter = new RecyclerViewAdapter(getApplicationContext(), list);

                recyclerView.setAdapter(adapter);

                // Hiding the progress dialog.
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();

            }
        });

    }
    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to Logout?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SaveSharedPreferences ssp = new SaveSharedPreferences();
                ssp.setPrefEmail(DisplayImagesActivityAdmin.this,"");
                Intent intent = new Intent(DisplayImagesActivityAdmin.this,Welcome.class);
                startActivity(intent);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
}

