package com.iotsmartaliv.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.iotsmartaliv.constants.Constant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is used for shared-preference.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2016-09-11
 */
public class SharePreference {
    private static SharedPreferences mPref;
    private static SharePreference mRef;
    private SharedPreferences.Editor mEditor;

    /**
     * Singleton method return the instance
     **/
    public static SharePreference getInstance(Context context) {
        if (mRef == null) {
            mRef = new SharePreference();
            mPref = context.getApplicationContext().getSharedPreferences(
                    "MyPref", 0);
            return mRef;
        }
        return mRef;
    }

    /**
     * Put long value into sharedpreference
     **/
    public void putLong(String key, long value) {
        try {
            mEditor = mPref.edit();
            mEditor.putLong(key, value);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get long value from sharedpreference
     **/
    public long getLong(String key) {
        try {
            long lvalue;
            lvalue = mPref.getLong(key, 0);
            return lvalue;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Put int value into sharedpreference
     **/
    public void putInt(String key, int value) {
        try {
            mEditor = mPref.edit();
            mEditor.putInt(key, value);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get int value from sharedpreference
     **/
    public int getIntForShake(String key) {
        try {
            int lvalue;
            lvalue = mPref.getInt(key, 25);
            return lvalue;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Put String value into sharedpreference
     **/

    public void putString(String key, String value) {
        try {
            mEditor = mPref.edit();
            mEditor.putString(key, value);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get String value from sharedpreference
     **/
    public String getString(String key) {
        try {
            String lvalue;
            lvalue = mPref.getString(key, "");
            return lvalue;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void delete(String key) {
        try {
            mEditor = mPref.edit();
            mEditor.remove(key);
            mEditor.commit(); // Consider using apply() for better performance
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void setIp(String ip){
//        try{
//            mEditor = mPref.edit();
//            mEditor.putString("VoIp",ip);
//            mEditor.commit();
//        }catch (Exception e){
//
//        }
//    }
//    public void setport(String port){
//        try{
//            mEditor = mPref.edit();
//            mEditor.putString("VoPort",port);
//            mEditor.commit();
//        }catch (Exception e){
//
//        }
//    }
    /**
     * Put String value into sharedpreference
     **/
    public void putBoolean(String key, Boolean value) {
        try {
            mEditor = mPref.edit();
            mEditor.putBoolean(key, value);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get String value from sharedpreference
     **/
    public Boolean getBoolean(String key) {
        try {
            Boolean lvalue;
            lvalue = mPref.getBoolean(key, false);
            return lvalue;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method is used for clear preference.
     */
    public void clearPref() {
        mPref.edit().clear().apply();
    }

    public void putFeatureForApp(List<String> featureList){
        try {
            Set<String> set = new HashSet<>(featureList);
            mEditor = mPref.edit();
            mEditor.putStringSet(Constant.APP_FEATURE, set);
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<String> getFeatureForApp() {

        // Retrieve Set<String> from SharedPreferences
        Set<String> set = mPref.getStringSet(Constant.APP_FEATURE, new HashSet<>());
        return new ArrayList<>(set);  // Convert Set<String> back to List<String>
    }
}