package com.msint.weddingplanner.appBase.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.appPref.AppPref;
import com.msint.weddingplanner.appBase.roomsDB.AppDataBase;
import com.msint.weddingplanner.appBase.roomsDB.category.CategoryRowModel;
import com.msint.weddingplanner.appBase.utils.AdConstants;
import com.msint.weddingplanner.appBase.utils.AdMobTwoButtonDialogListener;
import com.msint.weddingplanner.appBase.utils.AppConstants;
import com.msint.weddingplanner.appBase.utils.BackgroundAsync;
import com.msint.weddingplanner.appBase.utils.Constants;
import com.msint.weddingplanner.appBase.utils.OnAsyncBackground;
import com.msint.weddingplanner.appBase.utils.WeddingDisclosure;
import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity {
    boolean Ad_Show = true;

    public Context context;

    /* renamed from: db */
    private AppDataBase db;

    public InterstitialAd interstitialAd;
    SplashActivity splash_activity;

    public WeakReference<SplashActivity> splash_activityWeakReference;

    class C08841 implements Runnable {
        C08841() {
        }

        public void run() {
            if (SplashActivity.this.Ad_Show) {
                SplashActivity.this.GoToMainScreen();
            }
        }
    }

    class C08862 extends AdListener {

        class C08851 implements Runnable {
            C08851() {
            }

            public void run() {
                if (SplashActivity.this.Ad_Show) {
                    SplashActivity.this.GoToMainScreen();
                }
            }
        }

        C08862() {
        }

        public void onAdLoaded() {
            if (SplashActivity.this.interstitialAd.isLoaded() && SplashActivity.this.Ad_Show) {
                SplashActivity.this.Ad_Show = false;
                try {
                    SplashActivity.this.interstitialAd.show();
                } catch (Exception unused) {
                    SplashActivity.this.GoToMainScreen();
                }
            }
        }

        public void onAdFailedToLoad(int i) {
            new Handler().postDelayed(new C08851(), 3000);
        }

        public void onAdClosed() {
            SplashActivity.this.GoToMainScreen();
        }

        public void onAdOpened() {
            super.onAdOpened();
        }

        public void onAdLeftApplication() {
            super.onAdLeftApplication();
        }
    }

    public SplashActivity() {
        AdConstants.isAdShown = false;
    }


    public void onCreate(Bundle bundle) {
        this.context = this;
        this.db = AppDataBase.getAppDatabase(this.context);
        try {
            super.onCreate(bundle);
            setContentView((int) R.layout.activity_splash);
            ((TextView) findViewById(R.id.versionApp)).setText(AppConstants.getVersion(this.context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (AppPref.getIsAdfree(this.context)) {
            new Handler().postDelayed(new C08841(), 3000);
            return;
        }
        MobileAds.initialize(this.context, getString(R.string.strAdmobAppId));
        new Handler().postDelayed(new C08841(), 15000);
        this.splash_activity = this;
        this.splash_activityWeakReference = new WeakReference<>(this.splash_activity);
        fornpa();
    }


    public void GoToMainScreen() {
        this.Ad_Show = false;
        if (!AppPref.isFirstLaunch(this.context) || AppPref.isDefaultInserted(this.context)) {
            openMainActivity();
        } else {
            insertDefaultDataList();
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
        }).execute(new Object[0]);
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
                startActivity(new Intent(this, WeddingDisclosure.class));
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
                            if (SplashActivity.this.splash_activityWeakReference.get() != null && !((SplashActivity) SplashActivity.this.splash_activityWeakReference.get()).isFinishing()) {
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
            this.interstitialAd = new InterstitialAd(this);
            this.interstitialAd.setAdUnitId(AdConstants.AD_FULL);
            this.interstitialAd.loadAd(adRequest);
            this.Ad_Show = true;
            this.interstitialAd.setAdListener(new C08862());
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
