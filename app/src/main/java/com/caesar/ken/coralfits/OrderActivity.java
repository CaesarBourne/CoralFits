package com.caesar.ken.coralfits;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.caesar.ken.coralfits.Utilitites.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class OrderActivity extends AppCompatActivity {

    private Button uploadCamera, adminIntentButton;
    private Button gallerybutton, uploadAdmin;
    ImageView cameraImage;
    private StorageReference mystorage;
    public EnglishWearsFragment englishWearsFragmentInstance;
    private static final int GALLERY_REQUEST_CODE = 2;
    private static final int CAMERA_REQUEST_CODE = 1;
    public static final String EXTRA_CARD_VIEW_image = "cardView";
    public static final String EXTRA_CARD_VIEW_title = "arraylist";
    public static final String TAG = "OrderActivity";
    private EditText dataInfo, nameInfo;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        Toolbar toolbar = (Toolbar) findViewById(R.id.ordertoolbar);
        setSupportActionBar(toolbar);
        ActionBar toolbaractionBar = getSupportActionBar();
        toolbaractionBar.setDisplayHomeAsUpEnabled(true);

        mystorage = FirebaseStorage.getInstance().getReference();
        uploadCamera = (Button) findViewById(R.id.uploadcamera);
        uploadAdmin = (Button) findViewById(R.id.uploadAdmin);
        cameraImage = (ImageView) findViewById(R.id.imageorder);
        dataInfo = (EditText) findViewById(R.id.datainfo);
        nameInfo = (EditText) findViewById(R.id.nameInfo);
        gallerybutton = (Button) findViewById(R.id.uploadGallery);
        adminIntentButton = (Button) findViewById(R.id.adminIntentButton);
//        mProgressView = findViewById(R.id.upload_progress);

        gallerybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, GALLERY_REQUEST_CODE);

            }
        });

        uploadCamera.setContentDescription("Camera upload");
//        if (FirebaseAuth.getInstance().getCurrentUser().getEmail().toString() == "faxchat@gmail.com"){
//            adminIntentButton.setVisibility(View.VISIBLE);
//        }else {
//            adminIntentButton.setVisibility(View.INVISIBLE);
//        }
        String emailUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        Log.d(TAG, "THis is the email of the signed in user: " + emailUser + " ese o");
        adminIntentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });


        uploadAdmin.setVisibility(View.INVISIBLE);

        uploadCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });
        //IF image and text is part from the cardview of the fragments
        if (getIntent().getExtras() != null) {
            final String image = getIntent().getExtras().getString(EXTRA_CARD_VIEW_image);
            final String title = getIntent().getExtras().getString(EXTRA_CARD_VIEW_title);
            Picasso.get().load(image).into(cameraImage);
            cameraImage.setContentDescription(title);
            uploadAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    justSendToAdmin(title, image);
                    Toast.makeText(OrderActivity.this, "uploading finished", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            cameraImage.setImageDrawable(getResources().getDrawable(R.drawable.ordercloths));
            cameraImage.setContentDescription("");
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void justSendToAdmin(String text, String imageData) {
        final StorageReference filepathEnglish = mystorage.child("Photos").child(String.valueOf(System.currentTimeMillis()));
        Uri fragmentUri = Uri.parse(text);
        filepathEnglish.putFile(fragmentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(OrderActivity.this, "uploading complete", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrderActivity.this, "uploading failed", Toast.LENGTH_LONG).show();
            }
        });

    }


//    //the onclick
//    public void uploadGallery(View view){
//            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            //this below containsthe data of the captured image
            Uri uri = data.getData();
//            Bundle extras = data.getExtras();
//            //get photo info and tyecast to a bitmap
//            Bitmap photo = (Bitmap) extras.get("data");
//            cameraImage.setImageBitmap(photo);
            //this is the file path to store the images thatb are captured
            StorageReference filepathcamera = mystorage.child("Photos").child(uri.getLastPathSegment());
            //this is for admin upload
            filepathcamera.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(OrderActivity.this, "uploading complete", Toast.LENGTH_LONG).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(OrderActivity.this, "uploading failed", Toast.LENGTH_LONG).show();
                }
            });
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            final Uri urigallery = data.getData();
            final StorageReference filepathStorage = mystorage.child("Photos").child(urigallery.getLastPathSegment());
            Log.d(TAG, "this is the last path segment "+ urigallery.getLastPathSegment() + " thanks for seeing it");
//            dataInfo.setText("");
//            nameInfo.setText("");
//            final String imageurl = taskSnapshot.getDownloadUrl().toString();
            uploadAdmin.setVisibility(View.VISIBLE);
            uploadCamera.setVisibility(View.INVISIBLE);
            gallerybutton.setVisibility(View.INVISIBLE);
            Picasso.get().load(urigallery).into(cameraImage);
            uploadAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!validateForm()){
                        Toast.makeText(OrderActivity.this, "one od the fields is incorrect or empty", Toast.LENGTH_LONG).show();
                        return;
                    }try{



                    filepathStorage.putFile(urigallery).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepathStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String uploadtext = dataInfo.getText().toString();
                                    final String imageUrl = uri.toString();
                                    recieveImageFromStorage(uploadtext,imageUrl);
                                    Toast.makeText(OrderActivity.this, "uploading finished", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });


//            uploadAdmin.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    filepathgallery.putFile(urigallery).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(OrderActivity.this, "image gotten from gallery", Toast.LENGTH_LONG).show();
//                            if (dataInfo != null){
//                                final String uploadtext = dataInfo.getText().toString();
//                                final String imageurl = taskSnapshot.getDownloadUrl().toString();
//
////                        cameraImage.setImageDrawable(getResources().getDrawable((Drawable)urigallery));
//// after adding to firebase storage then add to the database adding method
//                                recieveImageFromStorage(uploadtext,imageurl);
//                                Toast.makeText(OrderActivity.this, "uploading finished", Toast.LENGTH_LONG).show();
//
//                            }
//                            else {
//                                Toast.makeText(getApplicationContext(), "you must input tge dta name to upload", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//                }
//            });

        }
        super.onActivityResult(requestCode, resultCode, data);

    }
    public boolean validateForm() {
        boolean valid = true;

        String nameemail = nameInfo.getText().toString();
        if (TextUtils.isEmpty(nameemail)){
            valid = false;
            nameInfo.setError("you must put in your email address");}
            else if (!nameemail.contains("@")) {
                valid = false;
                nameInfo.setError("not a valid email address");
        }else{
            nameInfo.setError(null);
        }

        String dataText = dataInfo.getText().toString();
        if (TextUtils.isEmpty(dataText)){
            valid = false;
            dataInfo.setError("You must put in Info about your choice");
        }
        else {
            dataInfo.setError(null);

        }

        return valid;
    }


    //receive image from firebase storage and send to firebasedatabase
    public void recieveImageFromStorage(String title, String imageUrl){
        CoralModelClass coralModelClassInstance = new CoralModelClass(title, imageUrl);
        Log.d(TAG, "this is the title and imageId " + title+ " imageId- " + imageUrl+" thanks eshe o");
        FirebaseDatabase.getInstance().getReference().child(Constants.ADMIN_VIEW).child(String.valueOf(System.currentTimeMillis()))
                .setValue(coralModelClassInstance);
    }
}

