package com.msint.weddingplanner.appBase.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class BackgroundAsync extends AsyncTask {
    private Context context;
    private String defaultDialogMessage = "Please wait ...";
    private ProgressDialog dialog;
    private String dialogMessage;
    private boolean isProgress;
    private OnAsyncBackground onAsyncBackground;

    public BackgroundAsync(Context context2, boolean z, String str, OnAsyncBackground onAsyncBackground2) {
        this.context = context2;
        this.isProgress = z;
        this.onAsyncBackground = onAsyncBackground2;
        this.dialogMessage = str;
        this.dialog = new ProgressDialog(context2);
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        if (this.isProgress) {
            try {
                if (this.dialog != null) {
                    this.dialog.setMessage((this.dialogMessage == null || this.dialogMessage.length() <= 0) ? this.defaultDialogMessage : this.dialogMessage);
                    this.dialog.setCancelable(false);
                    try {
                        this.dialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        this.onAsyncBackground.onPreExecute();
        super.onPreExecute();
    }

    /* access modifiers changed from: protected */
    public Object doInBackground(Object[] objArr) {
        this.onAsyncBackground.doInBackground();
        return 0;
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Object obj) {
        try {
            if (this.dialog != null && this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.onAsyncBackground.onPostExecute();
        super.onPostExecute(obj);
    }
}
