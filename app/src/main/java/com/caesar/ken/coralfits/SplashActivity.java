package com.caesar.ken.coralfits;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.caesar.ken.coralfits.Login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

public class SplashActivity extends AppCompatActivity {

    private Runnable runnable;
    private Handler myHandler;
    private static final int SPLASH_TIME_MS =2000;
    private static final String TAG = "splash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseInstanceId.getInstance().getToken() != null )
        {
            String token = FirebaseInstanceId.getInstance().getToken().toString();
            Log.d(TAG, "This is the token for this user: "+ token+ " thanks for seen it" );
            Toast.makeText(this,"the token is given thus: " + token+" Oti pari", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Ogaade there is no token avalable", Toast.LENGTH_LONG).show();
            Log.d(TAG, "This BAD BOY IS EMPTY "+ FirebaseInstanceId.getInstance().getToken() +" NO THANKS ITS NOT THERE" );
        }
        myHandler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    MainActivity.startMainActivity(SplashActivity.this, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                else {
                    LoginActivity.startActivityInstance(SplashActivity.this);
                }
                finish();
            }
        };
        myHandler.postDelayed(runnable, SPLASH_TIME_MS);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String token = FirebaseInstanceId.getInstance().getToken().toString();
        Log.d(TAG, "This is the token for this user: "+ token+ " thanks for seen it" );
        Toast.makeText(this,"the token may not be given " + token +" ko pari y na", Toast.LENGTH_LONG).show();
    }
}
