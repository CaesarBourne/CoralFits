package com.caesar.ken.coralfits;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import co.paystack.android.PaystackSdk;

/**
 * Created by e on 6/13/2018.
 */

public class FireCorral extends Application {
    private static boolean isChatActivityOpen = false;



    public static boolean isChatActivityOpen(){

        return isChatActivityOpen;
    }

    public static void setChatActivityOpen(boolean tisChatActivityOpen){
        FireCorral.isChatActivityOpen = tisChatActivityOpen;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        if (FirebaseApp.getApps(this).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        PaystackSdk.initialize(getApplicationContext());
    }
}
