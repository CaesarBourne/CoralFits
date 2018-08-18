package com.caesar.ken.coralfits;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caesar.ken.coralfits.CorralPayment.TestPayStack;
import com.caesar.ken.coralfits.Utilitites.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class OrderActivity extends AppCompatActivity {

    private Button uploadCamera, adminIntentButton, startPaystack, startcustomdata;
    private Button gallerybutton, uploadAdmin;
    ImageView cameraImage;
    private StorageReference mystorage;
    public EnglishWearsFragment englishWearsFragmentInstance;
    private static final int GALLERY_REQUEST_CODE = 2;
    private static final int CAMERA_REQUEST_CODE = 1;
    public static final String EXTRA_CARD_VIEW_image = "cardView";
    public static final String EXTRA_CARD_VIEW_title = "arraylist";

    public static final String TAG = "OrderActivity";
    private EditText dataInfo, nameInfo, customername;

    private View mProgressView;
    private LinearLayout mLoginFormView;

    public static void startAdminActivity(Context context){
        Intent intent = new Intent(context, AdminActivity.class);
        context.startActivity(intent);
    }
    public static void startAdminActivity(Context context, int flags){
        Intent intent = new Intent(context, AdminActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        mLoginFormView = findViewById(R.id.orderlinear);
        mProgressView = findViewById(R.id.upload_progress);
        Toolbar toolbar = (Toolbar) findViewById(R.id.ordertoolbar);
        setSupportActionBar(toolbar);
        ActionBar toolbaractionBar = getSupportActionBar();
        toolbaractionBar.setDisplayHomeAsUpEnabled(true);

        mystorage = FirebaseStorage.getInstance().getReference();
        startPaystack = findViewById(R.id.startPayActivity);
        uploadCamera = (Button) findViewById(R.id.uploadcamera);
        uploadAdmin = (Button) findViewById(R.id.uploadAdmin);
        cameraImage = (ImageView) findViewById(R.id.imageorder);
        dataInfo = (EditText) findViewById(R.id.datainfo);
        nameInfo = (EditText) findViewById(R.id.nameInfo);
        customername = (EditText) findViewById(R.id.custname);
        gallerybutton = (Button) findViewById(R.id.uploadGallery);
        adminIntentButton = (Button) findViewById(R.id.adminIntentButton);
        startcustomdata = (Button) findViewById(R.id.customerdatstart);

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
//        }
//        adminIntentButton.setVisibility(View.INVISIBLE);
        String emailUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString().trim();
        String AdminEmail ="caesaradek@gmail.com";
        if (emailUser.equals(AdminEmail) | emailUser.equals("tobiadeleke@gmail.com") ){
            adminIntentButton.setVisibility(View.VISIBLE);
            startcustomdata.setVisibility(View.VISIBLE);
            customername.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, "THis is the email of the signed in user: " + emailUser + " ese o");
        adminIntentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
        startcustomdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateCustomerName()){
                    Toast.makeText(OrderActivity.this, "one of the fields is incorrect or empty", Toast.LENGTH_LONG).show();
                    return;
                }
                else {

                String customernamestring = customername.getText().toString();
                Intent intent = new Intent(OrderActivity.this, CustomerData.class);
                intent.putExtra(CustomerData.CUSTOMER_NO, customernamestring);
                startActivity(intent);}
            }
        });

//SET THE UPLOAD BUTTON TO NOT SHOW UNTIL A UPLOAD
        uploadAdmin.setVisibility(View.GONE);

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
            cameraImage.setContentDescription(title);//
            uploadCamera.setVisibility(View.GONE);
            gallerybutton.setVisibility(View.GONE);
            startcustomdata.setVisibility(View.GONE);
            customername.setVisibility(View.GONE);
            startPaystack.setVisibility(View.VISIBLE);

            Toast.makeText(OrderActivity.this, "uploading finished", Toast.LENGTH_LONG).show();

            startPaystack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent intent = new Intent(OrderActivity.this, TestPayStack.class);
                   intent.putExtra(TestPayStack.EXTRA_PAY__ORDER_title, title);
                   intent.putExtra(TestPayStack.EXTRA_PAY_ORDER_image, image);
                   startActivity(intent);

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

//    public void justSendToAdmin(String text, String imageData) {
//        final StorageReference filepathEnglish = mystorage.child("Photos").child(String.valueOf(System.currentTimeMillis()));
//        Uri fragmentUri = Uri.parse(text);
//        filepathEnglish.putFile(fragmentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(OrderActivity.this, "uploading complete", Toast.LENGTH_LONG).show();
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(OrderActivity.this, "uploading failed", Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }


