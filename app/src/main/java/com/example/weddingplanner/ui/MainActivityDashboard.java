package com.example.weddingplanner.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.weddingplanner.adsUtils.TapdaqAdsUtils;
import com.example.weddingplanner.appBase.view.CostListActivity;
import com.example.weddingplanner.appBase.view.GuestListActivity;
import com.example.weddingplanner.appBase.view.TaskListActivity;
import com.example.weddingplanner.appBase.view.VendorListActivity;
import com.example.weddingplanner.R;
import com.example.weddingplanner.appBase.appPref.AppPref;
import com.example.weddingplanner.appBase.baseClass.BaseActivityBinding;
import com.example.weddingplanner.appBase.roomsDB.AppDataBase;
import com.example.weddingplanner.appBase.utils.AppConstants;
import com.example.weddingplanner.databinding.ActivityMainDashboardBinding;
import com.tapdaq.sdk.STATUS;
import com.tapdaq.sdk.TMBannerAdView;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.common.TMBannerAdSizes;
import com.tapdaq.sdk.listeners.TMAdListener;
import com.tapdaq.sdk.listeners.TMInitListener;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivityDashboard extends BaseActivityBinding {
    private static final int REQUEST_CODE = 1002;
    public static Activity adActivity;
    private static Context maincontext;
    public static String strPrivacyUri = "https://www.google.com/";
    public static String strTermsUri = "https://www.google.com/";
    public ActivityMainDashboardBinding binding;
    private Timer timer;
    boolean doubleBackToExitPressedOnce = false;
    public AppDataBase db;


    public void setBinding() {
        maincontext = this;
        binding = ActivityMainDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        TapdaqConfig config = Tapdaq.getInstance().config();

        config.setUserSubjectToGdprStatus(STATUS.TRUE); //GDPR declare if user is in EU
        config.setConsentStatus(STATUS.TRUE); //GDPR consent must be obtained from the user
        config.setAgeRestrictedUserStatus(STATUS.FALSE); //Is user subject to COPPA or GDPR age restrictions

        Tapdaq.getInstance().initialize(this, "<APP_ID>", "<CLIENT_KEY>", config, new TapdaqInitListener());

        TMBannerAdView ad = new TMBannerAdView(this); // Create ad view
        binding.adBanner.addView(ad); // Insert view into layout
        ad.load(this, TMBannerAdSizes.STANDARD, new TMAdListener()); // Load banner with predefined size using default placement

        db = AppDataBase.getAppDatabase(this);

        LoadAd();
        initGlid();
    }
    @Override
    protected void onDestroy() {

        if (binding.adBanner != null) {
            binding.adBanner.destroy(this);
        }
        super.onDestroy();
    }
    public class TapdaqInitListener extends TMInitListener {

        public void didInitialise() {
            super.didInitialise();
            // Ads may now be requested

            TapdaqAdsUtils.loadInterstitial(MainActivityDashboard.this);
            TapdaqAdsUtils.showInterstitial(MainActivityDashboard.this);


        }

        @Override
        public void didFailToInitialise(TMAdError error) {
            super.didFailToInitialise(error);
            //Tapdaq failed to initialise
        }
    }
    private void initGlid() {
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_dashboard).centerCrop()).into(binding.navDrawer.imgHome);
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_tasks).centerCrop()).into(binding.navDrawer.imgChecklist);
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_budgets).centerCrop()).into(binding.navDrawer.imgBudget);
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_guests).centerCrop()).into(binding.navDrawer.imgGuest);
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_vendors).centerCrop()).into(binding.navDrawer.imgVendor);
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_setting).centerCrop()).into(binding.navDrawer.imgSettings);
        ((RequestBuilder) Glide.with(this).load(R.drawable.rate_us).centerCrop()).into(binding.navDrawer.imgRateus);
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_share).centerCrop()).into(binding.navDrawer.imgShare);
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_privacy_policy).centerCrop()).into(binding.navDrawer.imgPrivacypolicy);
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_term_service).centerCrop()).into(binding.navDrawer.imgTerms);

        

        binding.navDrawer.llHome.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivityDashboard.this, DashboardActivity.class));

            }
        });
        binding.navDrawer.llChecklist.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivityDashboard.this, TaskListActivity.class));
                finish();

            }
        });
        binding.navDrawer.llBudget.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivityDashboard.this, CostListActivity.class));
                finish();
            }
        });
        binding.navDrawer.llGuest.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivityDashboard.this, GuestListActivity.class));
                finish();
            }
        });
        binding.navDrawer.llVendor.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivityDashboard.this, VendorListActivity.class));
                finish();
            }
        });

        binding.navDrawer.llSettings.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                startActivityForResult(new Intent(MainActivityDashboard.this, SettingActivity.class), REQUEST_CODE);

            }
        });


        binding.navDrawer.llRateus.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getPackageName())));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });
        binding.navDrawer.llShare.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name));
                    intent.putExtra("android.intent.extra.TEXT", "Wedding Planner\n\nPlan your Wedding Day – Tasks, Budget, Guests, Vendors, Payments\n\n-  Create your wedding, or Join your closed ones’ wedding\n-  Dashboard for summary of Tasks, Budget, Guests and Vendors\n-  Show and Share Wedding Countdown\n-  Export data in PDF format\n\nhttps://play.google.com/store/apps/details?id=" + getPackageName());
                    startActivity(Intent.createChooser(intent, "Share via"));
                } catch (Exception unused) {
                }
            }
        });

        binding.navDrawer.llPrivacyPolicy.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(strPrivacyUri));
                intent.addFlags(1208483840);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException unused) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(strPrivacyUri)));
                }
            }
        });
        binding.navDrawer.llTerms.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(strPrivacyUri));
                intent.addFlags(1208483840);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException unused) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(strPrivacyUri)));
                }
            }
        });
    }

    public void hamMenu() {
        if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        } else {
            binding.drawerLayout.closeDrawer(GravityCompat.END);
        }
    }

    public void setToolbar() {
        setSupportActionBar(binding.toolbar);
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


    public void setOnClicks() {
        binding.cardTasks.setOnClickListener(this);
        binding.cardGuests.setOnClickListener(this);
        binding.cardBudget.setOnClickListener(this);
        binding.cardVendor.setOnClickListener(this);
        binding.cardDashboard.setOnClickListener(this);
        binding.imgMenu.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_menu:
                hamMenu();
                return;
            case R.id.cardBudget:
                TapdaqAdsUtils.showInterstitial(MainActivityDashboard.this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(context, CostListActivity.class));

                    }
                },2000);

                return;
            case R.id.cardDashboard:
                TapdaqAdsUtils.showInterstitial(MainActivityDashboard.this);
                startActivity(new Intent(context, DashboardActivity.class));
                return;
            case R.id.cardGuests:
                startActivity(new Intent(context, GuestListActivity.class));
                TapdaqAdsUtils.showInterstitial(MainActivityDashboard.this);
                return;
            case R.id.cardTasks:
                startActivity(new Intent(context, TaskListActivity.class));
                TapdaqAdsUtils.showInterstitial(MainActivityDashboard.this);
                return;
            case R.id.cardVendor:
                startActivity(new Intent(context, VendorListActivity.class));
                TapdaqAdsUtils.showInterstitial(MainActivityDashboard.this);
                return;
            default:
                return;
        }
    }


    public void initMethods() {
        setProfile();
    }

    private void setRemainingTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                updateValue();
            }
        }, 0, 1000);
    }

    public void updateValue() {
        runOnUiThread(new Runnable() {
            public void run() {
                long j;
                try {
                    j = db.profileDao().getDetail(AppPref.getCurrentEvenId(context)).getDateTimeInMillis() - System.currentTimeMillis();
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
                binding.txtDays.setText(AppConstants.getFormattedLong(j4));
                binding.txtHours.setText(AppConstants.getFormattedLong((j / 3600000) % 24));
                binding.txtMinutes.setText(AppConstants.getFormattedLong((j / 60000) % 60));
                binding.txtSeconds.setText(AppConstants.getFormattedLong(j3));
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            setProfile();
        }
    }

    private void setProfile() {
        try {
            binding.txtMarriageName.setText(db.profileDao().getDetail(AppPref.getCurrentEvenId(context)).getWeddingName());
            setRemainingTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);

    }

    public static void LoadAd() {

    }

}
