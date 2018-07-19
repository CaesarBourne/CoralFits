package com.caesar.ken.coralfits;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

            final StorageReference filepathStorage = mystorage.child(Constants.ADMIN_PHOTOS).child(urigallery.getLastPathSegment());



            Picasso.get().load(urigallery).into(adminImage);


//ENGLISH BUTTON
            englishButtton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!validateForm()){
                        Toast.makeText(AdminActivity.this, "price field is incorrect or empty", Toast.LENGTH_LONG).show();
                        return;
                    }
                    try{
                    filepathStorage.putFile(urigallery).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepathStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String uploadtext = priceInfo.getText().toString();
                                    final String imageUrl = uri.toString();
                                    recieveImageFromStorageEnglish(uploadtext,imageUrl);
                                    Toast.makeText(AdminActivity.this, "uploading finished", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }catch (Exception e){

                    }

                }
            });

//NATIVE BUTTON
            nativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!validateForm()){
                        Toast.makeText(AdminActivity.this, "price field is empty", Toast.LENGTH_LONG).show();
                        return;
                    }
                    try {
                        filepathStorage.putFile(urigallery).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                filepathStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final String uploadtext = priceInfo.getText().toString();
                                        final String imageUrl = uri.toString();
                                        recieveImageFromStorageNative(uploadtext,imageUrl);
                                        Toast.makeText(AdminActivity.this, "uploading finished", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        });
                    } catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "you must input the price of the outfit", Toast.LENGTH_LONG).show();
                    }
                }
                    });




//CUSTOM BUTTON
            customButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!validateForm()){
                        Toast.makeText(AdminActivity.this, "price field is empty", Toast.LENGTH_LONG).show();
                        return;
                    }
                    try {
                        filepathStorage.putFile(urigallery).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                filepathStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final String uploadtext = priceInfo.getText().toString();
                                        final String imageUrl = uri.toString();
                                        recieveImageFromStorageCustom(uploadtext,imageUrl);
                                        Toast.makeText(AdminActivity.this, "uploading finished", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminActivity.this, "uploading Image failed", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(AdminActivity.this, "you must input the price of the outfit", Toast.LENGTH_LONG).show();
                    }
                        }
                    });

  }
//
        super.onActivityResult(requestCode, resultCode, data);
    }
    public boolean validateForm() {
        boolean valid = true;

//        String nameemail = nameInfo.getText().toString();
//        if (TextUtils.isEmpty(nameemail)){
//            valid = false;
//            nameInfo.setError("you must put in your email address");}
//        else if (!nameemail.contains("@")) {
//            valid = false;
//            nameInfo.setError("not a valid email address");
//        }else{
//            nameInfo.setError(null);
//        }

        String dataText = priceInfo.getText().toString();
        if (TextUtils.isEmpty(dataText)){
            valid = false;
            priceInfo.setError("You must put in Info about your choice");
        }
        else {
            priceInfo.setError(null);

        }

        return valid;
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
