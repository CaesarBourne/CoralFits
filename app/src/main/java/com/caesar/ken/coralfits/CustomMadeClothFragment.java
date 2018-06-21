package com.caesar.ken.coralfits;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomMadeClothFragment extends ListFragment {


    public CustomMadeClothFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayAdapter<String> eatAdapt = new ArrayAdapter<>(inflater.getContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Eatery));
        setListAdapter(eatAdapt);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
