package com.example.weddingplanner.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.example.weddingplanner.R;
import com.example.weddingplanner.appBase.appPref.AppPref;
import com.example.weddingplanner.appBase.roomsDB.AppDataBase;
import com.example.weddingplanner.appBase.roomsDB.category.CategoryRowModel;
import com.example.weddingplanner.appBase.utils.AdConstants;
import com.example.weddingplanner.appBase.utils.AdMobTwoButtonDialogListener;
import com.example.weddingplanner.appBase.utils.BackgroundAsync;
import com.example.weddingplanner.appBase.utils.Constants;
import com.example.weddingplanner.appBase.utils.OnAsyncBackground;
import com.example.weddingplanner.appBase.utils.TermAndServiceActivity;
import com.example.weddingplanner.appBase.view.AddEditProfileActivity;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity {
    boolean Ad_Show = true;

    public Context context;


    private AppDataBase db;

    SplashActivity splash_activity;

    public WeakReference<SplashActivity> splash_activityWeakReference;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        this.context = this;
        this.db = AppDataBase.getAppDatabase(this.context);

//        if (AppPref.getIsAdfree(this.context)) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (SplashActivity.this.Ad_Show) {
//                        SplashActivity.this.GoToMainScreen();
//                    }
//                }
//            }, 1000);
//            return;
//        }
        MobileAds.initialize(this.context, initializationStatus -> {

        });
        insertDefaultDataList();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SplashActivity.this.Ad_Show) {
                    SplashActivity.this.GoToMainScreen();
                }
            }
        }, 5000);
        this.splash_activity = this;
        this.splash_activityWeakReference = new WeakReference<>(this.splash_activity);
        fornpa();
    }


    public void GoToMainScreen() {
        this.Ad_Show = false;
        if (!AppPref.isFirstLaunch(this.context) || AppPref.isDefaultInserted(this.context)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    openMainActivity();
                }
            },5000);

        } else {

        }
    }

    private void insertDefaultDataList() {
        new BackgroundAsync(this.context, true, "", new OnAsyncBackground() {
            public void onPreExecute() {
            }

            public void doInBackground() {
                SplashActivity.this.insertDefaultList();
            }

            public void onPostExecute() {
                AppPref.setDefaultInserted(SplashActivity.this.context, true);
                SplashActivity.this.openMainActivity();
            }
        }).execute();
    }


    public void insertDefaultList() {
        addCostCategoryList();
    }

    private void addCostCategoryList() {
        try {
            this.db.categoryDao().insert(new CategoryRowModel("1", Constants.COST_CAT_TYPE_ATTIRE_ACCESSORIES, Constants.COST_CAT_TYPE_ATTIRE_ACCESSORIES, true));
            this.db.categoryDao().insert(new CategoryRowModel("2", Constants.COST_CAT_TYPE_HEALTH_BEAUTY, Constants.COST_CAT_TYPE_HEALTH_BEAUTY, true));
            this.db.categoryDao().insert(new CategoryRowModel("3", Constants.COST_CAT_TYPE_MUSIC_SHOW, Constants.COST_CAT_TYPE_MUSIC_SHOW, true));
            this.db.categoryDao().insert(new CategoryRowModel("4", Constants.COST_CAT_TYPE_FLOWER_DECOR, Constants.COST_CAT_TYPE_FLOWER_DECOR, true));
            this.db.categoryDao().insert(new CategoryRowModel("5", Constants.COST_CAT_TYPE_ACCESSORIES, Constants.COST_CAT_TYPE_ACCESSORIES, true));
            this.db.categoryDao().insert(new CategoryRowModel("6", Constants.COST_CAT_TYPE_JEWELRY, Constants.COST_CAT_TYPE_JEWELRY, true));
            this.db.categoryDao().insert(new CategoryRowModel("7", Constants.COST_CAT_TYPE_PHOTO_VIDEO, Constants.COST_CAT_TYPE_PHOTO_VIDEO, true));
            this.db.categoryDao().insert(new CategoryRowModel("8", Constants.COST_CAT_TYPE_CEREMONY, Constants.COST_CAT_TYPE_CEREMONY, true));
            this.db.categoryDao().insert(new CategoryRowModel("9", Constants.COST_CAT_TYPE_RECEPTION, Constants.COST_CAT_TYPE_RECEPTION, true));
            this.db.categoryDao().insert(new CategoryRowModel("10", Constants.COST_CAT_TYPE_TRANSPORTATION, Constants.COST_CAT_TYPE_TRANSPORTATION, true));
            this.db.categoryDao().insert(new CategoryRowModel("11", Constants.COST_CAT_TYPE_ACCOMMODATION, Constants.COST_CAT_TYPE_ACCOMMODATION, true));
            this.db.categoryDao().insert(new CategoryRowModel("12", Constants.COST_CAT_TYPE_MISCELLANEOUS, Constants.COST_CAT_TYPE_MISCELLANEOUS, true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void openMainActivity() {
        try {
            if (!AppPref.IsTermsAccept(this.context)) {
                startActivity(new Intent(this, TermAndServiceActivity.class));
            } else if (!AppPref.isFirstLaunch(this.context)) {
                startActivity(new Intent(this.context, MainActivityDashboard.class));
            } else {
                startActivity(new Intent(this.context, AddEditProfileActivity.class));
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fornpa() {
        ConsentInformation.getInstance(this.context).requestConsentInfoUpdate(AdConstants.publisherIds, new ConsentInfoUpdateListener() {
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                Log.d("consentStatus", consentStatus.toString());
                Log.d("conse", ConsentInformation.getInstance(SplashActivity.this.context).isRequestLocationInEeaOrUnknown() + "");
                if (ConsentInformation.getInstance(SplashActivity.this.context).isRequestLocationInEeaOrUnknown()) {
                    if (ConsentInformation.getInstance(SplashActivity.this.context).getConsentStatus() == ConsentStatus.PERSONALIZED) {
                        AdConstants.npaflag = false;
                        SplashActivity.this.afternpa();
                    }
                    if (ConsentInformation.getInstance(SplashActivity.this.context).getConsentStatus() == ConsentStatus.NON_PERSONALIZED) {
                        AdConstants.npaflag = true;
                        SplashActivity.this.afternpa();
                    }
                    if (ConsentInformation.getInstance(SplashActivity.this.context).getConsentStatus() == ConsentStatus.UNKNOWN) {
                        SplashActivity.this.Ad_Show = false;
                        try {
                            if (SplashActivity.this.splash_activityWeakReference.get() != null && !SplashActivity.this.splash_activityWeakReference.get().isFinishing()) {
                                SplashActivity.this.showDialog();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    AdConstants.npaflag = false;
                    SplashActivity.this.afternpa();
                }
            }

            public void onFailedToUpdateConsentInfo(String str) {
                AdConstants.setnpa(SplashActivity.this);
                SplashActivity.this.afternpa();
            }
        });
    }

    public void afternpa() {
        AdRequest adRequest;
        try {
            if (AdConstants.npaflag) {
                Log.d("NPA", "" + AdConstants.npaflag);
                Bundle bundle = new Bundle();
                bundle.putString("npa", "1");
                adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, bundle).build();
            } else {
                Log.d("NPA", "" + AdConstants.npaflag);
                adRequest = new AdRequest.Builder().build();
            }
//            this.interstitialAd = new InterstitialAd(this);
//            this.interstitialAd.setAdUnitId(AdConstants.AD_FULL);
//            this.interstitialAd.loadAd(adRequest);
//            this.Ad_Show = true;
//            this.interstitialAd.setAdListener(new C08862());
        } catch (Exception unused) {
            GoToMainScreen();
        }
    }



    public void showDialog() {
        AdConstants.showPersonalizeDialog(true, this, getString(R.string.app_name), getString(R.string.app_description1), getString(R.string.app_description2), getString(R.string.app_description3), new AdMobTwoButtonDialogListener() {
            public void onCancel() {
                SplashActivity.this.finish();
            }

            public void onOk(boolean z) {
                if (z) {
                    ConsentInformation.getInstance(SplashActivity.this.context).setConsentStatus(ConsentStatus.PERSONALIZED);
                } else {
                    ConsentInformation.getInstance(SplashActivity.this.context).setConsentStatus(ConsentStatus.NON_PERSONALIZED);
                }
                AdConstants.setnpa(SplashActivity.this);
                SplashActivity.this.afternpa();
            }
        });
    }
}
