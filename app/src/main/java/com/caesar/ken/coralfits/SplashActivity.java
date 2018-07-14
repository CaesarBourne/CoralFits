package com.caesar.ken.coralfits;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.caesar.ken.coralfits.Login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private Runnable runnable;
    private Handler myHandler;
    private static final int SPLASH_TIME_MS =2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myHandler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    MainActivity.startMainActivity(SplashActivity.this);
                }
                else {
                    LoginActivity.startActivityInstance(SplashActivity.this);
                }
                finish();
            }
        };
        myHandler.postDelayed(runnable, SPLASH_TIME_MS);
    }
}
