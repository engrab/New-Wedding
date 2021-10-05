package com.msint.weddingplanner.appBase.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.appPref.AppPref;
import com.msint.weddingplanner.databinding.AlertDialogPdfReportBinding;
import com.msint.weddingplanner.databinding.AlertDialogRestoreBinding;
import com.msint.weddingplanner.databinding.AlertDialogTwoButtonBinding;
import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import net.lingala.zip4j.util.InternalZipConstants;

public class AppConstants {
    public static void logDebug(Context context, String str, String str2) {
        if (AppPref.isEnableDebugToast(context)) {
            toastShort(context, "toastDebug ->> " + str + " : \nmsg ->> " + str2 + " : \ncontext ->> " + context.getClass().getSimpleName());
        } else if (AppPref.isEnableDebugLog(context)) {
            logDebug(str, "context -->> " + context.getClass().getSimpleName() + " msg -->> " + str2);
        } else {
            logDebug(str, str2);
        }
    }

    public static void logDebug(String str, String str2) {
        Log.d("logDebug -->> " + str, str2);
    }

    public static void toastShort(Context context, String str) {
        Toast.makeText(context, str, 0).show();
    }

    public static String getVersion(Context context) {
        String str;
        try {
            str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            str = "";
        }
        return String.valueOf("Version " + str);
    }

