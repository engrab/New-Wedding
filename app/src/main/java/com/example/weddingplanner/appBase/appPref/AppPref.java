package com.example.weddingplanner.appBase.appPref;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.example.weddingplanner.appBase.models.profile.ProfileRowModel;
import com.example.weddingplanner.appBase.utils.Constants;

public class AppPref {
    static final String CURRENT_EVENT_ID = "CURRENT_EVENT_ID";
    static final String IS_ADFREE = "IS_ADFREE";
    static final String IS_DB_VERSION_INSERTED = "IS_DB_VERSION_INSERTED";
    static final String IS_DEFAULT_INSERTED = "IS_DEFAULT_INSERTED";
    static final String IS_ENABLE_CUSTOM_DEBUG_LOGS = "isEnableDebugLog";
    static final String IS_ENABLE_CUSTOM_DEBUG_TOAST = "isEnableDebugToast";
    static final String IS_FIRST_LAUNCH = "isFirstLaunch";
    static final String IS_INVITATION_NOTIFICATION = "IS_INVITATION_NOTIFICATION";
    static final String IS_PAYMENT_NOTIFICATION = "IS_PAYMENT_NOTIFICATION";
    static final String IS_RATEUS = "IS_RATEUS";
    static final String IS_TASK_NOTIFICATION = "IS_TASK_NOTIFICATION";
    static final String IS_TERMS_ACCEPT = "IS_TERMS_ACCEPT";
    static final String MyPref = "userPref";
    static final String NEVER_SHOW_RATTING_DIALOG = "isNeverShowRatting";
    static final String PROFILE_DETAILS = "PROFILE_DETAILS";
    static final String SORT_TYPE_BUDGET = "SORT_TYPE_BUDGET";
    static final String SORT_TYPE_GUEST = "SORT_TYPE_GUEST";
    static final String SORT_TYPE_TASK = "SORT_TYPE_TASK";
    static final String SORT_TYPE_VENDOR = "SORT_TYPE_VENDOR";

    public static boolean getIsAdfree(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_ADFREE, false);
    }

    public static void setIsAdfree(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_ADFREE, z);
        edit.commit();
    }

    public static boolean IsTermsAccept(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_TERMS_ACCEPT, false);
    }

    public static void setIsTermsAccept(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_TERMS_ACCEPT, z);
        edit.commit();
    }

    public static boolean IsRateUS(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_RATEUS, false);
    }

    public static void setRateUS(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_RATEUS, z);
        edit.commit();
    }

    public static void setEnableDebugLog(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_ENABLE_CUSTOM_DEBUG_LOGS, z);
        edit.commit();
    }

    public static boolean isEnableDebugLog(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_ENABLE_CUSTOM_DEBUG_LOGS, true);
    }

    public static void setEnableDebugToast(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_ENABLE_CUSTOM_DEBUG_TOAST, z);
        edit.commit();
    }

    public static boolean isEnableDebugToast(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_ENABLE_CUSTOM_DEBUG_TOAST, false);
    }

    public static void setNeverShowRatting(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(NEVER_SHOW_RATTING_DIALOG, z);
        edit.commit();
    }

    public static boolean isNeverShowRatting(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(NEVER_SHOW_RATTING_DIALOG, false);
    }

    public static void setFirstLaunch(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_FIRST_LAUNCH, z);
        edit.commit();
    }

    public static boolean isFirstLaunch(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_FIRST_LAUNCH, true);
    }

    public static void setIsDbVersionInserted(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_DB_VERSION_INSERTED, z);
        edit.commit();
    }

    public static boolean isDbVersionInserted(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_DB_VERSION_INSERTED, false);
    }

    public static void setDefaultInserted(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_DEFAULT_INSERTED, z);
        edit.commit();
    }

    public static boolean isDefaultInserted(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_DEFAULT_INSERTED, false);
    }

    public static void setSortTypeTask(Context context, String str) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putString(SORT_TYPE_TASK, str);
        edit.commit();
    }

    public static String getSortTypeTask(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getString(SORT_TYPE_TASK, Constants.ORDER_TYPE_NAME_ASC);
    }

    public static void setSortTypeGuest(Context context, String str) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putString(SORT_TYPE_GUEST, str);
        edit.commit();
    }

    public static String getSortTypeGuest(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getString(SORT_TYPE_GUEST, Constants.ORDER_TYPE_NAME_ASC);
    }

    public static void setSortTypeBudget(Context context, String str) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putString(SORT_TYPE_BUDGET, str);
        edit.commit();
    }

    public static String getSortTypeBudget(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getString(SORT_TYPE_BUDGET, Constants.ORDER_TYPE_NAME_ASC);
    }

    public static void setSortTypeVendor(Context context, String str) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putString(SORT_TYPE_VENDOR, str);
        edit.commit();
    }

    public static String getSortTypeVendor(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getString(SORT_TYPE_VENDOR, Constants.ORDER_TYPE_NAME_ASC);
    }

    public static void setCurrentEvenId(Context context, String str) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putString(CURRENT_EVENT_ID, str);
        edit.commit();
    }

    public static String getCurrentEvenId(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getString(CURRENT_EVENT_ID, (String) null);
    }

    public static void setProfileDetails(Context context, ProfileRowModel profileRowModel) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putString(PROFILE_DETAILS, new Gson().toJson((Object) profileRowModel));
        edit.commit();
    }

    public static ProfileRowModel getProfileDetails(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(MyPref, 0);
        Gson gson = new Gson();
        return (ProfileRowModel) gson.fromJson(sharedPreferences.getString(PROFILE_DETAILS, gson.toJson((Object) new ProfileRowModel())), ProfileRowModel.class);
    }

    public static void setTaskNotification(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_TASK_NOTIFICATION, z);
        edit.commit();
    }

    public static boolean isTaskNotification(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_TASK_NOTIFICATION, true);
    }

    public static void setInvitationNotification(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_INVITATION_NOTIFICATION, z);
        edit.commit();
    }

    public static boolean isInvitationNotification(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_INVITATION_NOTIFICATION, true);
    }

    public static void setPaymentNotification(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getApplicationContext().getSharedPreferences(MyPref, 0).edit();
        edit.putBoolean(IS_PAYMENT_NOTIFICATION, z);
        edit.commit();
    }

    public static boolean isPaymentNotification(Context context) {
        return context.getApplicationContext().getSharedPreferences(MyPref, 0).getBoolean(IS_PAYMENT_NOTIFICATION, true);
    }
}
