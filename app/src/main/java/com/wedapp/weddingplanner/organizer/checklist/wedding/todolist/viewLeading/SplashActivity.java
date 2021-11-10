package com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.viewLeading;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.R;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.appPref.AppPrefLeading;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.roomDatabase.AppDataBase;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.roomDatabase.category.CategoryRowModel;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils.AdConstants;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils.AdMobTwoButtonDialogListener;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils.BackgroundAsync;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils.Constants;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils.OnAsyncBackground;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils.TermAndServiceActivity;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.view.AddEditProfileActivityLeading;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;


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




        insertDefaultDataList();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Ad_Show) {
                    GoToMainScreen();
                }
            }
        }, 5000);
        this.splash_activity = this;
        this.splash_activityWeakReference = new WeakReference<>(this.splash_activity);
        fornpa();
    }


    public void GoToMainScreen() {
        this.Ad_Show = false;
        if (!AppPrefLeading.isFirstLaunch(this.context) || AppPrefLeading.isDefaultInserted(this.context)) {
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
                insertDefaultList();
            }

            public void onPostExecute() {
                AppPrefLeading.setDefaultInserted(context, true);
                openMainActivity();
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
            if (!AppPrefLeading.IsTermsAccept(this.context)) {
                startActivity(new Intent(this, TermAndServiceActivity.class));
            } else if (!AppPrefLeading.isFirstLaunch(this.context)) {
                startActivity(new Intent(this.context, MainActivityDashboardLeading.class));
            } else {
                startActivity(new Intent(this.context, AddEditProfileActivityLeading.class));
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fornpa() {
        ConsentInformation.getInstance(this.context).requestConsentInfoUpdate(AdConstants.publisherIds, new ConsentInfoUpdateListener() {
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                if (ConsentInformation.getInstance(context).isRequestLocationInEeaOrUnknown()) {
                    if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.PERSONALIZED) {
                        AdConstants.npaflag = false;
                        afternpa();
                    }
                    if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.NON_PERSONALIZED) {
                        AdConstants.npaflag = true;
                        afternpa();
                    }
                    if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.UNKNOWN) {
                        Ad_Show = false;
                        try {
                            if (splash_activityWeakReference.get() != null && !splash_activityWeakReference.get().isFinishing()) {
                                showDialog();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    AdConstants.npaflag = false;
                    afternpa();
                }
            }

            public void onFailedToUpdateConsentInfo(String str) {
                AdConstants.setnpa(SplashActivity.this);
                afternpa();
            }
        });
    }

    public void afternpa() {

    }




    public void showDialog() {
        AdConstants.showPersonalizeDialog(true, this, getString(R.string.app_name), getString(R.string.app_description1), getString(R.string.app_description2), getString(R.string.app_description3), new AdMobTwoButtonDialogListener() {
            public void onCancel() {
                finish();
            }

            public void onOk(boolean z) {
                if (z) {
                    ConsentInformation.getInstance(context).setConsentStatus(ConsentStatus.PERSONALIZED);
                } else {
                    ConsentInformation.getInstance(context).setConsentStatus(ConsentStatus.NON_PERSONALIZED);
                }
                AdConstants.setnpa(SplashActivity.this);
                afternpa();
            }
        });
    }
}
