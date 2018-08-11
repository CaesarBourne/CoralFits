package com.caesar.ken.coralfits;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EnglishRecyclerAdapter extends RecyclerView.Adapter<EnglishRecyclerAdapter.EViewHolder>{


    public  String imageData[];
    private static final String TAG = "EnglishRecyclerAdapter";
    public String textData[];
    public TextView myText;
    public ImageView myImage;
    public CardClickedListener cardClickedListenerChild;


    List<CoralModelClass> insisdeList;
    public EnglishRecyclerAdapter(List<CoralModelClass> myList){
       this.insisdeList = myList;
    }
    public interface CardClickedListener{
        void onCardClicked(int position);
    }

    public static class EViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;

        public EViewHolder( CardView v) {
            super(v);
            cardView = v;
        }
    }
    //this is like a constructor to set the context of the activity using the listener to call the listeners method the interfac listener now has a context
    public void setCardClickedListener(CardClickedListener cardClickedListenerChild){
        this.cardClickedListenerChild = cardClickedListenerChild;
    }
    public void add(CoralModelClass coralModelClass){
        insisdeList.add(coralModelClass);
        notifyItemInserted(insisdeList.size()-1);
    }
    @Override
    public EnglishRecyclerAdapter.EViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_english_adapter_layout, parent, false);
        return new EViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(EnglishRecyclerAdapter.EViewHolder holder, final int position) {
        CardView myCard = holder.cardView;
        int cardViewPosition = holder.getAdapterPosition();
         myText = (TextView) myCard.findViewById(R.id.info_text);
         myImage = (ImageView) myCard.findViewById(R.id.info_image2);
         CoralModelClass initializedcoralModelClass = insisdeList.get(position);
          Log.d(TAG, "abegi work na : "+ insisdeList.toString() + " okay hope ure correct");
         String imageId = initializedcoralModelClass.getImageId();
         String textId = initializedcoralModelClass.getTitle();
        Picasso.get().load(imageId).into(myImage);
        myText.setText(textId);
        myCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardClickedListenerChild != null){
                    cardClickedListenerChild.onCardClicked(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (insisdeList != null){
            return insisdeList.size();
        }
        return 0;
    }
}
