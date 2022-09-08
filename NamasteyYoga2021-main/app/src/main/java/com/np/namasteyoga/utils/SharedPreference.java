package com.np.namasteyoga.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;


/**
 * Created by AbhinavT on 13-10-2017.
 */
public class SharedPreference {

    private Context context;
    private static SharedPreference savePreferenceAndData;

    public static SharedPreference getInstance(Context context) {
        if (savePreferenceAndData == null) {
            savePreferenceAndData = new SharedPreference(context);
        }
        return savePreferenceAndData;
    }

    public SharedPreference(Context context) {
        this.context = context;

    }

//    public void setUser(String key, UserDetail userDetail) {
//        Gson gson = new Gson();
//        String json = gson.toJson(userDetail);
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//
////        prefs.edit().putString(key, Utils.encryptLC(json)).apply();
//        prefs.edit().putString(key, (json)).apply();
//
//    }


    public void setString(String key, String data) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(key, data).apply();
    }

    public String getString(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, null);
    }

    public void setIsProfileModified(String key, boolean status) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(key, status).apply();
    }

    public boolean getIsProfileModified(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, false);
    }

    public void setIsReportDeleted(String key, boolean status) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(key, status).apply();
    }

    public boolean getIsReportDeleted(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, false);
    }

    public void setBoolean(String key, boolean data) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(key, data).apply();
    }

    public boolean getBoolean(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, false);
    }


    public void setInt(String key, int value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt(key, value).apply();
    }

    public int getInt(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(key, 0);
    }

    public void clearData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().clear().apply();
    }

    /*  public void setUser(String key, AdminUser adminUser) {
          Gson gson = new Gson();
          String json = gson.toJson(adminUser);
          SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
          prefs.edit().putString(key, json).apply();

      }

      public void setUserRecordId(String key, int recordId) {
          Gson gson = new Gson();
          String json = gson.toJson(recordId);
          SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
          prefs.edit().putString(key, json).apply();

      }*/


//    public UserDetail getUser(String key) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        String data = prefs.getString(key, null);
//        Gson gson = new Gson();
////        return gson.fromJson(Utils.decrypt(data), UserDetail.class);
//        System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::"+data);
//        return gson.fromJson(data, UserDetail.class);
//    }
}

