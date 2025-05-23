package com.msint.weddingplanner.appBase.view;


import android.view.View;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.appPref.AppPref;
import com.msint.weddingplanner.appBase.baseClass.BaseActivityBinding;
import com.msint.weddingplanner.appBase.models.guest.GuestSummaryModel;
import com.msint.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.msint.weddingplanner.appBase.roomsDB.AppDataBase;
import com.msint.weddingplanner.databinding.ActivityGuestSummaryBinding;

public class GuestSummaryActivity extends BaseActivityBinding {
    private ActivityGuestSummaryBinding binding;

    /* renamed from: db */
    private AppDataBase f553db;
    private GuestSummaryModel model;
    public ToolbarModel toolbarModel;

    /* access modifiers changed from: protected */
    public void setBinding() {
        this.binding = (ActivityGuestSummaryBinding) DataBindingUtil.setContentView(this, R.layout.activity_guest_summary);
        this.f553db = AppDataBase.getAppDatabase(this);
        setModelDetail();
        this.binding.setRowModel(this.model);
    }

    private void setModelDetail() {
        this.model = new GuestSummaryModel();
    }

    /* access modifiers changed from: protected */
    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle("Guest Summary");
        this.binding.includedToolbar.setModel(this.toolbarModel);
    }

    /* access modifiers changed from: protected */
    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.imgBack) {
            onBackPressed();
        }
    }

    public void onBackPressed() {
        MainActivityDashboard.BackPressedAd(this);
    }

    /* access modifiers changed from: protected */
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
