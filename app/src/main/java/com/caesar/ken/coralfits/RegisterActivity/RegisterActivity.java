package com.caesar.ken.coralfits.RegisterActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.caesar.ken.coralfits.FirstIntroduction;
import com.caesar.ken.coralfits.Login.LoginActivity;
import com.caesar.ken.coralfits.MainActivity;
import com.caesar.ken.coralfits.Models.User;
import com.caesar.ken.coralfits.R;
import com.caesar.ken.coralfits.Utilitites.Constants;
import com.caesar.ken.coralfits.Utilitites.SharedPreferencesUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private static final String TAG = "RegisterActivity";
    NotificationCompat.Builder notification;
    private static final int uniqueId = 646847;


    public static void startActivity(Context context){
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        Toolbar loginToolbar = (Toolbar) findViewById(R.id.logintoolbar);
        loginToolbar.setTitle("Corral Fits");
        setSupportActionBar(loginToolbar);
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        Button registerButton = (Button) findViewById(R.id.registeraton_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginFormView = findViewById(R.id.login_formReg);
        mProgressView = findViewById(R.id.login_progressReg);
    }
    private void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one., if password is not empty but not validi.e someting is in password but not long enough
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mEmailView.getText().toString();
            registerMainCore(this, email, password);
        }
    }
    // this is whwere the main registratio happens
    public void registerMainCore(Activity activity, String argemail, String argPassword){
        Log.d(TAG, "This is the email: "+ argemail+ " and password: " +argPassword+ " thanks");
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(argemail, argPassword)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            registrationSuccess(task.getResult().getUser());
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "you did not register succesfully try again later", Toast.LENGTH_LONG).show();
                            showProgress(false);
                        }

                    }
                });
    }
    public void registrationSuccess(FirebaseUser firebaseUser){
        showProgress(false);
        Toast.makeText(this, "Registration is succesful", Toast.LENGTH_LONG).show();
        addUsersManinCore(getApplicationContext(), firebaseUser);

    }
// adding the users to firebase
    public void addUsersManinCore(Context context, FirebaseUser firebaseUser){
        String email = firebaseUser.getEmail();
        String userId = firebaseUser.getUid();

        String firebaseToken = new SharedPreferencesUtility(context).getString(Constants.ARGUMENT_FIREBASE_TOKEN);


        User user = new User(email, userId,firebaseToken );
        FirebaseDatabase.getInstance().getReference().child(Constants.ARGUMENT_USERS).child(firebaseUser.getUid()).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onAddUserSuccess("youn have been added as a user of Corral fits app");
                    }
                });
    }
    public void onAddUserSuccess(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        notification.setTicker("Order for the best outfits is vogue");
        notification.setContentTitle("Corral Fits");
        notification.setContentText("Welcome to Corral fits ");
        notification.setWhen(System.currentTimeMillis());
        notification.setSmallIcon(R.drawable.corrallogo);
        notification.setLights(Color.BLUE, 500, 500);
        long[] pattern ={500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500};
        notification.setVibrate(pattern);
        notification.setStyle(new NotificationCompat.InboxStyle());
        Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification.setSound(alarmsound);
        notification.setContentIntent(pendingIntent);

        NotificationManager notmanage = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notmanage.notify(uniqueId, notification.build());
        FirstIntroduction.startFirstIntroductionActivity(getApplicationContext(), Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

}
