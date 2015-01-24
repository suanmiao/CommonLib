package com.suan.example.util.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.suan.example.component.SApplication;

/**
 * Created by suanmiao on 14-10-31.
 */
public class LocalConfig {

    private static final int SHAREDPREF_OPEN_MODE = Context.MODE_MULTI_PROCESS;
    /**
     * app state ,include:
     * 1.is first launch?
     * 2.settings in setting page;
     */
    private static final String SHAREDPREF_APP_STATE = "app_state";

    private static final String KEY_FIRST_LAUNCH = "first_launch";

    /**
     * user config, include:
     * 1.user token
     * 2.personalized setting for user;
     */
    private static final String SHAREDPREF_USER_CONFIG = "user_config";

    private static final String KEY_USER_ACCOUNT = "user_account";
    private static final String KEY_FONT_SIZE = "font_size";
    private static final String KEY_PUSH_ON = "push_on";
    private static final String KEY_TRAFFIC_SAVE_MODE = "traffic_save_mode";


    private static SharedPreferences getAppStateSharedpref() {
        return SApplication.getAppContext().getSharedPreferences(SHAREDPREF_APP_STATE,
                SHAREDPREF_OPEN_MODE);
    }

    private static SharedPreferences getUserConfigSharedpref() {
        return SApplication.getAppContext().getSharedPreferences(SHAREDPREF_USER_CONFIG,
                SHAREDPREF_OPEN_MODE);
    }

    public static boolean setFirstLaunch(boolean firstLaunch) {
        SharedPreferences.Editor editor = getAppStateSharedpref().edit();
        editor.putBoolean(KEY_FIRST_LAUNCH, firstLaunch);
        return editor.commit();
    }

    public static String getUserAccountString() {
        SharedPreferences userConfig = getUserConfigSharedpref();
        return userConfig.getString(KEY_USER_ACCOUNT, "");
    }

    public static boolean putUserAccountString(String content){
        SharedPreferences.Editor editor = getUserConfigSharedpref().edit();
        editor.putString(KEY_USER_ACCOUNT,content);
        return editor.commit();
    }

    public static int getFontSize() {
        SharedPreferences userConfig = getUserConfigSharedpref();
        return userConfig.getInt(KEY_FONT_SIZE , 14);
    }

    public static boolean putFontSize(int fontSize){
        SharedPreferences.Editor editor = getUserConfigSharedpref().edit();
        editor.putInt(KEY_FONT_SIZE,fontSize);
        return editor.commit();
    }

    public static boolean getPushOn() {
        SharedPreferences userConfig = getUserConfigSharedpref();
        return userConfig.getBoolean(KEY_PUSH_ON , false);
    }

    public static boolean putPushOn(boolean switchOn){
        SharedPreferences.Editor editor = getUserConfigSharedpref().edit();
        editor.putBoolean(KEY_PUSH_ON,switchOn);
        return editor.commit();
    }

    public static boolean getTrafficSaveMode() {
        SharedPreferences userConfig = getUserConfigSharedpref();
        return userConfig.getBoolean(KEY_TRAFFIC_SAVE_MODE , false);
    }

    public static boolean putTrafficSaveMode(boolean switchOn){
        SharedPreferences.Editor editor = getUserConfigSharedpref().edit();
        editor.putBoolean(KEY_TRAFFIC_SAVE_MODE,switchOn);
        return editor.commit();
    }

}