    public static void shareApp(Context context) {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.TEXT", "Wedding Planner & Organizer, Guest Checklists\n\nBest wedding planner for manage guest checklist, marriage countdown & tasks list.\n- Manage task & subtasks, set category and status\n- Add, Edit, Sort, Export and Filter Task Lists\n- Filters, sort and export Guest Lists\n- keep Track of budget, vendors and task-subtask.\n- Guest Summary clarifies total guest list and Invitation send status\n\nhttps://play.google.com/store/apps/details?id=" + context.getPackageName());
            context.startActivity(Intent.createChooser(intent, "Share via"));
        } catch (Exception e) {
            logDebug(context, "shareApp", e.toString());
        }
    }

    public static void showRattingDialog(final Context context, String str, String str2) {
        RatingDialog build = new RatingDialog.Builder(context).session(1).title(str).threshold(4.0f).icon(context.getResources().getDrawable(R.mipmap.ic_launcher)).titleTextColor(R.color.actionbar).negativeButtonText("Never").positiveButtonTextColor(R.color.actionbar).negativeButtonTextColor(R.color.actionbar).formTitle("Submit Feedback").formHint("Tell us where we can improve").formSubmitText("Submit").formCancelText("Cancel").ratingBarColor(R.color.ratingBarColor).ratingBarBackgroundColor(R.color.ratingBarBackgroundColor).playstoreUrl(str2).onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
            public void onFormSubmitted(String str) {
                AppConstants.emailUsFeedback(context, str);
                AppPref.setNeverShowRatting(context, true);
            }
        }).build();
        if (AppPref.isNeverShowRatting(context)) {
            toastShort(context, "Already Submitted");
        } else {
            build.show();
        }
    }

    public static void emailUsFeedback(Context context, String str) {
        try {
            String str2 = Build.MODEL;
            String str3 = Build.MANUFACTURER;
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("mailto:msdeveloper0291@gmail.com"));
            intent.putExtra("android.intent.extra.SUBJECT", "Your Suggestion - " + context.getString(R.string.app_name) + "(" + context.getPackageName() + ")");
            intent.putExtra("android.intent.extra.TEXT", str + "\n\nDevice Manufacturer : " + str3 + "\nDevice Model : " + str2 + "\nAndroid Version : " + Build.VERSION.RELEASE);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.d("", e.toString());
        }
    }

    public static void emailUs(Context context) {
        try {
            String str = Build.MODEL;
            String str2 = Build.MANUFACTURER;
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("mailto:msdeveloper0291@gmail.com"));
            intent.putExtra("android.intent.extra.SUBJECT", Constants.APP_EMAIL_SUBJECT);
            intent.putExtra("android.intent.extra.TEXT", "Kindly give us your precious feedback. \n If you have any query then let us know.\nTo : " + context.getString(R.string.app_name) + " \n\nDevice Manufacturer : " + str2 + "\nDevice Model : " + str + "\nAndroid Version : " + Build.VERSION.RELEASE);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.d("", e.toString());
        }
    }

    public static String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    public static void setEditTextSelection(final EditText editText) {
        if (editText != null) {
            editText.post(new Runnable() {
                public void run() {
                    editText.setSelection(editText.getText().length());
                }
            });
        }
    }

    public static boolean isNotEmpty(Context context, EditText editText, String str) {
        if (editText == null || str == null) {
            return false;
        }
        if (!editText.getText().toString().trim().isEmpty()) {
            return true;
        }
        requestFocusAndError(context, editText, str);
        return false;
    }

    public static void requestFocusAndError(Context context, EditText editText, String str) {
        editText.requestFocus();
        toastShort(context, str);
    }

    public static void hideKeyboard(Context context, View view) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getFormattedPrice(double d) {
        return d != 0.0d ? new DecimalFormat("#.00", DecimalFormatSymbols.getInstance(Locale.US)).format(d) : "0";
    }

    public static String getFormattedPriceForMinus(double d) {
        return d != 0.0d ? new DecimalFormat("#.00", DecimalFormatSymbols.getInstance(Locale.US)).format(d) : "0";
    }

    public static String getFormattedLong(long j) {
        StringBuilder sb;
        if (j > 0) {
            sb = new StringBuilder();
            sb.append(j < 10 ? "0" : "");
            sb.append(j);
        } else {
            sb = new StringBuilder();
            sb.append(j);
            sb.append("");
        }
        return sb.toString();
    }

    public static String getSearchableTextPattern(String str) {
        return "%" + str.toLowerCase() + "%";
    }

    public static long getCurrentDateInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static String getFormattedDate(long j, DateFormat dateFormat) {
        return dateFormat.format(new Date(j));
    }

    public static String getFormattedDate(long j, String str) {
        return new SimpleDateFormat(str).format(new Date(j));
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getResIdUsingCategoryType(String r2) {
        /*
            int r0 = r2.hashCode()
            r1 = 0
            switch(r0) {
                case -1890823218: goto L_0x0089;
                case -1585103185: goto L_0x007e;
                case -686877022: goto L_0x0073;
                case -201416998: goto L_0x0069;
                case -172982990: goto L_0x005f;
                case -39644758: goto L_0x0055;
                case 88715314: goto L_0x004a;
                case 650201872: goto L_0x0040;
                case 927605132: goto L_0x0035;
                case 1188539379: goto L_0x002b;
                case 1660756189: goto L_0x0020;
                case 1750257734: goto L_0x0015;
                case 1817211153: goto L_0x000a;
                default: goto L_0x0008;
            }
        L_0x0008:
            goto L_0x0094
        L_0x000a:
            java.lang.String r0 = "Attire & accessories"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0094
            r2 = 1
            goto L_0x0095
        L_0x0015:
            java.lang.String r0 = "Flower & decor"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0094
            r2 = 4
            goto L_0x0095
        L_0x0020:
            java.lang.String r0 = "All Category"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0094
            r2 = 0
            goto L_0x0095
        L_0x002b:
            java.lang.String r0 = "Photo & video"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0094
            r2 = 7
            goto L_0x0095
        L_0x0035:
            java.lang.String r0 = "Transportation"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0094
            r2 = 10
            goto L_0x0095
        L_0x0040:
            java.lang.String r0 = "Accessories"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0094
            r2 = 5
            goto L_0x0095
        L_0x004a:
            java.lang.String r0 = "Accommodation"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0094
            r2 = 11
            goto L_0x0095
        L_0x0055:
            java.lang.String r0 = "Jewelry"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0094
            r2 = 6
            goto L_0x0095
        L_0x005f:
            java.lang.String r0 = "Music & show"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0094
            r2 = 3
            goto L_0x0095
        L_0x0069:
            java.lang.String r0 = "Health & beauty"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0094
            r2 = 2
            goto L_0x0095
        L_0x0073:
            java.lang.String r0 = "Ceremony"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0094
            r2 = 8
            goto L_0x0095
        L_0x007e:
            java.lang.String r0 = "Reception"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0094
            r2 = 9
            goto L_0x0095
        L_0x0089:
            java.lang.String r0 = "Miscellaneous"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0094
            r2 = 12
            goto L_0x0095
        L_0x0094:
            r2 = -1
        L_0x0095:
            switch(r2) {
                case 0: goto L_0x00c9;
                case 1: goto L_0x00c5;
                case 2: goto L_0x00c1;
                case 3: goto L_0x00bd;
                case 4: goto L_0x00b9;
                case 5: goto L_0x00b5;
                case 6: goto L_0x00b1;
                case 7: goto L_0x00ad;
                case 8: goto L_0x00a9;
                case 9: goto L_0x00a5;
                case 10: goto L_0x00a1;
                case 11: goto L_0x009d;
                case 12: goto L_0x0099;
                default: goto L_0x0098;
            }
        L_0x0098:
            return r1
        L_0x0099:
            r2 = 2131165300(0x7f070074, float:1.7944813E38)
            return r2
        L_0x009d:
            r2 = 2131165293(0x7f07006d, float:1.79448E38)
            return r2
        L_0x00a1:
            r2 = 2131165304(0x7f070078, float:1.7944821E38)
            return r2
        L_0x00a5:
            r2 = 2131165303(0x7f070077, float:1.794482E38)
            return r2
        L_0x00a9:
            r2 = 2131165295(0x7f07006f, float:1.7944803E38)
            return r2
        L_0x00ad:
            r2 = 2131165302(0x7f070076, float:1.7944817E38)
            return r2
        L_0x00b1:
            r2 = 2131165299(0x7f070073, float:1.7944811E38)
            return r2
        L_0x00b5:
            r2 = 2131165292(0x7f07006c, float:1.7944797E38)
            return r2
        L_0x00b9:
            r2 = 2131165296(0x7f070070, float:1.7944805E38)
            return r2
        L_0x00bd:
            r2 = 2131165301(0x7f070075, float:1.7944815E38)
            return r2
        L_0x00c1:
            r2 = 2131165298(0x7f070072, float:1.794481E38)
            return r2
        L_0x00c5:
            r2 = 2131165294(0x7f07006e, float:1.7944801E38)
            return r2
        L_0x00c9:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.msint.weddingplanner.appBase.utils.AppConstants.getResIdUsingCategoryType(java.lang.String):int");
    }

    public static String getResourcePathWithPackage(Context context) {
        return "android.resource://" + context.getPackageName() + InternalZipConstants.ZIP_FILE_SEPARATOR;
    }

    public static void showTwoButtonDialog(Context context, String str, String str2, boolean z, boolean z2, String str3, String str4, final TwoButtonDialogListener twoButtonDialogListener) {
        int i = 0;
        AlertDialogTwoButtonBinding alertDialogTwoButtonBinding = (AlertDialogTwoButtonBinding) DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.alert_dialog_two_button, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(alertDialogTwoButtonBinding.getRoot());
        dialog.setCancelable(z);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        alertDialogTwoButtonBinding.txtTitle.setText(str);
        alertDialogTwoButtonBinding.txtDec.setText(Html.fromHtml(str2));
        TextView textView = alertDialogTwoButtonBinding.btnCancel;
        if (!z2) {
            i = 8;
        }
        textView.setVisibility(i);
        alertDialogTwoButtonBinding.btnCancel.setText(str4);
        alertDialogTwoButtonBinding.btnOk.setText(str3);
        alertDialogTwoButtonBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                twoButtonDialogListener.onCancel();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialogTwoButtonBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                twoButtonDialogListener.onOk();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showRestoreDialog(Context context, String str, String str2, boolean z, boolean z2, String str3, String str4, final TwoButtonDialogListener twoButtonDialogListener) {
        int i = 0;
        AlertDialogRestoreBinding alertDialogRestoreBinding = (AlertDialogRestoreBinding) DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.alert_dialog_restore, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(alertDialogRestoreBinding.getRoot());
        dialog.setCancelable(z);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        alertDialogRestoreBinding.txtTitle.setText(str);
        alertDialogRestoreBinding.txtDec.setText(Html.fromHtml(str2));
        TextView textView = alertDialogRestoreBinding.btnCancel;
        if (!z2) {
            i = 8;
        }
        textView.setVisibility(i);
        alertDialogRestoreBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialogRestoreBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                twoButtonDialogListener.onOk();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialogRestoreBinding.btnMerge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                twoButtonDialogListener.onCancel();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pdfReportDialog(Context context, final TwoButtonDialogListener twoButtonDialogListener) {
        AlertDialogPdfReportBinding alertDialogPdfReportBinding = (AlertDialogPdfReportBinding) DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.alert_dialog_pdf_report, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(alertDialogPdfReportBinding.getRoot());
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        alertDialogPdfReportBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialogPdfReportBinding.btnReports.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                twoButtonDialogListener.onCancel();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialogPdfReportBinding.btnExport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                twoButtonDialogListener.onOk();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFileSize(long j) {
        if (j <= 0) {
            return "0 byte";
        }
        double d = (double) j;
        int log10 = (int) (Math.log10(d) / Math.log10(1024.0d));
        StringBuilder sb = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.#");
        double pow = Math.pow(1024.0d, (double) log10);
        Double.isNaN(d);
        sb.append(decimalFormat.format(d / pow));
        sb.append(" ");
        sb.append(new String[]{"byte", "kb", "mb", "gb", "tb"}[log10]);
        return sb.toString();
    }

    public static void deleteTempFile(Context context) {
        try {
            File file = new File(getRootPath(context) + "/temp");
            File[] listFiles = file.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File delete : listFiles) {
                    delete.delete();
                }
            }
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRootPath(Context context) {
        return new File(context.getDatabasePath(Constants.APP_DB_NAME).getParent()).getParent();
    }

    public static String getPrefPath(Context context) {
        return new File(new File(context.getDatabasePath(Constants.APP_DB_NAME).getParent()).getParent(), "shared_prefs").getAbsolutePath();
    }

    public static String getLocalZipFilePath() {
        return getLocalFileDir() + File.separator + Constants.DB_BACKUP_FILE_NAME_PRE + "_" + getFormattedDate(System.currentTimeMillis(), (DateFormat) Constants.FILE_DATE_FORMAT) + ".zip";
    }

    public static String getLocalFileDir() {
        File file = new File(Environment.getExternalStorageDirectory() + InternalZipConstants.ZIP_FILE_SEPARATOR + Constants.DB_BACKUP_DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    public static String getTempFileDir(Context context) {
        File file = new File(getRootPath(context) + "/temp");
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }
}
