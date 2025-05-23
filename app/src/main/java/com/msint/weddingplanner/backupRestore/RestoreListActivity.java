package com.msint.weddingplanner.backupRestore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.support.annotation.NonNull;
import android.support.p004v7.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.baseClass.BaseActivityRecyclerBinding;
import com.msint.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.msint.weddingplanner.appBase.utils.AppConstants;
import com.msint.weddingplanner.appBase.utils.BackgroundAsync;
import com.msint.weddingplanner.appBase.utils.Constants;
import com.msint.weddingplanner.appBase.utils.OnAsyncBackground;
import com.msint.weddingplanner.appBase.utils.RecyclerItemClick;
import com.msint.weddingplanner.appBase.utils.TwoButtonDialogListener;
import com.msint.weddingplanner.databinding.ActivityRestoreListBinding;
import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.p008io.FilenameUtils;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class RestoreListActivity extends BaseActivityRecyclerBinding implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {

    public ActivityRestoreListBinding binding;

    public boolean isDesc;

    public boolean isResultOK;

    public LocalBackupRestore localBackupRestore;

    public RestoreListModel model;

    public BackupRestoreProgress progressDialog;
    private ToolbarModel toolbarModel;

    /* access modifiers changed from: protected */
    public void callApi() {
    }

    /* access modifiers changed from: protected */
    public void initMethods() {
    }

    public void onRationaleAccepted(int i) {
    }

    public void onRationaleDenied(int i) {
    }

    /* access modifiers changed from: protected */
    public void setBinding() {
        this.binding = (ActivityRestoreListBinding) DataBindingUtil.setContentView(this, R.layout.activity_restore_list);
        this.model = new RestoreListModel();
        this.model.setArrayList(new ArrayList());
        this.model.setNoDataIcon(R.drawable.dummy_empty);
        this.model.setNoDataText(getString(R.string.noDataTitleBackup));
        this.model.setNoDataDetail(getString(R.string.noDataDescBackup));
        this.binding.setModel(this.model);
        this.localBackupRestore = new LocalBackupRestore(this);
        this.progressDialog = new BackupRestoreProgress(this);
    }

    /* access modifiers changed from: protected */
    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle("Restore");
        this.toolbarModel.setAdd(true);
        this.binding.includedToolbar.setModel(this.toolbarModel);
    }

    /* access modifiers changed from: protected */
    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.includedToolbar.imgAdd.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgAdd) {
            this.isDesc = !this.isDesc;
            shortList();
        } else if (id == R.id.imgBack) {
            onBackPressed();
        }
    }

    /* access modifiers changed from: protected */
    public void fillData() {
        checkPermAndFill();
    }

    private void checkPermAndFill() {
        if (isHasPermissions(this, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")) {
            fillList();
            return;
        }
        requestPermissions(this, getString(R.string.rationale_save), Constants.REQUEST_PERM_FILE, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE");
    }

    private void fillList() {
        new BackgroundAsync(this, true, "", new OnAsyncBackground() {
            public void onPreExecute() {
            }

            public void doInBackground() {
                try {
                    File[] listFiles = new File(AppConstants.getLocalFileDir()).listFiles();
                    if (listFiles != null) {
                        for (int i = 0; i < listFiles.length; i++) {
                            if (FilenameUtils.getExtension(listFiles[i].getName()).equalsIgnoreCase("zip")) {
                                RestoreRowModel restoreRowModel = new RestoreRowModel();
                                restoreRowModel.setTitle(listFiles[i].getName());
                                restoreRowModel.setPath(listFiles[i].getAbsolutePath());
                                restoreRowModel.setDateModified(AppConstants.getFormattedDate(listFiles[i].lastModified(), (DateFormat) Constants.FILE_DATE_FORMAT));
                                restoreRowModel.setLastModifiedDate(listFiles[i].lastModified());
                                restoreRowModel.setSize((listFiles[i].length() / 1024) + "KB");
                                RestoreListActivity.this.model.getArrayList().add(restoreRowModel);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onPostExecute() {
                RestoreListActivity.this.shortList();
            }
        }).execute(new Object[0]);
    }


    public void shortList() {
        this.binding.includedToolbar.imgAdd.setImageResource(this.isDesc ? R.drawable.sort_down : R.drawable.sort_up);
        Collections.sort(this.model.getArrayList(), new Comparator<RestoreRowModel>() {
            @SuppressLint({"NewApi"})
            public int compare(RestoreRowModel restoreRowModel, RestoreRowModel restoreRowModel2) {
                if (RestoreListActivity.this.isDesc) {
                    return (restoreRowModel.getLastModifiedDate() > restoreRowModel2.getLastModifiedDate() ? 1 : (restoreRowModel.getLastModifiedDate() == restoreRowModel2.getLastModifiedDate() ? 0 : -1));
                }
                return (restoreRowModel2.getLastModifiedDate() > restoreRowModel.getLastModifiedDate() ? 1 : (restoreRowModel2.getLastModifiedDate() == restoreRowModel.getLastModifiedDate() ? 0 : -1));
            }
        });
        notifyAdapter();
    }

    /* access modifiers changed from: protected */
    public void setRecycler() {
        this.binding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.binding.recycler.setAdapter(new RestoreAdapter(this.context, this.model.getArrayList(), new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                if (i2 == 2) {
                    RestoreListActivity.this.deleteItem(i);
                } else {
                    RestoreListActivity.this.restoreItem(i);
                }
            }
        }));
        this.binding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                if (i2 > 0 && RestoreListActivity.this.binding.fabAdd.getVisibility() == 0) {
                    RestoreListActivity.this.binding.fabAdd.hide();
                } else if (i2 < 0 && RestoreListActivity.this.binding.fabAdd.getVisibility() != 0) {
                    RestoreListActivity.this.binding.fabAdd.show();
                }
            }
        });
    }

    private void notifyAdapter() {
        setViewVisibility();
        if (this.binding.recycler.getAdapter() != null) {
            this.binding.recycler.getAdapter().notifyDataSetChanged();
        }
    }

    private void setViewVisibility() {
        int i = 8;
        this.binding.linData.setVisibility(this.model.isListData() ? 0 : 8);
        LinearLayout linearLayout = this.binding.linNoData;
        if (!this.model.isListData()) {
            i = 0;
        }
        linearLayout.setVisibility(i);
    }

    public void deleteItem(final int i) {
        AppConstants.showTwoButtonDialog(this.context, getString(R.string.app_name), getString(R.string.delete_msg) + "<br /> <b>This Backup</b> <br />", true, true, getString(R.string.delete), getString(R.string.cancel), new TwoButtonDialogListener() {
            public void onCancel() {
            }

            public void onOk() {
                RestoreListActivity.this.deleteFile(i);
            }
        });
    }


    public void deleteFile(int i) {
        File file = new File(this.model.getArrayList().get(i).getPath());
        try {
            if (file.exists()) {
                if (file.delete()) {
                    this.model.getArrayList().remove(i);
                    this.binding.recycler.getAdapter().notifyItemRemoved(i);
                    Toast.makeText(this.context, "File deleted.", 0).show();
                } else {
                    Toast.makeText(this.context, "File can't be deleted.", 0).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyAdapter();
    }

    public void restoreItem(final int i) {
        AppConstants.showRestoreDialog(this.context, getString(R.string.app_name), "<b>" + getString(R.string.restore_msg) + "</b>", true, true, getString(R.string.restore), getString(R.string.cancel), new TwoButtonDialogListener() {
            public void onOk() {
                boolean unused = RestoreListActivity.this.isResultOK = true;
                RestoreListActivity.this.localBackupRestore.localBackUpAndRestore(RestoreListActivity.this.progressDialog, false, RestoreListActivity.this.model.getArrayList().get(i).getPath(), false);
            }

            public void onCancel() {
                boolean unused = RestoreListActivity.this.isResultOK = true;
                RestoreListActivity.this.localBackupRestore.localBackUpAndRestore(RestoreListActivity.this.progressDialog, false, RestoreListActivity.this.model.getArrayList().get(i).getPath(), true);
            }
        });
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
            fillList();
        }
    }

    public void onPermissionsDenied(int i, @NonNull List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) this, list)) {
            new AppSettingsDialog.Builder((Activity) this).build().show();
        }
    }

    public void onBackPressed() {
        if (this.isResultOK) {
            setResult(-1);
        }
        finish();
    }
}
