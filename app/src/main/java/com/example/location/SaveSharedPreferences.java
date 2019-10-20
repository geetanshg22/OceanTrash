package com.example.location;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreferences {

    static final String PREF_EMAIL = "email";
    static final String PREF_LOCATION = "patiala";
    static final String PREF_LATITUDE = "0";
    static final String PREF_LONGITUDE = "0";
    static final String PREF_DENSITY = "0";
    static final String PREF_BETCON = "NA";
    static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setPrefEmail(Context context, String email) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_EMAIL,email);
        editor.commit();
    }

    public static String getPrefEmail(Context context) {
        return getSharedPreferences(context).getString(PREF_EMAIL,"");
    }

    public static void setPrefLocation(Context context, String location) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_LOCATION,location);
        editor.commit();
    }

    public static String getPrefLocation(Context context) {
        return getSharedPreferences(context).getString(PREF_LOCATION,"");
    }

    public static void setPrefLatitude(Context context, String latitude) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_LATITUDE,latitude);
        editor.commit();
    }

    public static void setPrefLongitude(Context context, String longitude) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_LONGITUDE,longitude);
        editor.commit();
    }

    public static String getPrefLatitude(Context context) {
        return getSharedPreferences(context).getString(PREF_LATITUDE,"");
    }

    public static String getPrefLongitude(Context context) {
        return getSharedPreferences(context).getString(PREF_LONGITUDE,"");
    }

    public static void setPrefCountDensity(Context context, String location) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_DENSITY,location);
        editor.commit();
    }

    public static String getPrefCountDensity(Context context) {
        return getSharedPreferences(context).getString(PREF_DENSITY,"");
    }

    public static void setBetweenCountries(Context context, String email) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_BETCON,email);
        editor.commit();
    }

    public static String getBetweenCountries(Context context) {
        return getSharedPreferences(context).getString(PREF_BETCON,"");
    }
}

