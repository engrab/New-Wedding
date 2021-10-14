package com.example.weddingplanner.appBase.view;


import android.view.View;
import com.example.weddingplanner.R;
import com.example.weddingplanner.appBase.appPref.AppPref;
import com.example.weddingplanner.appBase.baseClass.BaseActivityBinding;
import com.example.weddingplanner.appBase.models.guest.GuestSummaryModel;
import com.example.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.example.weddingplanner.appBase.roomsDB.AppDataBase;
import com.example.weddingplanner.databinding.ActivityGuestSummaryBinding;

public class GuestSummaryActivity extends BaseActivityBinding {
    private ActivityGuestSummaryBinding binding;


    private AppDataBase f553db;
    private GuestSummaryModel model;
    public ToolbarModel toolbarModel;


    public void setBinding() {
        binding = ActivityGuestSummaryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        this.f553db = AppDataBase.getAppDatabase(this);
        setModelDetail();
//        this.binding.setRowModel(this.model);
    }

    private void setModelDetail() {
        this.model = new GuestSummaryModel();
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle("Guest Summary");
//        this.binding.includedToolbar.setModel(this.toolbarModel);
        binding.includedToolbar.textTitle.setText("Guest Summary");
        binding.includedToolbar.imgDelete.setVisibility(View.GONE);
        binding.includedToolbar.imgAdd.setVisibility(View.GONE);
        binding.includedToolbar.imageHome.setVisibility(View.GONE);
        binding.includedToolbar.imgOther.setVisibility(View.GONE);
        binding.includedToolbar.spinner.setVisibility(View.GONE);
        binding.includedToolbar.progressbar.setVisibility(View.GONE);
        binding.includedToolbar.etOther.setVisibility(View.GONE);
        binding.includedToolbar.search.setVisibility(View.GONE);
        binding.includedToolbar.imgShare.setVisibility(View.GONE);
    }


    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.imgBack) {
            onBackPressed();
        }
    }

    public void onBackPressed() {

        super.onBackPressed();
    }


    public void initMethods() {
        setTotals();
    }

    private void setTotals() {
        try {
            this.model.setTotalGuest(this.f553db.guestDao().getAllGuestCount(AppPref.getCurrentEvenId(this.context)));
            this.model.setSendInvitation(this.f553db.guestDao().getInvitationSentCount(AppPref.getCurrentEvenId(this.context), 1));
            this.model.setTotalCompanion(this.f553db.guestDao().getAllCompanionCount(AppPref.getCurrentEvenId(this.context)));
            this.model.setAdult(this.f553db.guestDao().getAllCompanionTypeCount(AppPref.getCurrentEvenId(this.context), 1));
            this.model.setChild(this.f553db.guestDao().getAllCompanionTypeCount(AppPref.getCurrentEvenId(this.context), 3));
            this.model.setBaby(this.f553db.guestDao().getAllCompanionTypeCount(AppPref.getCurrentEvenId(this.context), 2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
