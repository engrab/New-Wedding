package com.msint.weddingplanner.pdfRepo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.baseClass.BaseActivityRecyclerBinding;
import com.msint.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.msint.weddingplanner.appBase.utils.BackgroundAsync;
import com.msint.weddingplanner.appBase.utils.Constants;
import com.msint.weddingplanner.appBase.utils.OnAsyncBackground;
import com.msint.weddingplanner.appBase.utils.RecyclerItemClick;
import com.msint.weddingplanner.appBase.view.MainActivityDashboard;
import com.msint.weddingplanner.databinding.ActivityReportsListBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ReportsListActivity extends BaseActivityRecyclerBinding implements View.OnClickListener, EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    private ActivityReportsListBinding binding;

    public boolean isDesc;

    public ArrayList<File> list;
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
        this.binding = (ActivityReportsListBinding) DataBindingUtil.setContentView(this, R.layout.activity_reports_list);
        this.list = new ArrayList<>();
    }

    /* access modifiers changed from: protected */
    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(getString(R.string.pdf_reports));
        this.toolbarModel.setAdd(true);
        this.binding.includedToolbar.setModel(this.toolbarModel);
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
                    String str = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Constants.REPORT_DIRECTORY;
                    Log.d("Files", "Path: " + str);
                    File[] listFiles = new File(str).listFiles();
                    Log.d("Files", "Size: " + listFiles.length);
                    for (int i = 0; i < listFiles.length; i++) {
                        Log.d("Files", "FileName:" + listFiles[i].getName());
                        ReportsListActivity.this.list.add(listFiles[i]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onPostExecute() {
                ReportsListActivity.this.shortList();
            }
        }).execute(new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void setRecycler() {
        this.binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        this.binding.recycler.setAdapter(new ReportPdfAdapter(this.context, this.list, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                ReportsListActivity.this.setViewVisibility();
            }
        }));
    }

    private void notifyAdapter() {
        setViewVisibility();
        if (this.binding.recycler.getAdapter() != null) {
            this.binding.recycler.getAdapter().notifyDataSetChanged();
        }
    }


    public void setViewVisibility() {
        int i = 8;
        this.binding.linData.setVisibility(this.list.size() > 0 ? View.VISIBLE : View.GONE);
        LinearLayout linearLayout = this.binding.linNoData;
        if (this.list.size() <= 0) {
            i = 0;
        }
        linearLayout.setVisibility(i);
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

    public void onBackPressed() {
        MainActivityDashboard.BackPressedAd(this);
    }


    public void shortList() {
        this.binding.includedToolbar.imgAdd.setImageResource(this.isDesc ? R.drawable.sort_down : R.drawable.sort_up);
        Collections.sort(this.list, new Comparator<File>() {
            @SuppressLint({"NewApi"})
            public int compare(File file, File file2) {
                if (ReportsListActivity.this.isDesc) {
                    return (file.lastModified() > file2.lastModified() ? 1 : (file.lastModified() == file2.lastModified() ? 0 : -1));
                }
                return (file2.lastModified() > file.lastModified() ? 1 : (file2.lastModified() == file.lastModified() ? 0 : -1));
            }
        });
        notifyAdapter();
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

    public void onPermissionsGranted(int i, @NonNull List<String> list2) {
        if (i == 1053) {
            fillList();
        }
    }

    public void onPermissionsDenied(int i, @NonNull List<String> list2) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) this, list2)) {
            new AppSettingsDialog.Builder((Activity) this).build().show();
        }
    }
}
