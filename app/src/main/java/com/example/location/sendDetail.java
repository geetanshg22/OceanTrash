package com.example.location;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class sendDetail extends AppCompatActivity {

    private Button btnLocation,allUploads;
    private ImageView imageView;

    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;

    private final int PICK_IMAGE_REQUEST = 71;

    // Folder path for Firebase Storage.
    String Storage_Path = "ALL/";

    // Root Database Name for Firebase Database.
    public static final String Database_Path = "ALL";

    // Creating button.
    Button ChooseButton, UploadButton;

    // Creating EditText.
    EditText ImageName ;

    // Creating ImageView.
    ImageView SelectImage;

    // Creating URI.
    Uri FilePathUri;
    int count = 0;

    // Creating StorageReference and DatabaseReference object.
    DatabaseReference databaseReference;

    // Image request code for onActivityResult() .
    int Image_Request_Code = 7;

    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        btnLocation = findViewById(R.id.currLocation);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sendDetail.this,getLocation.class);
                startActivity(intent);
            }
        });

        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference();

        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        //Assign ID'S to button.
        ChooseButton = (Button)findViewById(R.id.btnChoose);
        UploadButton = (Button)findViewById(R.id.btnUpload);
        allUploads = findViewById(R.id.allUpload);


        // Assign ID's to EditText.
        ImageName = (EditText)findViewById(R.id.ImageNameEditText);

        // Assign ID'S to image view.
        SelectImage = (ImageView)findViewById(R.id.imgView);

        // Assigning Id to ProgressDialog.
        progressDialog = new ProgressDialog(sendDetail.this);

        // Adding click listener to Choose image button.
        ChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Creating intent.
                Intent intent = new Intent();

                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);

            }
        });


        // Adding click listener to Upload image button.
        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!String.valueOf(ImageName.getText()).isEmpty()){
                    SaveSharedPreferences ssp = new SaveSharedPreferences();
                    ssp.setPrefLocation(sendDetail.this,String.valueOf(ImageName.getText()));
                }
                // Calling method to upload selected image on Firebase storage.
                UploadImageFileToFirebaseStorage();

            }
        });

        allUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent intent = new Intent(sendDetail.this,DisplayImagesActivity.class);
               startActivity(intent);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                SelectImage.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                ChooseButton.setText("Image Selected");

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            final StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            // Getting image name from EditText and store into string variable.
                            String TempImageName = ImageName.getText().toString().trim();

                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

                            while(!urlTask.isSuccessful());
                            @SuppressWarnings("VisibleForTests")
                            Uri downloadUrl = urlTask.getResult();
                            SaveSharedPreferences ssp = new SaveSharedPreferences();
                            String s = ssp.getPrefLocation(sendDetail.this);
                            ImageUploadInfo imageUploadInfo = new ImageUploadInfo(s, downloadUrl.toString());

                            // Getting image upload ID.
                            String ImageUploadId = databaseReference.push().getKey();

                            // Adding image upload id s child element into databaseReference.
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                            getSetter gs = new getSetter();
                            double longitudeDou = Double.parseDouble(ssp.getPrefLongitude(sendDetail.this));
                            double latitudeDou =  Double.parseDouble(ssp.getPrefLatitude(sendDetail.this));
                            int latitude = (int)latitudeDou;
                            int longitude = (int)longitudeDou;
                            if(longitude >= 55){
                                ssp.setPrefLatitude(sendDetail.this,"-9.45382168");
                                ssp.setPrefLongitude(sendDetail.this,"127.41398944");
                                ssp.setPrefCountDensity(sendDetail.this,"2518509.0100000002");
                                ssp.setBetweenCountries(sendDetail.this,"Saint Helena -- Lower Atlantic Ocean");
                            }
                            if(longitude < 55 && longitude >=-50 && latitude<=5){
                                ssp.setPrefLatitude(sendDetail.this,"-34.24673366");
                                ssp.setPrefLongitude(sendDetail.this,"-8.09335271");
                                ssp.setPrefCountDensity(sendDetail.this,"7334003.2");
                                ssp.setBetweenCountries(sendDetail.this,"USA,Hawaii--Atlantic Ocean--Great Pacific Garbage");
                            }
                            if(longitude<-50 && latitude<-20){
                                ssp.setPrefLatitude(sendDetail.this,"-30.92298246");
                                ssp.setPrefLongitude(sendDetail.this,"-95.1377193");
                                ssp.setPrefCountDensity(sendDetail.this,"1334085.61");
                                ssp.setBetweenCountries(sendDetail.this,"Lori-East Timor--Between Indian and Pacific Ocean");
                            }
                            if(longitude<=25 && longitude>=-90 && latitude>=10){
                                ssp.setPrefLatitude(sendDetail.this,"35.3682289");
                                ssp.setPrefLongitude(sendDetail.this,"-25.64265607");
                                ssp.setPrefCountDensity(sendDetail.this,"1334085.61");
                                ssp.setBetweenCountries(sendDetail.this,"Portugal,Sul--Upper Altantic Ocean");
                            }
                            if(longitude<-90 && latitude>=10){
                                ssp.setPrefLatitude(sendDetail.this,"33.14634808");
                                ssp.setPrefLongitude(sendDetail.this,"-151.15573907");
                                ssp.setPrefCountDensity(sendDetail.this,"19290549.950000014");
                                ssp.setBetweenCountries(sendDetail.this,"Chile--Lower Pacific Ocean");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(sendDetail.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Image is Uploading...");

                        }
                    });
            Intent intent = new Intent(sendDetail.this,Finally.class);
            startActivity(intent);

        }
        else {

            Toast.makeText(sendDetail.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }

}
