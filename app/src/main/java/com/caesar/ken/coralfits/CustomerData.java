package com.caesar.ken.coralfits;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.caesar.ken.coralfits.Utilitites.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomerData extends AppCompatActivity {

    private static final String TAG = "CustomerData";
    public static final String CUSTOMER_NO = "customerno";
    RecyclerView recyclerView;
    EnglishRecyclerAdapter myEnglishRecyclerAdapter;
    List<CoralModelClass> orderList;
    EditText custemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_data);
        recyclerView = (RecyclerView) findViewById(R.id.customerrecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


    }

    @Override
    public void onStart() {
        super.onStart();

        if (getIntent().getExtras() != null) {

            final String namedatabase = getIntent().getExtras().getString(CUSTOMER_NO);

            DatabaseReference mydatabaseRef = FirebaseDatabase.getInstance().getReference()
                    .child(Constants.ADMIN_VIEW).child(namedatabase).child(String.valueOf(System.currentTimeMillis()));
            mydatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> dataSnapshotIterator = dataSnapshot.getChildren().iterator();
                    List<CoralModelClass> dataList = new ArrayList();
                    Log.d(TAG, "this is the image url: " + dataSnapshot + " work na ");
                    while (dataSnapshotIterator.hasNext()) {
                        CoralModelClass imagetextData = dataSnapshotIterator.next().getValue(CoralModelClass.class);
                        Log.d(TAG, "the corral model data should be this: " + imagetextData + " work na ");
                        //add the image and textdata to the array list then...
                        dataList.add(imagetextData);
                        //add it to the recycler adapter
                        collectData(dataList);


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(CustomerData.this, "oga "+ namedatabase+ " has not sent any data to you", Toast.LENGTH_LONG).show();

                }
            });
        }
    }
    public void collectData(List<CoralModelClass> data){
        Log.d(TAG,"this is the image url: " + data +"na d data be this ");
        myEnglishRecyclerAdapter = new EnglishRecyclerAdapter(data);
        recyclerView.setAdapter(myEnglishRecyclerAdapter);

        myEnglishRecyclerAdapter.notifyDataSetChanged();


    }
}
