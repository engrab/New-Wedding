package com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.viewLeading;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdView;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.adsUtilsLeading.AdsUtils;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.view.CategoryListActivityLeading;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.view.ProfileListActivityLeading;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.R;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.baseClass.BaseActivityBindingLeading;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.models.toolbar.ToolbarModel;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils.AdConstants;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils.AdMobTwoButtonDialogListener;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils.Constants;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.backupLeading.BackupRestoreProgress;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.backupLeading.LocalBackupRestore;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.databinding.ActivitySettingBinding;

import java.util.List;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class SettingActivityLeading extends BaseActivityBindingLeading implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    private LocalBackupRestore backupRestore;
    private ActivitySettingBinding binding;
    private boolean isUpdateProfile;
    private BackupRestoreProgress progressDialog;
    public ToolbarModel toolbarModel;

    AdView adView;

    public void onRationaleAccepted(int i) {
    }

    public void onRationaleDenied(int i) {
    }


    @Override
    public void initMethods() {

    }

    public void setBinding() {
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        this.backupRestore = new LocalBackupRestore(this);
        this.progressDialog = new BackupRestoreProgress(this);

        adView = AdsUtils.ShowMediumBanner(this, binding.llAdds);
    }

    @Override
    protected void onDestroy() {
        if (adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }

    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(getString(R.string.drawerTitleSetting));
//        this.binding.includedToolbar.setModel(this.toolbarModel);
        this.binding.includedToolbar.textTitle.setText("Setting");
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
        this.binding.cardProfile.setOnClickListener(this);
        this.binding.cardCategory.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.cardCategory:
                startActivity(new Intent(this.context, CategoryListActivityLeading.class));
                return;

            case R.id.cardProfile:
//                startActivityForResult(new Intent(this.context, ProfileListActivityLeading.class), 1002);
                startActivityForResult(new Intent(this.context, ProfileListActivityLeading.class), 1002);
                return;

            case R.id.imgBack:
                super.onBackPressed();
                return;
            default:
                return;
        }
    }

    private void checkPermAndBackup() {
        if (isHasPermissions(this, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")) {
            this.backupRestore.localBackUpAndRestore(this.progressDialog, true, "", false);
            return;
        }
        requestPermissions(this, getString(R.string.rationale_export), Constants.REQUEST_PERM_FILE, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE");
    }





    private void setSwitchIcon(ImageView imageView, boolean z) {
        imageView.setImageResource(z ? R.drawable.switch_on : R.drawable.switch_off);
    }


    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 1002) {
            this.isUpdateProfile = true;
        }
    }

    public void onBackPressed() {
        if (this.isUpdateProfile) {
            setResult(-1);
        }
        super.onBackPressed();
    }

    private boolean isHasPermissions(Context context, String... strArr) {
        return EasyPermissions.hasPermissions(context, strArr);
    }

    private void requestPermissions(Context context, String str, int i, String... strArr) {
        EasyPermissions.requestPermissions((Activity) context, str, i, strArr);
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
    }

    public void onPermissionsGranted(int i, @NonNull List<String> list) {
        if (i == 1053) {
            this.backupRestore.localBackUpAndRestore(this.progressDialog, true, "", false);
        }
    }

    public void onPermissionsDenied(int i, @NonNull List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) this, list)) {
            new AppSettingsDialog.Builder((Activity) this).build().show();
        }
    }
}
