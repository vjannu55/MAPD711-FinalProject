package com.codescafe.doctorappointment.utils;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.codescafe.doctorappointment.models.UserModel;
import com.google.gson.Gson;


public class UserManager {

    public static SharedPreferences mPrefs;
    public static SharedPreferences.Editor mEditor;

    public static String login = "login";
    public static String user = "users";




    public UserManager(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
    }


    public static void setBooleanData(String key, Boolean val,Context activity) {
        mPrefs = activity.getSharedPreferences("userDetails",MODE_PRIVATE);
        mEditor = mPrefs.edit();
        mEditor.putBoolean(key, val);
        mEditor.commit();
    }

    public static boolean getBooleanData(String key,Context activity) {
        mPrefs = activity.getSharedPreferences("userDetails",MODE_PRIVATE);
        return mPrefs.getBoolean(key, false);
    }


    //user
    public static void setUserDetails(UserModel val, Activity activity) {
        mPrefs = activity.getSharedPreferences("userDetails",MODE_PRIVATE);
        mEditor = mPrefs.edit();
        mEditor.putString(user, new Gson().toJson(val));
        mEditor.commit();
    }

    public static UserModel getUserDetails(Context activity) {
        return new Gson().fromJson(activity.getSharedPreferences("userDetails",Context.MODE_PRIVATE).getString(user, ""), UserModel.class);
    }

    public static void logoutUser() {
        mEditor = mPrefs.edit();
        mEditor.clear();
        mEditor.commit();
    }
}
