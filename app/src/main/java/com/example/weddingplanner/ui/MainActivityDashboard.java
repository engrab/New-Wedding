package com.example.weddingplanner.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.weddingplanner.appBase.view.CostListActivity;
import com.example.weddingplanner.appBase.view.GuestListActivity;
import com.example.weddingplanner.appBase.view.TaskListActivity;
import com.example.weddingplanner.appBase.view.VendorListActivity;
import com.example.weddingplanner.appBase.view.WeddingProVersionActivity;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.example.weddingplanner.R;
import com.example.weddingplanner.appBase.appPref.AppPref;
import com.example.weddingplanner.appBase.baseClass.BaseActivityBinding;
import com.example.weddingplanner.appBase.roomsDB.AppDataBase;
import com.example.weddingplanner.appBase.utils.AdConstants;
import com.example.weddingplanner.appBase.utils.AppConstants;
import com.example.weddingplanner.appBase.utils.Constants;
import com.example.weddingplanner.appBase.utils.TermAndServiceActivity;
import com.example.weddingplanner.databinding.ActivityMainDashboardBinding;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivityDashboard extends BaseActivityBinding {
    public static Activity adActivity;
    private static Context maincontext;

    public ActivityMainDashboardBinding binding;
    private Timer timer;


    public void setBinding() {
        maincontext = this;
        binding = ActivityMainDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        LoadAd();
        initGlid();
    }

    private void initGlid() {
        ((RequestBuilder) Glide.with(this).load(Integer.valueOf(R.drawable.drawer_dashboard)).centerCrop()).into(this.binding.navDrawer.imgHome);
        ((RequestBuilder) Glide.with(this).load(Integer.valueOf(R.drawable.drawer_tasks)).centerCrop()).into(this.binding.navDrawer.imgChecklist);
        ((RequestBuilder) Glide.with(this).load(Integer.valueOf(R.drawable.drawer_budgets)).centerCrop()).into(this.binding.navDrawer.imgBudget);
        ((RequestBuilder) Glide.with(this).load(Integer.valueOf(R.drawable.drawer_guests)).centerCrop()).into(this.binding.navDrawer.imgGuest);
        ((RequestBuilder) Glide.with(this).load(Integer.valueOf(R.drawable.drawer_vendors)).centerCrop()).into(this.binding.navDrawer.imgVendor);
        ((RequestBuilder) Glide.with(this).load(Integer.valueOf(R.drawable.drawer_vendors)).centerCrop()).into(this.binding.navDrawer.imgMywedding);
        ((RequestBuilder) Glide.with(this).load(Integer.valueOf(R.drawable.drawer_setting)).centerCrop()).into(this.binding.navDrawer.imgSettings);
        ((RequestBuilder) Glide.with(this).load(Integer.valueOf(R.drawable.drawer_setting)).centerCrop()).into(this.binding.navDrawer.imgSubscription);
        ((RequestBuilder) Glide.with(this).load(Integer.valueOf(R.drawable.settings)).centerCrop()).into(this.binding.navDrawer.imgSignout);
        ((RequestBuilder) Glide.with(this).load(Integer.valueOf(R.drawable.rate_us)).centerCrop()).into(this.binding.navDrawer.imgRateus);
        ((RequestBuilder) Glide.with(this).load(Integer.valueOf(R.drawable.drawer_share)).centerCrop()).into(this.binding.navDrawer.imgShare);
        ((RequestBuilder) Glide.with(this).load(Integer.valueOf(R.drawable.drawer_feedback)).centerCrop()).into(this.binding.navDrawer.imgFeedback);
        ((RequestBuilder) Glide.with(this).load(Integer.valueOf(R.drawable.drawer_privacy_policy)).centerCrop()).into(this.binding.navDrawer.imgPrivacypolicy);
        ((RequestBuilder) Glide.with(this).load(Integer.valueOf(R.drawable.drawer_terms_of_service)).centerCrop()).into(this.binding.navDrawer.imgTerms);

        

        binding.navDrawer.imgHome.setOnClickListener(new View.OnClickListener() {
            /* class com.selfmentor.myweddingplanner.activity.HomeActivity.AnonymousClass21 */

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
               
            }
        });
        binding.navDrawer.imgChecklist.setOnClickListener(new View.OnClickListener() {
            /* class com.selfmentor.myweddingplanner.activity.HomeActivity.AnonymousClass21 */

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
               
            }
        });
        binding.navDrawer.imgBudget.setOnClickListener(new View.OnClickListener() {
            /* class com.selfmentor.myweddingplanner.activity.HomeActivity.AnonymousClass21 */

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
               
            }
        });
        binding.navDrawer.imgGuest.setOnClickListener(new View.OnClickListener() {
            /* class com.selfmentor.myweddingplanner.activity.HomeActivity.AnonymousClass21 */

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
               
            }
        });
        binding.navDrawer.imgVendor.setOnClickListener(new View.OnClickListener() {
            /* class com.selfmentor.myweddingplanner.activity.HomeActivity.AnonymousClass21 */

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
               
            }
        });
        binding.navDrawer.imgMywedding.setOnClickListener(new View.OnClickListener() {
            /* class com.selfmentor.myweddingplanner.activity.HomeActivity.AnonymousClass21 */

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
               
            }
        });
        binding.navDrawer.imgSettings.setOnClickListener(new View.OnClickListener() {
            /* class com.selfmentor.myweddingplanner.activity.HomeActivity.AnonymousClass21 */

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
               
            }
        });
        binding.navDrawer.imgSubscription.setOnClickListener(new View.OnClickListener() {
            /* class com.selfmentor.myweddingplanner.activity.HomeActivity.AnonymousClass21 */

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
               
            }
        });
        binding.navDrawer.imgSignout.setOnClickListener(new View.OnClickListener() {
            /* class com.selfmentor.myweddingplanner.activity.HomeActivity.AnonymousClass21 */

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
               
            }
        });
        binding.navDrawer.imgRateus.setOnClickListener(new View.OnClickListener() {
            /* class com.selfmentor.myweddingplanner.activity.HomeActivity.AnonymousClass21 */

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
               
            }
        });
        binding.navDrawer.imgShare.setOnClickListener(new View.OnClickListener() {
            /* class com.selfmentor.myweddingplanner.activity.HomeActivity.AnonymousClass21 */

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
               
            }
        });
        binding.navDrawer.imgFeedback.setOnClickListener(new View.OnClickListener() {
            /* class com.selfmentor.myweddingplanner.activity.HomeActivity.AnonymousClass21 */

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
               
            }
        });
        binding.navDrawer.imgPrivacypolicy.setOnClickListener(new View.OnClickListener() {
            /* class com.selfmentor.myweddingplanner.activity.HomeActivity.AnonymousClass21 */

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
               
            }
        });
        binding.navDrawer.imgTerms.setOnClickListener(new View.OnClickListener() {
            /* class com.selfmentor.myweddingplanner.activity.HomeActivity.AnonymousClass21 */

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
               
            }
        });
        
        
        
        
        
        
        
        
        
        
        
        
        
    }

    public void hamMenu() {
        if (!this.binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.binding.drawerLayout.openDrawer(GravityCompat.START);
        } else {
            this.binding.drawerLayout.closeDrawer(GravityCompat.END);
        }
    }

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
                uriparse(TermAndServiceActivity.strPrivacyUri);
                return true;
            case R.id.drawer_proversion:
                startActivity(new Intent(this.context, WeddingProVersionActivity.class));
                return true;
            case R.id.drawer_ratting:
                AppConstants.showRattingDialog(this.context, Constants.RATTING_BAR_TITLE, Constants.APP_PLAY_STORE_URL);
                return true;
            case R.id.drawer_setting:
                startActivityForResult(new Intent(this.context, SettingActivity.class), 1002);
                return true;
            case R.id.drawer_share:
                AppConstants.shareApp(this.context);
                return true;
            case R.id.drawer_terms_of_service:
                uriparse(TermAndServiceActivity.strTermsUri);
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


    public void setOnClicks() {
        this.binding.cardTasks.setOnClickListener(this);
        this.binding.cardGuests.setOnClickListener(this);
        this.binding.cardBudget.setOnClickListener(this);
        this.binding.cardVendor.setOnClickListener(this);
        this.binding.cardDashboard.setOnClickListener(this);
        binding.imgMenu.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_menu:
                hamMenu();
                return;
            case R.id.cardBudget:
                startActivity(new Intent(this.context, CostListActivity.class));
                return;
            case R.id.cardDashboard:
                startActivity(new Intent(this.context, DashboardActivity.class));
                return;
            case R.id.cardGuests:
                startActivity(new Intent(this.context, GuestListActivity.class));
                return;
            case R.id.cardTasks:
                startActivity(new Intent(this.context, TaskListActivity.class));
                return;
            case R.id.cardVendor:
                startActivity(new Intent(this.context, VendorListActivity.class));
                return;
            default:
                return;
        }
    }


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

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
