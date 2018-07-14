package com.caesar.ken.coralfits;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.caesar.ken.coralfits.Utilitites.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AdminActivity extends AppCompatActivity {

    private Button englishButtton, nativeButton, customButton, gallerybutton, cameraButton;
    private ImageView adminImage;
    private EditText priceInfo;
    private StorageReference mystorage;
    public static final String TAG = "AdminActivity";
    private static final int GALLERY_REQUEST_CODE =3;
    private static final int CAMERA_REQUEST_CODE = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar admintoolbar = (Toolbar) findViewById(R.id.admintoolbar);
        setSupportActionBar(admintoolbar);
        getSupportActionBar().setTitle("Admin Template");

        englishButtton = (Button) findViewById(R.id.uploadEnglishCloth);
        mystorage = FirebaseStorage.getInstance().getReference();
        nativeButton = (Button) findViewById(R.id.uploadNativeCloth);
        customButton = (Button) findViewById(R.id.uploadCustomCloth);
        gallerybutton = (Button) findViewById(R.id.getGallery);
        cameraButton = (Button) findViewById(R.id.getCamera);
        adminImage = (ImageView)findViewById(R.id.adminImage);
        priceInfo = (EditText) findViewById(R.id.priceInfo);


        gallerybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent,GALLERY_REQUEST_CODE );

            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){

        }
        else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){


            final Uri urigallery = data.getData();

            final StorageReference filepathgallery = mystorage.child(Constants.ADMIN_PHOTOS).child(urigallery.getLastPathSegment());



            Picasso.get().load(urigallery).into(adminImage);


//ENGLISH BUTTON
            englishButtton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (priceInfo != null){
                    filepathgallery.putFile(urigallery).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AdminActivity.this, "image gotten from gallery", Toast.LENGTH_LONG).show();

                            final String uploadtext = priceInfo.getText().toString();
                            final String imageurl = taskSnapshot.getDownloadUrl().toString();

// after adding to firebase storage then add to the database adding method
                            recieveImageFromStorageEnglish(uploadtext, imageurl);
                            Toast.makeText(AdminActivity.this, "uploading finished", Toast.LENGTH_LONG).show();

                        }
                    });
                }
                         else {
                            Toast.makeText(getApplicationContext(), "you must input the price of the outfit", Toast.LENGTH_LONG).show();
                        }

                }
            });

//NATIVE BUTTON
            nativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (priceInfo != null) {
                        filepathgallery.putFile(urigallery).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(AdminActivity.this, "image gotten from gallery", Toast.LENGTH_LONG).show();

                                final String uploadtext = priceInfo.getText().toString();
                                final String imageurl = taskSnapshot.getDownloadUrl().toString();

// after adding to firebase storage then add to the database adding method
                                recieveImageFromStorageNative(uploadtext, imageurl);
                                Toast.makeText(AdminActivity.this, "uploading finished", Toast.LENGTH_LONG).show();

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "you must input the price of the outfit", Toast.LENGTH_LONG).show();
                    }
                }
                    });




//CUSTOM BUTTON
            customButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (priceInfo.getText().toString() != null) {
                        filepathgallery.putFile(urigallery).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(AdminActivity.this, "image gotten from gallery", Toast.LENGTH_LONG).show();

                                final String uploadtext = priceInfo.getText().toString();
                                final String imageurl = taskSnapshot.getDownloadUrl().toString();

// after adding to firebase storage then add to the database adding method

                                Toast.makeText(AdminActivity.this, "uploading Image Succesful", Toast.LENGTH_LONG).show();
                                recieveImageFromStorageCustom(uploadtext, imageurl);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminActivity.this, "uploading Image failed", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(AdminActivity.this, "you must input the price of the outfit", Toast.LENGTH_LONG).show();
                    }
                        }
                    });

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    //receive image from firebase storage and send to firebasedatabase
    public void recieveImageFromStorageEnglish(String title, String imageUrl){
        CoralModelClass coralModelClassInstance = new CoralModelClass(title, imageUrl);
        Log.d(TAG, "this is the title and imageId " + title+ " imageId- " + imageUrl+" thanks eshe o");
        adminImage.setImageResource(R.drawable.corrallogo);
        FirebaseDatabase.getInstance().getReference().child(Constants.ENGLISH_WEARS).child(String.valueOf(System.currentTimeMillis()))
                .setValue(coralModelClassInstance);
    }
    public void recieveImageFromStorageNative(String title, String imageUrl){
        CoralModelClass coralModelClassInstance = new CoralModelClass(title, imageUrl);
        Log.d(TAG, "this is the title and imageId " + title+ " imageId- " + imageUrl+" thanks eshe o");
        adminImage.setImageResource(R.drawable.corrallogo);
        FirebaseDatabase.getInstance().getReference().child(Constants.NATIVE_WEARS).child(String.valueOf(System.currentTimeMillis()))
                .setValue(coralModelClassInstance);
    }

    public void recieveImageFromStorageCustom(String title, String imageUrl){
        CoralModelClass coralModelClassInstance = new CoralModelClass(title, imageUrl);
        Log.d(TAG, "this is the title and imageId " + title+ " imageId- " + imageUrl+" thanks eshe o");
        adminImage.setImageResource(R.drawable.corrallogo);
        FirebaseDatabase.getInstance().getReference().child(Constants.CUSTOM_WEARS).child(String.valueOf(System.currentTimeMillis()))
                .setValue(coralModelClassInstance);
    }
}