//    //the onclick
//    public void uploadGallery(View view){
//            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            //this below containsthe data of the captured image
            final Uri uriCamera = data.getData();
//            Bundle extras = data.getExtras();
//            //get photo info and tyecast to a bitmap
//            Bitmap photo = (Bitmap) extras.get("data");
//            cameraImage.setImageBitmap(photo);
            //this is the file path to store the images thatb are captured
             final StorageReference filepathStorage = mystorage.child("Photos").child(uriCamera.getLastPathSegment());
            //this is for admin upload
            uploadAdmin.setVisibility(View.VISIBLE);
            uploadCamera.setVisibility(View.GONE);
            gallerybutton.setVisibility(View.GONE);
            startcustomdata.setVisibility(View.GONE);
            adminIntentButton.setVisibility(View.GONE);
            Picasso.get().load(uriCamera).into(cameraImage);
            uploadAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!validateForm()){
                        Toast.makeText(OrderActivity.this, "one of the fields is incorrect or empty", Toast.LENGTH_LONG).show();
                        return;
                    }try{



                        filepathStorage.putFile(uriCamera).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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

        }
        else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            final Uri urigallery = data.getData();
            final StorageReference filepathStorage = mystorage.child("Photos").child(urigallery.getLastPathSegment());
            Log.d(TAG, "this is the last path segment "+ urigallery.getLastPathSegment() + " thanks for seeing it");
//            dataInfo.setText("");
//            nameInfo.setText("");
//            final String imageurl = taskSnapshot.getDownloadUrl().toString();
            uploadAdmin.setVisibility(View.VISIBLE);
            uploadCamera.setVisibility(View.GONE);
            gallerybutton.setVisibility(View.GONE);
            startcustomdata.setVisibility(View.GONE);
            adminIntentButton.setVisibility(View.GONE);
            Picasso.get().load(urigallery).into(cameraImage);
            uploadAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!validateForm()){
                        Toast.makeText(OrderActivity.this, "one of the fields is incorrect or empty", Toast.LENGTH_LONG).show();
                        return;
                    }try{

                        showProgress(true);


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
                                    showProgress(false);
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    public boolean validateForm() {
        boolean valid = true;

        String nameemail = nameInfo.getText().toString();
        if (TextUtils.isEmpty(nameemail)){
            valid = false;
            nameInfo.setError("you must put in your email address");}
            else if (nameemail.contains(".")) {
                valid = false;
                nameInfo.setError("not a valid name");}
                else if (nameemail.contains("#")) {
                valid = false;
                nameInfo.setError("not a valid name");
        }
        else if (nameemail.contains("#")) {
            valid = false;
            nameInfo.setError("not a valid name");}
        else if (nameemail.contains("$")) {
            valid = false;
            nameInfo.setError("not a valid name");}
        else if (nameemail.contains("{")) {
            valid = false;
            nameInfo.setError("not a valid name");}
        else if (nameemail.contains("]")) {
            valid = false;
            nameInfo.setError("not a valid name");}
        else{
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
    public boolean validateCustomerName() {
        boolean valid = true;

        String nameemail = customername.getText().toString();
        if (TextUtils.isEmpty(nameemail)){
            valid = false;
            customername.setError("you must put in name");}
        else if (nameemail.contains(".")) {
            valid = false;
            customername.setError("not a valid name");}
        else if (nameemail.contains("#")) {
            valid = false;
            customername.setError("not a valid name");
        }
        else if (nameemail.contains("#")) {
            valid = false;
            customername.setError("not a valid name");}
        else if (nameemail.contains("$")) {
            valid = false;
            customername.setError("not a valid name");}
        else if (nameemail.contains("{")) {
            valid = false;
            customername.setError("not a valid name");}
        else if (nameemail.contains("]")) {
            valid = false;
            customername.setError("not a valid name");}
        else{
            customername.setError(null);
        }



        return valid;
    }


    //receive image from firebase storage and send to firebasedatabase
    public void recieveImageFromStorage(String title, String imageUrl){
        String useremail = nameInfo.getText().toString();
        CoralModelClass coralModelClassInstance = new CoralModelClass(title, imageUrl);
        Log.d(TAG, "this is the title and imageId " + title+ " imageId- " + imageUrl+" thanks eshe o");
        FirebaseDatabase.getInstance().getReference().child(Constants.ADMIN_VIEW).child(useremail).child(String.valueOf(System.currentTimeMillis()))
                .setValue(coralModelClassInstance);
    }
}

