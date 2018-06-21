package com.caesar.ken.coralfits;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class OrderActivity extends AppCompatActivity {

    private Button uploadButton;
    private Button gallerybutton;
    ImageView cameraImage;
    private StorageReference mystorage;
    private static final int GALLERY_REQUEST_CODE =2;
    private static final int CAMERA_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.ordertoolbar);
        setSupportActionBar(toolbar);
        ActionBar toolbaractionBar = getSupportActionBar();
        toolbaractionBar.setDisplayHomeAsUpEnabled(true);

        mystorage = FirebaseStorage.getInstance().getReference();
        uploadButton = (Button) findViewById(R.id.uploadbutton);
        cameraImage  = (ImageView) findViewById(R.id.imageorder);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });

    }
    public void uploadGallery(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_REQUEST_CODE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
            //this below containsthe data of the captured image
            Uri uri = data.getData();
//            Bundle extras = data.getExtras();
//            //get photo info and tyecast to a bitmap
//            Bitmap photo = (Bitmap) extras.get("data");
//            cameraImage.setImageBitmap(photo);
            //this is the file path to store the images thatb are captured
            StorageReference filepathcamera = mystorage.child("Photos").child("EnglishWears");
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
        }else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            Uri urigallery = data.getData();
            StorageReference filepathgallery = mystorage.child("Photos").child("EnglishWears");
            filepathgallery.putFile(urigallery).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(OrderActivity.this, "uploading finished", Toast.LENGTH_LONG).show();

                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
