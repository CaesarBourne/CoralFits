package com.caesar.ken.coralfits;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.caesar.ken.coralfits.CorralPayment.PaymentStack;
import com.caesar.ken.coralfits.CorralPayment.TestPayStack;
import com.caesar.ken.coralfits.Login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Toolbar tabToolbar;
    ShareActionProvider shareActionProvider;
    public TextView customerstatus;
    public static final String EXTRA_PROCESSING_STATUS = "processing";

    public static void startMainActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
    public static void startMainActivity(Context context, int flags){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        FireCorral.setChatActivityOpen(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FireCorral.setChatActivityOpen(false);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        tabToolbar = (Toolbar) findViewById(R.id.mytabtoolbar);
        setSupportActionBar(tabToolbar);
        SectionsPageAdapter sectionAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpage);
        TabLayout myTablayout = (TabLayout) findViewById(R.id.myTablayout);
        myTablayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(sectionAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });
        fab.setContentDescription("Make an order");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu) ;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.createorder:
                Intent intent = new Intent(this, TestPayStack.class);
                startActivity(intent);
                return true;
            case R.id.logoutUser:
                startAlertDialogLogout();
                return true;
            default
                    :return super.onOptionsItemSelected(item);
        }

    }

    public void startAlertDialogLogout(){
        new AlertDialog.Builder(this).setTitle("Logout")
                .setMessage("Are you sure you want to Logout")
                .setIcon(R.drawable.logout_warning)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        logoutUser();
                  LoginActivity.startActivityInstance(getApplicationContext(), Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    public void logoutUser(){

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseAuth.getInstance().signOut();
        }
    }

    public class SectionsPageAdapter extends FragmentPagerAdapter {
        public SectionsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    if (getIntent().getExtras() != null){


                    Bundle bundle = new Bundle();
                    bundle.putString(HomeFragment.EXTRA_PROCESSING_STATUS_FRAGMENT, EXTRA_PROCESSING_STATUS);
                    Fragment homefragment = new HomeFragment();
                    homefragment.setArguments(bundle);
                    return homefragment;}
                    else {
                        return new HomeFragment();
                    }

                case 1:
                    return new EnglishWearsFragment();

                case 2:
                    return new NativeWearsFragment();

                case 3:
                    return new CustomMadeClothFragment();

            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return getResources().getString(R.string.Home);
                case 1:
                    return getResources().getString(R.string.Englishwears);
                case 2:
                    return getResources().getString(R.string.CustomMadeCloth);
                case 3:
                    return getResources().getString(R.string.NativeWears);
            }
            return null;
        }
    }
}
