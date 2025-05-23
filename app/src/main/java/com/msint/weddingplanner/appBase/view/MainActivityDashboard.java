package com.msint.weddingplanner.appBase.view;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.appPref.AppPref;
import com.msint.weddingplanner.appBase.baseClass.BaseActivityBinding;
import com.msint.weddingplanner.appBase.roomsDB.AppDataBase;
import com.msint.weddingplanner.appBase.utils.AdConstants;
import com.msint.weddingplanner.appBase.utils.AppConstants;
import com.msint.weddingplanner.appBase.utils.Constants;
import com.msint.weddingplanner.appBase.utils.WeddingDisclosure;
import com.msint.weddingplanner.databinding.ActivityMainDashboardBinding;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivityDashboard extends BaseActivityBinding {
    public static Activity adActivity;
    public static InterstitialAd interstitialAd_admob;
    private static Context maincontext;

    public ActivityMainDashboardBinding binding;
    private Timer timer;

    /* access modifiers changed from: protected */
    public void setBinding() {
        maincontext = this;
        this.binding = (ActivityMainDashboardBinding) DataBindingUtil.setContentView(this, R.layout.activity_main_dashboard);
        LoadAd();
    }

    /* access modifiers changed from: protected */
    public void setToolbar() {
        setSupportActionBar(this.binding.toolbar);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.drawer_feedback:
                AppConstants.emailUs(this.context);
                return true;
            case R.id.drawer_privacy_policy:
                uriparse(WeddingDisclosure.strPrivacyUri);
                return true;
            case R.id.drawer_proversion:
                startActivity(new Intent(this.context, WeddingProVersionActivity.class).setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE));
                return true;
            case R.id.drawer_ratting:
                AppConstants.showRattingDialog(this.context, Constants.RATTING_BAR_TITLE, Constants.APP_PLAY_STORE_URL);
                return true;
            case R.id.drawer_setting:
                startActivityForResult(new Intent(this.context, SettingActivity.class).setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE), 1002);
                return true;
            case R.id.drawer_share:
                AppConstants.shareApp(this.context);
                return true;
            case R.id.drawer_terms_of_service:
                uriparse(WeddingDisclosure.strTermsUri);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public void uriparse(String str) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.addFlags(1208483840);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/developer?id=MS+International")));
        }
    }

    /* access modifiers changed from: protected */
    public void setOnClicks() {
        this.binding.cardTasks.setOnClickListener(this);
        this.binding.cardGuests.setOnClickListener(this);
        this.binding.cardBudget.setOnClickListener(this);
        this.binding.cardVendor.setOnClickListener(this);
        this.binding.cardDashboard.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardBudget:
                startActivity(new Intent(this.context, CostListActivity.class).setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE));
                return;
            case R.id.cardDashboard:
                startActivity(new Intent(this.context, DashboardActivity.class).setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE));
                return;
            case R.id.cardGuests:
                startActivity(new Intent(this.context, GuestListActivity.class).setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE));
                return;
            case R.id.cardTasks:
                startActivity(new Intent(this.context, TaskListActivity.class).setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE));
                return;
            case R.id.cardVendor:
                startActivity(new Intent(this.context, VendorListActivity.class).setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE));
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void initMethods() {
        setProfile();
    }

    private void setRemainingTimer() {
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                MainActivityDashboard.this.updateValue();
            }
        }, 0, 1000);
    }

    public void updateValue() {
        runOnUiThread(new Runnable() {
            public void run() {
                long j;
                try {
                    j = AppDataBase.getAppDatabase(MainActivityDashboard.this.context).profileDao().getDetail(AppPref.getCurrentEvenId(MainActivityDashboard.this.context)).getDateTimeInMillis() - System.currentTimeMillis();
                } catch (Exception e) {
                    e.printStackTrace();
                    j = 0;
                }
                long j2 = j / 1000;
                long j3 = j2 % 60;
                long j4 = j / 86400000;
                long j5 = j4 % 7;
                long j6 = j4 % 30;
                long j7 = j4 / 7;
                long j8 = j4 / 30;
                long j9 = j4 / 365;
                long j10 = (75 * j2) / 60;
                long j11 = (j2 * 16) / 60;
                MainActivityDashboard.this.binding.txtDays.setText(AppConstants.getFormattedLong(j4));
                MainActivityDashboard.this.binding.txtHours.setText(AppConstants.getFormattedLong((j / 3600000) % 24));
                MainActivityDashboard.this.binding.txtMinutes.setText(AppConstants.getFormattedLong((j / 60000) % 60));
                MainActivityDashboard.this.binding.txtSeconds.setText(AppConstants.getFormattedLong(j3));
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 1002) {
            setProfile();
        }
    }

    private void setProfile() {
        try {
            this.binding.txtMarriageName.setText(AppDataBase.getAppDatabase(this.context).profileDao().getDetail(AppPref.getCurrentEvenId(this.context)).getWeddingName());
            setRemainingTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        if (!AppPref.IsRateUS(this)) {
            AppPref.setRateUS(this, true);
            AppConstants.showRattingDialog(this.context, Constants.RATTING_BAR_TITLE, Constants.APP_PLAY_STORE_URL);
            return;
        }
        super.onBackPressed();
    }

    public static void LoadAd() {
        AdRequest adRequest;
        try {
            if (!AppPref.getIsAdfree(maincontext)) {
                Log.d("LoadAd", "AdMob");
                if (AdConstants.npaflag) {
                    Log.d("NPA", "" + AdConstants.npaflag);
                    Bundle bundle = new Bundle();
                    bundle.putString("npa", "1");
                    adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, bundle).build();
                } else {
                    Log.d("NPA", "" + AdConstants.npaflag);
                    adRequest = new AdRequest.Builder().build();
                }
                interstitialAd_admob = new InterstitialAd(maincontext);
                interstitialAd_admob.setAdUnitId(AdConstants.AD_FULL);
                interstitialAd_admob.loadAd(adRequest);
                interstitialAd_admob.setAdListener(new AdListener() {
                    public void onAdClosed() {
                        super.onAdClosed();
                        MainActivityDashboard.BackScreen();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void BackScreen() {
        if (!AdConstants.isAdShown && (interstitialAd_admob == null || !interstitialAd_admob.isLoaded())) {
            LoadAd();
        }
        if (adActivity != null) {
            adActivity.finish();
        }
    }

    public static void BackPressedAd(Activity activity) {
        adActivity = activity;
        if (interstitialAd_admob == null) {
            BackScreen();
        } else if (!interstitialAd_admob.isLoaded() || AdConstants.isAdShown) {
            BackScreen();
        } else {
            try {
                interstitialAd_admob.show();
            } catch (Exception unused) {
                BackScreen();
            }
            AdConstants.isAdShown = true;
        }
    }
}
