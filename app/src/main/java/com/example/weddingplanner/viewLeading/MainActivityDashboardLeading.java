package com.example.weddingplanner.viewLeading;

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
import com.example.weddingplanner.adsUtilsLeading.AdsUtils;
import com.example.weddingplanner.allLeading.models.profile.ProfileListModel;
import com.example.weddingplanner.allLeading.models.profile.ProfileRowModel;
import com.example.weddingplanner.allLeading.roomDatabase.taskList.TaskRowModel;
import com.example.weddingplanner.allLeading.view.AddEditProfileActivityLeading;
import com.example.weddingplanner.allLeading.view.CostListActivityLeading;
import com.example.weddingplanner.allLeading.view.GuestListActivityLeading;
import com.example.weddingplanner.allLeading.view.TaskListActivityLeading;
import com.example.weddingplanner.allLeading.view.VendorListActivityLeading;
import com.example.weddingplanner.R;
import com.example.weddingplanner.allLeading.appPref.AppPrefLeading;
import com.example.weddingplanner.allLeading.baseClass.BaseActivityBindingLeading;
import com.example.weddingplanner.allLeading.roomDatabase.AppDataBase;
import com.example.weddingplanner.allLeading.utils.AppConstants;
import com.example.weddingplanner.databinding.ActivityMainDashboardBinding;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivityDashboardLeading extends BaseActivityBindingLeading {
    private static final int REQUEST_CODE = 1002;
    public static Activity adActivity;
    private static Context maincontext;
    public static String strPrivacyUri = "https://www.google.com/";
    public static String strTermsUri = "https://www.google.com/";
    public ActivityMainDashboardBinding binding;
    private Timer timer;
    boolean doubleBackToExitPressedOnce = false;
    public AppDataBase db;

    public ProfileListModel model;
    AdView adView;


    public void setBinding() {
        maincontext = this;
        binding = ActivityMainDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        db = AppDataBase.getAppDatabase(this);

        LoadAd();
        initGlid();
        fillData();

//        loadImageFromStorage("profileImage/");

    }

    @Override
    protected void onResume() {
        super.onResume();
        taskDataDBDemo();
        guestDataDBDemo();
        budgetDataDBDemo();
        vendorDataDBDemo();
    }

    @Override
    protected void onDestroy() {

        if (adView != null){
            adView.destroy();
        }

        super.onDestroy();
    }
//    public void taskDataDB() {
//        ArrayList<TaskRowModel> listMain = new ArrayList<>();
//        List arrayList = new ArrayList();
//        try {
//            arrayList = this.db.taskDao().getAll(AppPrefLeading.getCurrentEvenId(this.context));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (arrayList.size() > 0) {
//            binding.tvAllTask.setText(arrayList.size()+"");
//            for (int i = 0; i < arrayList.size(); i++) {
//                TaskRowModel taskRowModel = (TaskRowModel) arrayList.get(i);
//                try {
//                    taskRowModel.setArrayList(new ArrayList());
//                    taskRowModel.getArrayList().addAll(this.db.subTaskDao().getAll(taskRowModel.getId()));
//                } catch (Exception e3) {
//                    e3.printStackTrace();
//                }
//                listMain.add(taskRowModel);
//            }
//            int count= 0;
//            for (int i = 0; i < listMain.size(); i++) {
//                    if (listMain.get(i).isPending()) {
//                        count++;
//                    }
//            }
//            binding.tvCompletedTask.setText(count+"");
//        }
//    }

    public void taskDataDBDemo(){
            long allCount = db.taskDao().getAllCount(AppPrefLeading.getCurrentEvenId(this.context));
            long pendingCount = db.taskDao().getAllCount(AppPrefLeading.getCurrentEvenId(this.context), 0);
            if (allCount > 0){
                binding.tvAllTask.setText(allCount+"");
                binding.tvCompletedTask.setText(pendingCount+"");
            }


    }

    public void guestDataDBDemo(){

            long allCount = db.guestDao().getAllCount(AppPrefLeading.getCurrentEvenId(this.context));
            long pendingCount = db.guestDao().getInvitationSentCount(AppPrefLeading.getCurrentEvenId(this.context),1);
            binding.tvAllGuest.setText(allCount+"");
            binding.tvInvited.setText(pendingCount+"");


    }

    public void budgetDataDBDemo(){

        double allTotal = db.costDao().getAllTotal(AppPrefLeading.getCurrentEvenId(this.context));
        double total = db.costDao().getTotal(AppPrefLeading.getCurrentEvenId(this.context), 0);
        binding.tvTotalBudget.setText(allTotal+"");
        binding.tvUsedBudget.setText(total+"");


    }
    public void vendorDataDBDemo(){

        long allTotal = db.vendorDao().getAllCount(AppPrefLeading.getCurrentEvenId(this.context));
        double total = db.vendorDao().getTotal(AppPrefLeading.getCurrentEvenId(this.context),0);
        binding.tvAllVendor.setText(allTotal+"");
        binding.tvVendorPaid.setText(total+"");


    }


    private void initGlid() {
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_dashboard).centerCrop()).into(binding.navDrawer.imgHome);
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_tasks).centerCrop()).into(binding.navDrawer.imgChecklist);
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_budgets).centerCrop()).into(binding.navDrawer.imgBudget);
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_guests).centerCrop()).into(binding.navDrawer.imgGuest);
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_vendors).centerCrop()).into(binding.navDrawer.imgVendor);
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_setting).centerCrop()).into(binding.navDrawer.imgSettings);
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_setting).centerCrop()).into(binding.navDrawer.imgProfile);
        ((RequestBuilder) Glide.with(this).load(R.drawable.rate_us).centerCrop()).into(binding.navDrawer.imgRateus);
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_share).centerCrop()).into(binding.navDrawer.imgShare);
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_privacy_policy).centerCrop()).into(binding.navDrawer.imgPrivacypolicy);
        ((RequestBuilder) Glide.with(this).load(R.drawable.drawer_term_service).centerCrop()).into(binding.navDrawer.imgTerms);


        binding.cvTaskList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityDashboardLeading.this, TaskListActivityLeading.class));
                AdsUtils.showInterstitial(MainActivityDashboardLeading.this);
            }
        });

        binding.cvGuestList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityDashboardLeading.this, GuestListActivityLeading.class));
                AdsUtils.showInterstitial(MainActivityDashboardLeading.this);
            }
        });

        binding.cvBudgetList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityDashboardLeading.this, CostListActivityLeading.class));
                AdsUtils.showInterstitial(MainActivityDashboardLeading.this);
            }
        });
        binding.cvVendorList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityDashboardLeading.this, VendorListActivityLeading.class));
                AdsUtils.showInterstitial(MainActivityDashboardLeading.this);
            }
        });

        binding.navDrawer.llHome.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivityDashboardLeading.this, DashboardActivityLeading.class));

            }
        });
        binding.navDrawer.llChecklist.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivityDashboardLeading.this, TaskListActivityLeading.class));


            }
        });
        binding.navDrawer.llBudget.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivityDashboardLeading.this, CostListActivityLeading.class));

            }
        });
        binding.navDrawer.llGuest.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivityDashboardLeading.this, GuestListActivityLeading.class));

            }
        });
        binding.navDrawer.llVendor.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivityDashboardLeading.this, VendorListActivityLeading.class));

            }
        });

        binding.navDrawer.llProfle.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                openItemDetail(model.getArrayList().get(0), true);

            }
        });

        binding.navDrawer.llSettings.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                startActivityForResult(new Intent(MainActivityDashboardLeading.this, SettingActivityLeading.class), REQUEST_CODE);

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


    public void fillData() {
        try {
            this.model = new ProfileListModel();
            this.model.getArrayList().addAll(this.db.profileDao().getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void openItemDetail(ProfileRowModel profileRowModel, boolean z) {
        Intent intent = new Intent(this.context, AddEditProfileActivityLeading.class);
        intent.putExtra(AddEditProfileActivityLeading.EXTRA_IS_EDIT, z);
        intent.putExtra(AddEditProfileActivityLeading.EXTRA_MODEL, profileRowModel);
        startActivity(intent);
    }



    public void setOnClicks() {

        binding.imgMenu.setOnClickListener(this);
//        binding.fabProfile.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_menu:
                hamMenu();
                return;

//            case R.id.fab_profile:
//                fabProfile();
//                return;

            default:
                return;
        }
    }

//    private void loadImageFromStorage(String path) {
//
//        try {
//            ContextWrapper cw = new ContextWrapper(context);
//
//            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
//            File mypath=new File(directory,"profile.jpg");
//            if (mypath.exists()){
//
//                binding.profileImage.setImageDrawable(Drawable.createFromPath(mypath.toString()));
//            }else {
//                binding.profileImage.setImageResource(R.drawable.splash);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void fabProfile() {
//        ImagePicker.with(this)
//                .crop()                    //Crop image(Optional), Check Customization for more option
//                .start(10);
//    }

//    private void saveToInternalStorage(Bitmap bitmapImage) {
//        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//        // path to /data/data/yourapp/app_data/imageDir
//        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
//        // Create imageDir
//        File mypath = new File(directory, "profile.jpg");
//
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(mypath);
//            // Use the compress method on the BitMap object to write image to the OutputStream
//            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            Log.d("TAG", "saveToInternalStorage: successfully");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (fos != null) {
//                    fos.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }



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
                    j = db.profileDao().getDetail(AppPrefLeading.getCurrentEvenId(context)).getDateTimeInMillis() - System.currentTimeMillis();
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
//        else if (requestCode == 10) {
//            Uri data;
//            if (intent != null) {
//                data = intent.getData();
//                binding.profileImage.setImageURI(data);
//                Bitmap bitmap;
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data);
//                    saveToInternalStorage(bitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }
    }

    private void setProfile() {
        try {
            binding.txtMarriageName.setText(db.profileDao().getDetail(AppPrefLeading.getCurrentEvenId(context)).getWeddingName());
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
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    public void LoadAd() {


        adView = AdsUtils.showBanner(this, binding.llAdds);
        AdsUtils.loadInterstitial(this);
    }

}
