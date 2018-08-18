package com.caesar.ken.coralfits;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.caesar.ken.coralfits.CorralPayment.TestPayStack;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {



    AnimationDrawable animationDrawable;
    TextView customerstatus;
    TestPayStack testPayStackchild;
    private static final String PAYMENT_STATUS = "pay";
    String paymentstatus, paymentmessage;
    public static final String EXTRA_PROCESSING_STATUS_FRAGMENT = "processing";
    public HomeFragment() {
        // Required empty public constructor
    }



    public static void startHomefragment (Context context){
         Intent intent = new Intent(context, HomeFragment.class);
         context.startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.homeimage);
        imageView.setBackgroundResource(R.drawable.animatedimages);

        customerstatus = (TextView) view.findViewById(R.id.customerstatus);
        animationDrawable = (AnimationDrawable) imageView.getBackground();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationDrawable.start();
            }
        });

        TestPayStack.StatusListener mystatus = new TestPayStack.StatusListener() {
            @Override
            public void status(String pay, String message) {
                paymentstatus =pay;
                paymentmessage = message;
            }
        };

        Bundle bundle = getArguments();
        if (bundle != null){

            customerstatus.setVisibility(View.VISIBLE);
            String customerMessage = bundle.getString(EXTRA_PROCESSING_STATUS_FRAGMENT);
            customerstatus.setText(customerMessage);
        }

//        if (paymentstatus!= null){
//            testPayStackchild.setStatusListener(mystatus);
//            statustext.setText(paymentstatus);
//        }

        return view ;

    }



}
