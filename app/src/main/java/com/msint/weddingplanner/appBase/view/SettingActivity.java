package com.msint.weddingplanner.appBase.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.appPref.AppPref;
import com.msint.weddingplanner.appBase.baseClass.BaseActivityBinding;
import com.msint.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.msint.weddingplanner.appBase.utils.AdConstants;
import com.msint.weddingplanner.appBase.utils.AdMobTwoButtonDialogListener;
import com.msint.weddingplanner.appBase.utils.Constants;
import com.msint.weddingplanner.backupRestore.BackupRestoreProgress;
import com.msint.weddingplanner.backupRestore.BackupTransferGuidActivity;
import com.msint.weddingplanner.backupRestore.LocalBackupRestore;
import com.msint.weddingplanner.backupRestore.RestoreListActivity;
import com.msint.weddingplanner.databinding.ActivitySettingBinding;
import com.msint.weddingplanner.pdfRepo.ReportsListActivity;
import java.util.List;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class SettingActivity extends BaseActivityBinding implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    private LocalBackupRestore backupRestore;
    private ActivitySettingBinding binding;
    private boolean isUpdateProfile;
    private BackupRestoreProgress progressDialog;
    public ToolbarModel toolbarModel;

    public void onRationaleAccepted(int i) {
    }

    public void onRationaleDenied(int i) {
    }

    /* access modifiers changed from: protected */
    public void setBinding() {
        this.binding = (ActivitySettingBinding) DataBindingUtil.setContentView(this, R.layout.activity_setting);
        this.backupRestore = new LocalBackupRestore(this);
        this.progressDialog = new BackupRestoreProgress(this);
        if (!ConsentInformation.getInstance(this.context).isRequestLocationInEeaOrUnknown() || AppPref.getIsAdfree(this.context)) {
            this.binding.cardAdSettings.setVisibility(View.GONE);
        } else {
            this.binding.cardAdSettings.setVisibility(View.VISIBLE);
        }
    }

    /* access modifiers changed from: protected */
    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(getString(R.string.drawerTitleSetting));
        this.binding.includedToolbar.setModel(this.toolbarModel);
    }

    /* access modifiers changed from: protected */
    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.cardProfile.setOnClickListener(this);
        this.binding.cardCategory.setOnClickListener(this);
        this.binding.linTask.setOnClickListener(this);
        this.binding.linInvitation.setOnClickListener(this);
        this.binding.linPayment.setOnClickListener(this);
        this.binding.cardPdfReport.setOnClickListener(this);
        this.binding.cardLocalBackup.setOnClickListener(this);
        this.binding.cardRestoreBackups.setOnClickListener(this);
        this.binding.cardBackupTransferGuid.setOnClickListener(this);
        this.binding.cardAdSettings.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardAdSettings:
                showDialog();
                return;
            case R.id.cardBackupTransferGuid:
                startActivity(new Intent(this.context, BackupTransferGuidActivity.class).setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE));
                return;
            case R.id.cardCategory:
                startActivity(new Intent(this.context, CategoryListActivity.class).setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE));
                return;
            case R.id.cardLocalBackup:
                checkPermAndBackup();
                return;
            case R.id.cardPdfReport:
                startActivity(new Intent(this, ReportsListActivity.class).setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE));
                return;
            case R.id.cardProfile:
                startActivityForResult(new Intent(this.context, ProfileListActivity.class).setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE), 1002);
                return;
            case R.id.cardRestoreBackups:
                startActivityForResult(new Intent(this.context, RestoreListActivity.class).setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE), 1002);
                return;
            case R.id.imgBack:
                onBackPressed();
                return;
            case R.id.linInvitation:
                AppPref.setInvitationNotification(this.context, !AppPref.isInvitationNotification(this.context));
                setSwitchIcon(this.binding.imgInvitation, AppPref.isInvitationNotification(this.context));
                return;
            case R.id.linPayment:
                AppPref.setPaymentNotification(this.context, !AppPref.isPaymentNotification(this.context));
                setSwitchIcon(this.binding.imgPayment, AppPref.isPaymentNotification(this.context));
                return;
            case R.id.linTask:
                AppPref.setTaskNotification(this.context, !AppPref.isTaskNotification(this.context));
                setSwitchIcon(this.binding.imgTask, AppPref.isTaskNotification(this.context));
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

    private void showDialog() {
        AdConstants.showPersonalizeDialog(false, this, getString(R.string.app_name), getString(R.string.app_description1), getString(R.string.app_description2), getString(R.string.app_description3), new AdMobTwoButtonDialogListener() {
            public void onCancel() {
            }

            public void onOk(boolean z) {
                if (z) {
                    ConsentInformation.getInstance(SettingActivity.this.context).setConsentStatus(ConsentStatus.PERSONALIZED);
                } else {
                    ConsentInformation.getInstance(SettingActivity.this.context).setConsentStatus(ConsentStatus.NON_PERSONALIZED);
                }
                AdConstants.setnpa(SettingActivity.this);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void initMethods() {
        setSwitchIcon(this.binding.imgTask, AppPref.isTaskNotification(this.context));
        setSwitchIcon(this.binding.imgInvitation, AppPref.isInvitationNotification(this.context));
        setSwitchIcon(this.binding.imgPayment, AppPref.isPaymentNotification(this.context));
    }

    private void setSwitchIcon(ImageView imageView, boolean z) {
        imageView.setImageResource(z ? R.drawable.switch_on : R.drawable.switch_off);
    }

    /* access modifiers changed from: protected */
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
