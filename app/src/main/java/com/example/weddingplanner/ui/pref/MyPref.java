package com.example.weddingplanner.ui.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.weddingplanner.appBase.MyApp;
import com.example.weddingplanner.ui.model.LoginData;
import com.example.weddingplanner.ui.model.WeddingFormData;
import com.example.weddingplanner.ui.param.Params;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MyPref {
    public static String ACTIVE_WEDDING_ID = "ACTIVE_WEDDING_ID";
    public static String DATE_FORMATE = "DateFormate";
    public static String IS_TERMS_ACCEPTED = "IS_TERMS_ACCEPTED";
    public static String SEND_TOKEN_TO_SERVER = "sendTokenToServer";
    public static final String SHAREDPREF = "MyPref";
    public static String SHOW_FIRST_RATE_US = "SHOW_FIRST_RATE_US";
    public static String SHOW_NEVER_RATE_US = "SHOW_NEVER_RATE_US";
    public static String SHOW_RATE_US = "SHOW_RATE_US";
    public static String SUBSCRIPTIONDATA = "SUBSCRIPTIONDATA";

    public static void savePreference(String str, String str2) {
        SharedPreferences.Editor edit = MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).edit();
        edit.putString(str2, str);
        edit.apply();
    }

    public static String getPreference(String str) {
        return MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).getString(str, "");
    }

    public static void clearPreference(String str) {
        SharedPreferences.Editor edit = MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).edit();
        edit.remove(str);
        edit.apply();
    }

    public static void setIsTermsAccept(boolean z) {
        SharedPreferences.Editor edit = MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).edit();
        edit.putBoolean(IS_TERMS_ACCEPTED, z);
        edit.apply();
    }

    public static Boolean getIsTermsAccept() {
        return MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).getBoolean(IS_TERMS_ACCEPTED, false);
    }

    public static void setShowNeverRateUs(boolean z) {
        SharedPreferences.Editor edit = MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).edit();
        edit.putBoolean(SHOW_NEVER_RATE_US, z);
        edit.apply();
    }

    public static Boolean getShowNeverRateUs() {
        return MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).getBoolean(SHOW_NEVER_RATE_US, false);
    }

    public static void setShowFirstRateUs(boolean z) {
        SharedPreferences.Editor edit = MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).edit();
        edit.putBoolean(SHOW_FIRST_RATE_US, z);
        edit.apply();
    }

    public static Boolean getShowFirstRateUs() {
        return MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).getBoolean(SHOW_FIRST_RATE_US, false);
    }

    public static void setShowRateUs(boolean z) {
        SharedPreferences.Editor edit = MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).edit();
        edit.putBoolean(SHOW_RATE_US, z);
        edit.apply();
    }

    public static Boolean getShowRateUs() {
        return MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).getBoolean(SHOW_RATE_US, false);
    }

    public static void setSelectedFormat(String str) {
        SharedPreferences.Editor edit = MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).edit();
        edit.putString(DATE_FORMATE, str);
        edit.apply();
    }

    public static String getSelectedFormat() {
        return MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).getString(DATE_FORMATE, "dd/MM/yyyy");
    }



    public static void setSendTokenToServer(Context context, Boolean bool) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).edit();
        edit.putBoolean(SEND_TOKEN_TO_SERVER, bool.booleanValue());
        edit.apply();
    }

    public static Boolean getSendTokenToServer(Context context) {
        return context.getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).getBoolean(SEND_TOKEN_TO_SERVER, false);
    }

    public static void setActiveWeddingId(String str) {
        SharedPreferences.Editor edit = MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).edit();
        edit.putString(ACTIVE_WEDDING_ID, str);
        edit.apply();
    }

    public static String getActiveWeddingId() {
        return MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).getString(ACTIVE_WEDDING_ID, "");
    }

    public static void saveCurrencyPreference(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).edit();
        edit.putString(str2, str);
        edit.apply();
    }

    public static String getCurrencyPreference(Context context, String str) {
        return context.getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).getString(str, "$");
    }

    public static void setSoundPreference(Boolean bool) {
        SharedPreferences.Editor edit = MyApp.getInstance().getSharedPreferences(SHAREDPREF, 0).edit();
        edit.putBoolean(Params.ISPLAYING, bool.booleanValue());
        edit.apply();
    }

    public static Boolean getSoundPreference() {
        return MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).getBoolean(Params.ISPLAYING, true);
    }



    public static WeddingFormData getWeddingData() {
        String string = MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).getString(Params.WEDDINGDATA, null);
        if (string != null) {
            return (WeddingFormData) new Gson().fromJson(string, new TypeToken<WeddingFormData>() {
                /* class com.selfmentor.myweddingplanner.Comman.MyPref.AnonymousClass3 */
            }.getType());
        }
        return null;
    }

    public static void setWeddingData(WeddingFormData weddingFormData) {
        String json = new Gson().toJson(weddingFormData);
        SharedPreferences.Editor edit = MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).edit();
        edit.putString(Params.WEDDINGDATA, json);
        edit.apply();
    }
    public static void setLoginData(LoginData loginData) {
        String json = new Gson().toJson(loginData);
        SharedPreferences.Editor edit = MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).edit();
        edit.putString(Params.LOGIN, json);
        edit.apply();
    }
    public static void clearWeddingPreference() {
        SharedPreferences.Editor edit = MyApp.getInstance().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE).edit();
        edit.remove(Params.WEDDINGDATA);
        edit.apply();
    }
}
