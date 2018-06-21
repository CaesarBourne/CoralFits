package com.caesar.ken.coralfits;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

public class MainActivity extends AppCompatActivity {

    Toolbar tabToolbar;
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

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.createorder:
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                return true;
            default
                    :return super.onOptionsItemSelected(item);
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
                    return new HomeFragment();

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
