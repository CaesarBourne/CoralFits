package com.caesar.ken.coralfits.Utilitites;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

public class SharedPreferencesUtility  {
    private Context context;
    private static final String APP_PREFERENCES = "app prefrence";
    private SharedPreferences mySharePreference;
    private SharedPreferences.Editor sharedPrefEdit;

    public SharedPreferencesUtility(Context inContext) {
        this.context = inContext;
    }

    //contextmust always cal the shared preferences method
    public void saveString( String key, String value){
        mySharePreference = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        sharedPrefEdit = mySharePreference.edit();
        sharedPrefEdit.putString(key, value);
        sharedPrefEdit.commit();
    }
    public String getString( String key){
        //the context must alaways call the get sharedpreferences method
        mySharePreference = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        String myVal = mySharePreference.getString(key, null);
        return myVal;
    }
}
