package com.msint.weddingplanner.appBase.view;

import android.content.Intent;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.appPref.AppPref;
import com.msint.weddingplanner.appBase.baseClass.BaseActivityBinding;
import com.msint.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.msint.weddingplanner.appBase.roomsDB.AppDataBase;
import com.msint.weddingplanner.databinding.ActivityDashboardBinding;

public class DashboardActivity extends BaseActivityBinding {
    private ActivityDashboardBinding binding;

    /* renamed from: db */
    private AppDataBase f551db;
    private ToolbarModel toolbarModel;

    /* access modifiers changed from: protected */
    public void setBinding() {
        this.binding = (ActivityDashboardBinding) DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        this.f551db = AppDataBase.getAppDatabase(this);
    }

    /* access modifiers changed from: protected */
    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(getString(R.string.drawerTitleHome));
        this.binding.includedToolbar.setModel(this.toolbarModel);
    }

    /* access modifiers changed from: protected */
    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.cardTaskPending.setOnClickListener(this);
        this.binding.cardTaskCompleted.setOnClickListener(this);
        this.binding.cardGuestPending.setOnClickListener(this);
        this.binding.cardGuestCompleted.setOnClickListener(this);
        this.binding.cardCostPending.setOnClickListener(this);
        this.binding.cardCostCompleted.setOnClickListener(this);
        this.binding.cardVendorPending.setOnClickListener(this);
        this.binding.cardVendorCompleted.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardCostCompleted:
                openCost(false);
                return;
            case R.id.cardCostPending:
                openCost(true);
                return;
            case R.id.cardGuestCompleted:
                openGuest(false);
                return;
            case R.id.cardGuestPending:
                openGuest(true);
                return;
            case R.id.cardTaskCompleted:
                openTask(false);
                return;
            case R.id.cardTaskPending:
                openTask(true);
                return;
            case R.id.cardVendorCompleted:
                openVendor(false);
                return;
            case R.id.cardVendorPending:
                openVendor(true);
                return;
            case R.id.imgBack:
                onBackPressed();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void initMethods() {
        setTotals();
    }

    private void setTotals() {
        try {
            TextView textView = this.binding.txtTaskPending;
            textView.setText(this.f551db.taskDao().getAllCount(AppPref.getCurrentEvenId(this.context), 1) + "");
        } catch (Exception e) {
            this.binding.txtTaskPending.setText("0");
            e.printStackTrace();
        }
        try {
            TextView textView2 = this.binding.txtTaskCompleted;
            textView2.setText(this.f551db.taskDao().getAllCount(AppPref.getCurrentEvenId(this.context), 0) + "");
        } catch (Exception e2) {
            this.binding.txtTaskCompleted.setText("0");
            e2.printStackTrace();
        }
        try {
            TextView textView3 = this.binding.txtGuestPending;
            textView3.setText(this.f551db.guestDao().getInvitationSentCount(AppPref.getCurrentEvenId(this.context), 0) + "");
        } catch (Exception e3) {
            this.binding.txtGuestPending.setText("0");
            e3.printStackTrace();
        }
        try {
            TextView textView4 = this.binding.txtGuestCompleted;
            textView4.setText(this.f551db.guestDao().getInvitationSentCount(AppPref.getCurrentEvenId(this.context), 1) + "");
        } catch (Exception e4) {
            this.binding.txtGuestCompleted.setText("0");
            e4.printStackTrace();
        }
        try {
            TextView textView5 = this.binding.txtCostPending;
            textView5.setText(this.f551db.paymentDao().getAllCountPendingCost(AppPref.getCurrentEvenId(this.context), 1) + "");
        } catch (Exception e5) {
            this.binding.txtCostPending.setText("0");
            e5.printStackTrace();
        }
        try {
            TextView textView6 = this.binding.txtCostCompleted;
            textView6.setText(this.f551db.paymentDao().getAllCountPaidByTypeCost(AppPref.getCurrentEvenId(this.context), 1) + "");
        } catch (Exception e6) {
            this.binding.txtCostCompleted.setText("0");
            e6.printStackTrace();
        }
        try {
            TextView textView7 = this.binding.txtVendorPending;
            textView7.setText(this.f551db.paymentDao().getAllCountPendingVendor(AppPref.getCurrentEvenId(this.context), 2) + "");
        } catch (Exception e7) {
            this.binding.txtVendorPending.setText("0");
            e7.printStackTrace();
        }
        try {
            TextView textView8 = this.binding.txtVendorCompleted;
            textView8.setText(this.f551db.paymentDao().getAllCountPaidByTypeVendor(AppPref.getCurrentEvenId(this.context), 2) + "");
        } catch (Exception e8) {
            this.binding.txtVendorCompleted.setText("0");
            e8.printStackTrace();
        }
    }

    private void openTask(boolean z) {
        Intent intent = new Intent(this.context, TaskListActivity.class);
        intent.putExtra(TaskListActivity.EXTRA_IS_FILTER, true);
        intent.putExtra(TaskListActivity.EXTRA_IS_PENDING, z);
        intent.setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
        startActivityForResult(intent, 1002);
    }

    private void openGuest(boolean z) {
        Intent intent = new Intent(this.context, GuestListActivity.class);
        intent.putExtra(GuestListActivity.EXTRA_IS_FILTER, true);
        intent.putExtra(GuestListActivity.EXTRA_IS_PENDING, !z);
        intent.setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
        startActivityForResult(intent, 1002);
    }

    private void openCost(boolean z) {
        Intent intent = new Intent(this.context, CostListActivity.class);
        intent.putExtra(CostListActivity.EXTRA_IS_FILTER, true);
        intent.putExtra(CostListActivity.EXTRA_IS_PENDING, z);
        intent.setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
        startActivityForResult(intent, 1002);
    }

    private void openVendor(boolean z) {
        Intent intent = new Intent(this.context, VendorListActivity.class);
        intent.putExtra(VendorListActivity.EXTRA_IS_FILTER, true);
        intent.putExtra(VendorListActivity.EXTRA_IS_PENDING, z);
        intent.setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
        startActivityForResult(intent, 1002);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 1002) {
            setTotals();
        }
    }
}
