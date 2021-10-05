package com.msint.weddingplanner.backupRestore;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import androidx.sqlite.db.SupportSQLiteDatabase;

import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.appPref.AppPref;
import com.msint.weddingplanner.appBase.roomsDB.AppDataBase;
import com.msint.weddingplanner.appBase.utils.AppConstants;
import com.msint.weddingplanner.appBase.utils.Constants;
import java.io.File;
import java.util.ArrayList;

public class LocalBackupRestore {

    public Activity activity;

    public LocalBackupRestore(Activity activity2) {
        this.activity = activity2;
    }

    public void localBackUpAndRestore(BackupRestoreProgress backupRestoreProgress, boolean z, String str, boolean z2) {
        backupRestoreProgress.showDialog();
        AppConstants.deleteTempFile(this.activity);
        if (z) {
            startLocalBackUp(backupRestoreProgress);
        } else {
            startLocalRestore(backupRestoreProgress, str, z2);
        }
    }

    private void startLocalBackUp(final BackupRestoreProgress backupRestoreProgress) {
        BackupRestoreProgress backupRestoreProgress2 = backupRestoreProgress;
        new ZipUnZipAsyncTask(backupRestoreProgress2, this.activity, true, getAllFilesForBackup(AppConstants.getRootPath(this.activity)), "", AppConstants.getLocalZipFilePath(), new GetCompleteResponse() {
            public void getList(ArrayList<RestoreRowModel> arrayList) {
            }

            public void getResponse(boolean z) {
                backupRestoreProgress.dismissDialog();
                if (z) {
                    AppConstants.toastShort(LocalBackupRestore.this.activity, LocalBackupRestore.this.activity.getString(R.string.export_successfully));
                } else {
                    AppConstants.toastShort(LocalBackupRestore.this.activity, LocalBackupRestore.this.activity.getString(R.string.failed_to_export));
                }
            }
        }).execute(new Object[0]);
    }

    private ArrayList<File> getAllFilesForBackup(String str) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] listFiles = new File(str + "/databases").listFiles();
        if (listFiles != null && listFiles.length > 0) {
            for (File add : listFiles) {
                arrayList.add(add);
            }
        }
        return arrayList;
    }

    private void startLocalRestore(final BackupRestoreProgress backupRestoreProgress, String str, final boolean z) {
        new ZipUnZipAsyncTask(backupRestoreProgress, this.activity, false, (ArrayList<File>) null, str, "", new GetCompleteResponse() {
            public void getList(ArrayList<RestoreRowModel> arrayList) {
            }

            public void getResponse(boolean z) {
                try {
                    LocalBackupRestore.this.localRestore(z);
                } catch (Exception e) {
                    backupRestoreProgress.dismissDialog();
                    e.printStackTrace();
                }
                backupRestoreProgress.dismissDialog();
                if (z) {
                    Log.i("startLocalRestore", "getCurrentEvenId: " + AppPref.getCurrentEvenId(LocalBackupRestore.this.activity));
                    AppPref.setCurrentEvenId(LocalBackupRestore.this.activity, AppDataBase.getAppDatabase(LocalBackupRestore.this.activity).profileDao().getSelectedId());
                    Log.i("startLocalRestore", "getCurrentEvenId: " + AppPref.getCurrentEvenId(LocalBackupRestore.this.activity));
                    AppConstants.toastShort(LocalBackupRestore.this.activity, LocalBackupRestore.this.activity.getString(R.string.import_successfully));
                    return;
                }
                AppConstants.toastShort(LocalBackupRestore.this.activity, LocalBackupRestore.this.activity.getString(R.string.failed_to_import));
            }
        }).execute(new Object[0]);
    }


    public void localRestore(boolean z) {
        AppDataBase appDatabase = AppDataBase.getAppDatabase(this.activity);
        SupportSQLiteDatabase writableDatabase = appDatabase.getOpenHelper().getWritableDatabase();
        if (!z) {
            deleteAllTableData(appDatabase);
        }
        writableDatabase.execSQL(String.format("ATTACH DATABASE '%s' AS encrypted;", new Object[]{AppConstants.getTempFileDir(this.activity) + File.separator + Constants.APP_DB_NAME}));
        try {
            Cursor query = writableDatabase.query("SELECT MAX(versionNumber) FROM encrypted.dbVersionList");
            if (query != null) {
                query.moveToFirst();
                query.getInt(0);
            }
            query.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        replaceAllTableData(z, writableDatabase);
        writableDatabase.execSQL("DETACH DATABASE encrypted");
    }

    private void deleteAllTableData(AppDataBase appDataBase) {
        appDataBase.dbVersionDao().deleteAllCategory();
        appDataBase.dbVersionDao().deleteAllCost();
        appDataBase.dbVersionDao().deleteAllGuest();
        appDataBase.dbVersionDao().deleteAllPayment();
        appDataBase.dbVersionDao().deleteAllProfile();
        appDataBase.dbVersionDao().deleteAllSubTask();
        appDataBase.dbVersionDao().deleteAllTask();
        appDataBase.dbVersionDao().deleteAllVendor();
    }

    private void replaceAllTableData(boolean z, SupportSQLiteDatabase supportSQLiteDatabase) {
        if (z) {
            try {
                supportSQLiteDatabase.execSQL("update encrypted.profileList set isSelected =0");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        replaceAll(supportSQLiteDatabase, z, "categoryList");
        replaceAll(supportSQLiteDatabase, z, "costList");
        replaceAll(supportSQLiteDatabase, z, "guestList");
        replaceAll(supportSQLiteDatabase, z, "paymentList");
        replaceAll(supportSQLiteDatabase, z, "profileList");
        replaceAll(supportSQLiteDatabase, z, "subTaskList");
        replaceAll(supportSQLiteDatabase, z, "taskList");
        replaceAll(supportSQLiteDatabase, z, "vendorList");
    }

    private void replaceAll(SupportSQLiteDatabase supportSQLiteDatabase, boolean z, String str) {
        if (z) {
            try {
                supportSQLiteDatabase.execSQL("insert into " + str + " select b.* from encrypted." + str + " b  left join " + str + " c on c.id=b.id  where c.id is null ");
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("replaceAll", "replaceAll: " + e.toString());
            }
        } else {
            try {
                supportSQLiteDatabase.execSQL("insert into " + str + " select * from encrypted." + str);
            } catch (SQLException e2) {
                e2.printStackTrace();
                Log.e("replaceAll", "replaceAll: " + e2.toString());
            }
        }
    }
}
