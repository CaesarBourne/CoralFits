package com.caesar.ken.coralfits;

import android.content.Context;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class EnglishWearsFragment extends Fragment {
    DatabaseReference englishdatabasereference;
    private static final String TAG = "EnglishWearsFragment";
    private RecyclerView recyclerView;
    public EnglishWearsFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.englishwearslayout, container, false);
        recyclerView = (RecyclerView) linearLayout.findViewById(R.id.englishrecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        englishdatabasereference = FirebaseDatabase.getInstance().getReference("EnglishWears");

        return linearLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<CoralModelClass, EnglishViewHolder> englishRecyclerAdapter =
                new FirebaseRecyclerAdapter<CoralModelClass, EnglishViewHolder>(CoralModelClass.class, R.layout.cardview_english_adapter_layout,
                        EnglishViewHolder.class, englishdatabasereference) {
                    @Override
                    protected void populateViewHolder(EnglishViewHolder viewHolder, CoralModelClass model, int position) {
                        Log.d(TAG,"this is the image url: " + model.getImageId());
                        Toast.makeText(getActivity(),"sho am: "+ model.getImageId(),Toast.LENGTH_LONG).show();
                        viewHolder.setImage(getContext(), model.getImageId() );
                        viewHolder.setText(model.getTitle());
                    }
                };
                recyclerView.setAdapter(englishRecyclerAdapter);
    }
    public static class EnglishViewHolder extends RecyclerView.ViewHolder{
        private CardView cv;
        View myView;
        public EnglishViewHolder( View cv) {
            super(cv);
            myView = cv;
        }

            public void setText(String title){
                TextView muText = (TextView)cv.findViewById(R.id.info_text);
                muText.setText(title);
        }
        public void setImage(Context appcontetxt, String image){
            ImageView imageView = (ImageView) cv.findViewById(R.id.info_image);
            Log.d(TAG,"this is the image url: " + image);

            Picasso.get().load("https://en.wikipedia.org/wiki/Nigeria#/media/File:Nok_sculpture_Louvre_70-1998-11-1.jpg").into(imageView);
        }

        }
    }

