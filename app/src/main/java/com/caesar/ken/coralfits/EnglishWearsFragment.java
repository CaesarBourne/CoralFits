package com.caesar.ken.coralfits;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.caesar.ken.coralfits.Utilitites.Constants;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class EnglishWearsFragment extends Fragment {
    DatabaseReference englishdatabasereference;
    private static final String TAG = "EnglishWearsFragment";
    private RecyclerView recyclerView;
    EnglishRecyclerAdapter myEnglishRecyclerAdapter;
    List<CoralModelClass> orderList;
    public EnglishWearsFragment() {
        // Required empty public constructor
    }


    int layoutId = R.layout.englishwearslayout;
    public static final String LAYOUT_STATE = "layoutstate";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(LAYOUT_STATE, layoutId);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null){
            layoutId = savedInstanceState.getInt(LAYOUT_STATE, R.layout.englishwearslayout);
        }
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(layoutId, container, false);
        recyclerView = (RecyclerView) linearLayout.findViewById(R.id.englishrecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

                return linearLayout;
    }


//    englishdatabasereference = FirebaseDatabase.getInstance().getReference("EnglishWears").child("Post1");
//        FirebaseRecyclerAdapter<CoralModelClass, EnglishViewHolder> englishRecyclerAdapter =
//                new FirebaseRecyclerAdapter<CoralModelClass, EnglishViewHolder>(CoralModelClass.class, R.layout.cardview_english_adapter_layout,
//                        EnglishViewHolder.class, englishdatabasereference) {
//                    @Override
//                    protected void populateViewHolder(EnglishViewHolder viewHolder, CoralModelClass model, int position) {
//                        Log.d(TAG,"this is the image url: " + model.getImageId()+" work na ");
//                        Toast.makeText(getActivity(),"sho am: "+ model.getImageId(),Toast.LENGTH_LONG).show();
//                        viewHolder.setImage(getContext(), model.getImageId() );
//                        viewHolder.setText(model.getTitle());
//                    }
//                };
//        recyclerView.setAdapter(englishRecyclerAdapter);


//    public static class EnglishViewHolder extends RecyclerView.ViewHolder{
//        private CardView cv;
//        View myView;
//        public EnglishViewHolder( View cv) {
//            super(cv);
//            myView = cv;
//        }
//
//            public void setText(String title){
//                TextView muText = (TextView) myView.findViewById(R.id.info_text);
//                muText.setText(title);
//        }
//        public void setImage(Context appcontetxt, String image){
//            ImageView imageView = (ImageView) myView.findViewById(R.id.info_image2);
//            Log.d(TAG,"this is the image url: " + image);
//
//            Picasso.get().load(image).into(imageView);
//        }
//
//        }



    public void collectData(List<CoralModelClass> data){
        Log.d(TAG,"this is the image url: " + data +"na d data be this ");
        myEnglishRecyclerAdapter = new EnglishRecyclerAdapter(data);
        recyclerView.setAdapter(myEnglishRecyclerAdapter);

        myEnglishRecyclerAdapter.notifyDataSetChanged();
        this.orderList = data;
        EnglishRecyclerAdapter.CardClickedListener cardViewClicked = new EnglishRecyclerAdapter.CardClickedListener() {
            @Override
            public void onCardClicked(int position) {
                Intent intent = new Intent(getContext(), OrderActivity.class);

                String imageClicked = orderList.get(position).getImageId();
                String titleClicked = orderList.get(position).getTitle();

                Log.d(TAG, "Please hekp me chechk this id: "+ imageClicked+ " and the title "+ titleClicked +" thanks");
                intent.putExtra(OrderActivity.EXTRA_CARD_VIEW_image, imageClicked);
                intent.putExtra(OrderActivity.EXTRA_CARD_VIEW_title, titleClicked);
                getContext().startActivity(intent);
            }
        };
        //this first sets the listener so when the card is clicked it fetches position from the adapter
        myEnglishRecyclerAdapter.setCardClickedListener(cardViewClicked);
//        Bundle arguments = new Bundle();
//        arguments.putSerializable(OrderActivity.EXTRA_CARD_VIEW_ARRAYLIST, (Serializable)orderList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference mydatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child(Constants.ENGLISH_WEARS);
        mydatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotIterator = dataSnapshot.getChildren().iterator();
                List<CoralModelClass> dataList = new ArrayList();
                Log.d(TAG,"this is the image url: " + dataSnapshot +" work na ");
                while (dataSnapshotIterator.hasNext()){
                    CoralModelClass imagetextData = dataSnapshotIterator.next().getValue(CoralModelClass.class);
                    Log.d(TAG,"the corral model data should be this: " + imagetextData +" work na ");
                    //add the image and textdata to the array list then...
                    dataList.add(imagetextData);
                    //add it to the recycler adapter
                    collectData(dataList);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
//FirebaseDatabase.getInstance().getReference("EnglishWears").child("Post1")
//        .addChildEventListener(new ChildEventListener() {
//@Override
//public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//        CoralModelClass coralModelClass = dataSnapshot.getValue(CoralModelClass.class);
//
//        myAdapter = new EnglishRecyclerAdapter(new ArrayList<CoralModelClass>());
//        recyclerView.setAdapter(myAdapter);
//        myAdapter.addData(coralModelClass);
//        }
//
//@Override
//public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//        }
//
//@Override
//public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//        }
//
//@Override
//public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//        }
//
//@Override
//public void onCancelled(DatabaseError databaseError) {
//
//        }
//        });
//
