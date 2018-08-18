package com.caesar.ken.coralfits;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.caesar.ken.coralfits.Utilitites.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomMadeClothFragment extends Fragment {
    private static final String TAG = "NativeWearsFragment";
    private RecyclerView recyclerView;
    EnglishRecyclerAdapter myEnglishRecyclerAdapter;
    List<CoralModelClass> orderList;

    public CustomMadeClothFragment() {
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
        if (savedInstanceState != null) {
            layoutId = savedInstanceState.getInt(LAYOUT_STATE, R.layout.englishwearslayout);
        }
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(layoutId, container, false);
        recyclerView = (RecyclerView) linearLayout.findViewById(R.id.englishrecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        return linearLayout;
    }




    public void collectData(List<CoralModelClass> data) {
        Log.d(TAG, "this is the image url: " + data + "na d data be this ");
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

                Log.d(TAG, "Please hekp me chechk this id: " + imageClicked + " and the title " + titleClicked + " thanks");
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
    public void onStart() {
        super.onStart();

        DatabaseReference mydatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child(Constants.CHILDREN_WEARS);
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

            }
        });

    }
}
