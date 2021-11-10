package com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils;

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
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.R;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.appPref.AppPrefLeading;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.databinding.AlertDialogRestoreBinding;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.databinding.AlertDialogTwoButtonBinding;

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
        if (AppPrefLeading.isEnableDebugToast(context)) {
            toastShort(context, "toastDebug ->> " + str + " : \nmsg ->> " + str2 + " : \ncontext ->> " + context.getClass().getSimpleName());
        } else if (AppPrefLeading.isEnableDebugLog(context)) {
            logDebug(str, "context -->> " + context.getClass().getSimpleName() + " msg -->> " + str2);
        } else {
            logDebug(str, str2);
        }
    }

    public static void logDebug(String str, String str2) {
        Log.d("logDebug -->> " + str, str2);
    }

    public static void toastShort(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
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
                AppPrefLeading.setNeverShowRatting(context, true);
            }
        }).build();
        if (AppPrefLeading.isNeverShowRatting(context)) {
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
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    public static int getResIdUsingCategoryType(String str) {
        char c;
        switch (str.hashCode()) {
            case -1890823218:
                if (str.equals(Constants.COST_CAT_TYPE_MISCELLANEOUS)) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case -1585103185:
                if (str.equals(Constants.COST_CAT_TYPE_RECEPTION)) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -686877022:
                if (str.equals(Constants.COST_CAT_TYPE_CEREMONY)) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -201416998:
                if (str.equals(Constants.COST_CAT_TYPE_HEALTH_BEAUTY)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -172982990:
                if (str.equals(Constants.COST_CAT_TYPE_MUSIC_SHOW)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -39644758:
                if (str.equals(Constants.COST_CAT_TYPE_JEWELRY)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 88715314:
                if (str.equals(Constants.COST_CAT_TYPE_ACCOMMODATION)) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 650201872:
                if (str.equals(Constants.COST_CAT_TYPE_ACCESSORIES)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 927605132:
                if (str.equals(Constants.COST_CAT_TYPE_TRANSPORTATION)) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 1188539379:
                if (str.equals(Constants.COST_CAT_TYPE_PHOTO_VIDEO)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 1660756189:
                if (str.equals(Constants.COST_CAT_TYPE_ALL_CATEGORY)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 1750257734:
                if (str.equals(Constants.COST_CAT_TYPE_FLOWER_DECOR)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 1817211153:
                if (str.equals(Constants.COST_CAT_TYPE_ATTIRE_ACCESSORIES)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return 0;
            case 1:
                return R.drawable.cat_attire_accessories;
            case 2:
                return R.drawable.cat_health_beauty;
            case 3:
                return R.drawable.cat_music;
            case 4:
                return R.drawable.cat_flower;
            case 5:
                return R.drawable.cat_accessories;
            case 6:
                return R.drawable.cat_jewelry;
            case 7:
                return R.drawable.cat_photo_video;
            case '\b':
                return R.drawable.cat_ceremony;
            case '\t':
                return R.drawable.cat_reception;
            case '\n':
                return R.drawable.cat_transportation;
            case 11:
                return R.drawable.cat_accommodation;
            case '\f':
                return R.drawable.cat_miscellaneous;
            default:
                return 0;
        }
    }
    public static String getResourcePathWithPackage(Context context) {
        return "android.resource://" + context.getPackageName() + InternalZipConstants.ZIP_FILE_SEPARATOR;
    }

    public static void showTwoButtonDialog(Context context, String str, String str2, boolean z, boolean z2, String str3, String str4, final TwoButtonDialogListener twoButtonDialogListener) {
        int i = 0;
        AlertDialogTwoButtonBinding alertDialogTwoButtonBinding = AlertDialogTwoButtonBinding.inflate(LayoutInflater.from(context), (ViewGroup) null, false);
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
        AlertDialogRestoreBinding alertDialogRestoreBinding = AlertDialogRestoreBinding.inflate(LayoutInflater.from(context),  (ViewGroup) null, false);
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

//    public static void pdfReportDialog(Context context, final TwoButtonDialogListener twoButtonDialogListener) {
//        AlertDialogPdfReportBinding alertDialogPdfReportBinding = AlertDialogPdfReportBinding.inflate(LayoutInflater.from(context),  (ViewGroup) null, false);
//        final Dialog dialog = new Dialog(context);
//        dialog.setContentView(alertDialogPdfReportBinding.getRoot());
//        dialog.setCancelable(true);
//        dialog.getWindow().setBackgroundDrawableResource(17170445);
//        alertDialogPdfReportBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                try {
//                    dialog.dismiss();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        alertDialogPdfReportBinding.btnReports.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                twoButtonDialogListener.onCancel();
//                try {
//                    dialog.dismiss();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        alertDialogPdfReportBinding.btnExport.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                twoButtonDialogListener.onOk();
//                try {
//                    dialog.dismiss();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        try {
//            if (!dialog.isShowing()) {
//                dialog.show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
