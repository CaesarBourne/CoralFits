package com.caesar.ken.coralfits.FirebaseInstanceID;

import android.util.Log;

import com.caesar.ken.coralfits.Utilitites.Constants;
import com.caesar.ken.coralfits.Utilitites.SharedPreferencesUtility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class CorralFirebaseInstanceID extends FirebaseInstanceIdService {


    private static final String TAG = "CorralInstanceID";

    @Override
    public void onTokenRefresh() {

       String token =  FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Oga this is the token for this user "+ token +" hpe youre satisfied");
       sendTokenToDatabaseandSharedPref(token);
    }


    public void sendTokenToDatabaseandSharedPref(String token){

        SharedPreferencesUtility sharedPreferencesUtility = new SharedPreferencesUtility(getApplicationContext());

        sharedPreferencesUtility.saveString(Constants.ARGUMENT_FIREBASE_TOKEN, token);
        // save token to database only when user is logged in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            FirebaseDatabase.getInstance().getReference().child(Constants.ARGUMENT_USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Constants.ARGUMENT_FIREBASE_TOKEN).setValue(token);
        }
    }
}
