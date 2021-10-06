package com.example.weddingplanner.ui.model;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.load.Key;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.example.weddingplanner.BuildConfig;
import com.example.weddingplanner.R;
import com.example.weddingplanner.appBase.MyApp;
import com.example.weddingplanner.ui.SplashActivity;
import com.example.weddingplanner.ui.currency.Currency;
import com.example.weddingplanner.ui.param.Params;
import com.example.weddingplanner.ui.pref.MyPref;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import pub.devrel.easypermissions.EasyPermissions;

public class ConstantData {
    public static final String APP_EMAIL_ID = "abcd@gmail.com";
    public static final String APP_PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=";
    public static final int BUDGET_LIMIT = 20;
    public static final String DISCLAIMER_TEXT = "We would like to inform you regarding the 'Consent to Collection and Use Of Data'\n\nBelow Android Version 10, please grant permission to access your STORAGE to export PDF report.\n\nTo add guest or vendor from contacts, please grant permission to read your CONTACTS.\n\nTo scan collaborator's QR code, please grant permission to access your CAMERA.\n\nWe assure you that we are not sharing any of your wedding's data with any third party. All your data are stored as encrypted.";
    public static final String DOWNLOAD_DIRECTORY = "WeddingReports";
    public static final int GUEST_LIMIT = 50;
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1020;
    public static final String PRIVACY_POLICY_URL = "https://www.google.com/";
    public static final String RATTING_BAR_TITLE = "Support us by giving rate and your precious review !!\nIt will take few seconds only.";
    public static final int REQUEST_CODE_SIGN_IN = 1038;
    public static final int REQUEST_FOR_CALCULATON = 1022;
    public static final int REQUEST_FOR_CATEGORY = 1007;
    public static final int REQUEST_FOR_CATEGORY_SELECTION = 1025;
    public static final int REQUEST_FOR_COLLABORATORS_STATUS = 1032;
    public static final int REQUEST_FOR_CONTACT_CODE = 1021;
    public static final int REQUEST_FOR_COST_CATEGORY = 1010;
    public static final int REQUEST_FOR_CREATEWEDDING = 1031;
    public static final int REQUEST_FOR_CURRENCY = 1002;
    public static final int REQUEST_FOR_EDIT_BUDGET = 1012;
    public static final int REQUEST_FOR_EDIT_CATEGORY = 1009;
    public static final int REQUEST_FOR_EDIT_GROUP = 1018;
    public static final int REQUEST_FOR_EDIT_GUEST = 1016;
    public static final int REQUEST_FOR_EDIT_SUBTASK = 1006;
    public static final int REQUEST_FOR_EDIT_TASK = 1004;
    public static final int REQUEST_FOR_EDIT_VENDOR = 1026;
    public static final int REQUEST_FOR_GROUP_SELECTION = 1019;
    public static final int REQUEST_FOR_GUESTLIST = 1017;
    public static final int REQUEST_FOR_JOINWEDDING = 1028;
    public static final int REQUEST_FOR_LOGIN = 1030;
    public static final int REQUEST_FOR_MY_WEDDINGS = 1029;
    public static final int REQUEST_FOR_NEW_BUDGET = 1011;
    public static final int REQUEST_FOR_NEW_CATEGORY = 1008;
    public static final int REQUEST_FOR_NEW_GUEST = 1015;
    public static final int REQUEST_FOR_NEW_TASK = 1003;
    public static final int REQUEST_FOR_NEW_VENDOR = 1024;
    public static final int REQUEST_FOR_NEXTACTIVITY = 1014;
    public static final int REQUEST_FOR_PAYMENT = 1013;
    public static final int REQUEST_FOR_QR_SCAN = 1033;
    public static final int REQUEST_FOR_SETTINGS = 1035;
    public static final int REQUEST_FOR_SETTINGS_WEDDING = 1037;
    public static final int REQUEST_FOR_SUBTASK = 1005;
    public static final int REQUEST_FOR_SWIP_GROUP = 1027;
    public static final int REQUEST_FOR_USERCOVEREDIT = 1036;
    public static final int REQUEST_FOR_USERPROFILEEDIT = 1034;
    public static final int REQUEST_FOR_VENDOR = 1023;
    public static final int REQUEST_PERM_FILE = 1001;
    public static final int TASKLIST_LIMIT = 50;
    public static final String TERMS_SERVICE_URL = "https://www.google.com/";
    public static final int VENDOR_LIMIT = 15;
    public static final int ZXING_CAMERA_PERMISSION = 1;
    public static Dialog dialog = null;
    public static final String password = "abc123@";
    public static final String password_Pwd = "abc@1234";
    //    public static final String password = strPassword();
//    public static final String password_Pwd = strPasswordPwd();
    public static String token = "123013201201";
    public static String token_Pwd = "120120120";
    //    public static String token = strToken();
//    public static String token_Pwd = strTokenPwd();
    public static ProgressBar progressBar = null;
    public static final String regex = "((http|https)://)(www.)?[a-zA-Z0-9@:%._\\+~#?&//=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*)";

    private static native String strPassword();

    private static native String strPasswordPwd();

    private static native String strToken();

    private static native String strTokenPwd();

    static {
//        System.loadLibrary("native-lib");
    }

    public static void showDialogRate(final Context context, boolean z) {
        RatingDialog.Builder ratingBarBackgroundColor = new RatingDialog.Builder(context).session(1).title(RATTING_BAR_TITLE).threshold(4.0f).icon(context.getResources().getDrawable(R.mipmap.ic_launcher)).titleTextColor(R.color.title_color).negativeButtonText("Never").feedbackTextColor(R.color.title_color).positiveButtonTextColor(R.color.white).negativeButtonTextColor(R.color.title_color).formTitle("Submit Feedback").formHint("Tell us where we can improve").formSubmitText("Submit").formCancelText("Cancel").ratingBarColor(R.color.ratingBarColor).ratingBarBackgroundColor(R.color.ratingBarBackgroundColor);
        RatingDialog build = ratingBarBackgroundColor.playstoreUrl(APP_PLAY_STORE_URL + context.getPackageName()).onRatingChanged(new RatingDialog.Builder.RatingDialogListener() {
            /* class com.selfmentor.myweddingplanner.Comman.ConstantData.AnonymousClass2 */

            @Override // com.codemybrainsout.ratingdialog.RatingDialog.Builder.RatingDialogListener
            public void onRatingSelected(float f, boolean z) {
                MyPref.setShowRateUs(true);
            }
        }).onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
            /* class com.selfmentor.myweddingplanner.Comman.ConstantData.AnonymousClass1 */

            @Override
            // com.codemybrainsout.ratingdialog.RatingDialog.Builder.RatingDialogFormListener
            public void onFormSubmitted(String str) {
                MyPref.setShowRateUs(true);
                MyPref.setShowNeverRateUs(true);
                ConstantData.EmailUs(context, str);
            }
        }).build();
        if (!MyPref.getShowNeverRateUs().booleanValue()) {
            build.show();
        } else if (z) {
            Toast.makeText(context, "Already Submitted", Toast.LENGTH_SHORT).show();
        }
    }

    public static void EmailUs(Context context, String str) {
        try {
            String str2 = Build.MODEL;
            String str3 = Build.MANUFACTURER;
            Intent intent = new Intent("android.intent.action.SENDTO");
            intent.setData(Uri.parse("mailto:"));
            Intent intent2 = new Intent("android.intent.action.SENDTO");
            intent2.putExtra("android.intent.extra.EMAIL", new String[]{APP_EMAIL_ID});
            intent2.putExtra("android.intent.extra.SUBJECT", context.getResources().getString(R.string.app_name) + "(" + context.getPackageName() + ")");
            intent2.putExtra("android.intent.extra.TEXT", str + "\n\nDevice Manufacturer : " + str3 + "\nDevice Model : " + str2 + "\nAndroid Version : " + Build.VERSION.RELEASE + "\nApp Version : " + BuildConfig.VERSION_NAME);
            intent2.setSelector(intent);
            context.startActivity(intent2);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    public enum GuestStatus {
        CONFIRMED("Confirmed"),
        PENDING(Params.PENDING),
        CANCELLED("Cancelled");

        private String stringValue;

        private GuestStatus(String str) {
            this.stringValue = str;
        }

        public String getStringValue() {
            return this.stringValue;
        }

        public void setStringValue(String str) {
            this.stringValue = str;
        }

        public String toString() {
            return this.stringValue;
        }
    }

    public static boolean isValidURL(String str) {
        Pattern compile = Pattern.compile(regex);
        if (str == null) {
            return false;
        }
        return compile.matcher(str).matches();
    }

    public enum EditAccessCollaborate {
        VIEW("View"),
        EDIT("Edit"),
        DENY("Deny");

        private String EditAccessCollaborate;

        private EditAccessCollaborate(String str) {
            this.EditAccessCollaborate = str;
        }

        public String getEditAccessCollaborate() {
            return this.EditAccessCollaborate;
        }

        public void setEditAccessCollaborate(String str) {
            this.EditAccessCollaborate = str;
        }

        public String toString() {
            return this.EditAccessCollaborate;
        }
    }

    public enum EditAccessCollaborateSettings {
        EDIT("Edit"),
        DENY("Deny");

        private String EditAccessCollaborateSettings;

        private EditAccessCollaborateSettings(String str) {
            this.EditAccessCollaborateSettings = str;
        }

        public String getEditAccessCollaborateSettings() {
            return this.EditAccessCollaborateSettings;
        }

        public void setEditAccessCollaborateSettings(String str) {
            this.EditAccessCollaborateSettings = str;
        }

        public String toString() {
            return this.EditAccessCollaborateSettings;
        }
    }

    public enum VendorStstus {
        ALL("all"),
        RESERVED("Reserved"),
        PENDING(Params.PENDING),
        REJECTED("Rejected");

        private String VendorStatus;

        private VendorStstus(String str) {
            this.VendorStatus = str;
        }

        public String getVendorStatus() {
            return this.VendorStatus;
        }

        public void setVendorStatus(String str) {
            this.VendorStatus = str;
        }

        public String toString() {
            return this.VendorStatus;
        }
    }

    public enum SubscriptionStstus {
        SKU_INAPP_FOREVER("inapp_forever"),
        SKU_SUB_MONTHLY("sub_monthly"),
        SKU_SUB_YEARLY("sub_yearly");

        private String SubscriptionStatus;

        private SubscriptionStstus(String str) {
            this.SubscriptionStatus = str;
        }

        public String getSubscriptionStatus() {
            return this.SubscriptionStatus;
        }

        public void setSubscriptionStatus(String str) {
            this.SubscriptionStatus = str;
        }

        public String toString() {
            return this.SubscriptionStatus;
        }
    }

    public static String encryptMsg(String str, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher instance = Cipher.getInstance("AES/ECB/PKCS5Padding");
        instance.init(1, secretKey);
        return Base64.encodeToString(instance.doFinal(str.getBytes(Key.STRING_CHARSET_NAME)), 0);
    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new SecretKeySpec(password.getBytes(), "AES");
    }

    public static SecretKey generateKeyPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new SecretKeySpec(password_Pwd.getBytes(), "AES");
    }

    public static String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    public static void toastShort(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static boolean isNetworkAvailable1(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (activeNetworkInfo == null || (activeNetworkInfo.getType() != 1 && activeNetworkInfo.getType() != 0)) {
            return false;
        }
        return true;
    }

    public static File profilePicStoreParent(Context context) {
        String mediaDir = getMediaDir(context);
        return new File(mediaDir, System.currentTimeMillis() + ".jpg");
    }

    public static String getMediaDir(Context context) {
        File file = new File(context.getFilesDir(), "ProfileImage");
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    public static Dialog DisplayProgressInitialize(Activity activity) {
        Dialog dialog2 = new Dialog(activity);
        dialog = dialog2;
        dialog2.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_progress);
        ((ProgressBar) dialog.findViewById(R.id.progressBar)).getIndeterminateDrawable().setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        return dialog;
    }

    public static void ShowProgress() {
        try {
            Dialog dialog2 = dialog;
            if (dialog2 != null && !dialog2.isShowing()) {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void HideProgress() {
        try {
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long DateToMilisecond(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        simpleDateFormat.setLenient(false);
        return simpleDateFormat.parse(str).getTime();
    }

    public static long StringDateToMilisecond(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setLenient(false);
        return simpleDateFormat.parse(str).getTime();
    }

    public static long DatetimeToMilisecond(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        simpleDateFormat.setLenient(false);
        if (!str.equals(", ")) {
            return simpleDateFormat.parse(str).getTime();
        }
        return 0;
    }

    public static long DatetimeToMilisecond2(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM, dd yyyy HH:mm aa");
        simpleDateFormat.setLenient(false);
        return simpleDateFormat.parse(str).getTime();
    }

    public static String getLongToStringDateTime(Long l) {
        if (l == null) {
            l = Long.valueOf(System.currentTimeMillis());
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(l);
    }

    public static String getLongToStringDate(Long l) {
        if (l == null) {
            l = Long.valueOf(System.currentTimeMillis());
        }
        return new SimpleDateFormat(MyPref.getSelectedFormat()).format(l);
    }

    public static String getLongToStringDatewithAMPM(Long l) {
        if (l == null) {
            l = Long.valueOf(System.currentTimeMillis());
        }
        return new SimpleDateFormat(MyPref.getSelectedFormat() + " HH:mm aa").format(l);
    }

    public static String getLongToStringCalenderDate(Long l) {
        if (l == null) {
            l = Long.valueOf(System.currentTimeMillis());
        }
        return new SimpleDateFormat("dd.MM.yyyy").format(l);
    }

    public static String getLongToStringDateTimewithAMPM(Long l) {
        if (l == null) {
            l = Long.valueOf(System.currentTimeMillis());
        }
        return new SimpleDateFormat(MyPref.getSelectedFormat() + " HH:mm aa").format(l);
    }

    public static String getLongToStringDayDateTimewithAMPM(Long l) {
        if (l == null) {
            l = Long.valueOf(System.currentTimeMillis());
        }
        return new SimpleDateFormat("EEEE, MMM, dd yyyy").format(l);
    }

    public static String getLongToStringDayDateTimeAMPM(Long l) {
        if (l == null) {
            l = Long.valueOf(System.currentTimeMillis());
        }
        return new SimpleDateFormat("MMM, dd yyyy").format(l);
    }

    public static String getLongToStringTimeAMPM(Long l) {
        if (l == null) {
            l = Long.valueOf(System.currentTimeMillis());
        }
        return new SimpleDateFormat("hh:mm aa").format(l);
    }

    public static String SelectedDateFormate(Long l) {
        if (l == null) {
            l = Long.valueOf(System.currentTimeMillis());
        }
        return new SimpleDateFormat(MyPref.getSelectedFormat()).format(l);
    }

    public static long getcurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getFormatedAmountValue(double d) {
        return new DecimalFormat("#,###,###.##").format(d);
    }

    public static String getFormatedInteger(double d) {
        return new DecimalFormat("#####").format(d);
    }

    public static String getFormatedPercentValue(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(d);
    }

    public static String getFormatedAmount(Context context, double d) {
        return "$" + " " + NumberFormat.getNumberInstance(Locale.US).format(d);
    }

    public static String getCurrencySymbol(String str) {
        CurrencyModel currencyModel = Currency.CurrencyList().get(Currency.CurrencyList().indexOf(new CurrencyModel(str)));
        if (currencyModel == null) {
            return "$";
        }
        return currencyModel.getCurrencySymbol();
    }

    public static Typeface getRegulerFontFamily(Context context) {
        return Typeface.createFromAsset(context.getResources().getAssets(), "font/firasans_regular.ttf");
//        return ResourcesCompat.getFont(context, R.font.firasans_regular);
    }

    public static Typeface getSemiboldFontFamily(Context context) {
        return ResourcesCompat.getFont(context, R.font.firasans_semibold);
    }

    public static Typeface getMediamFontFamily(Context context) {
        return ResourcesCompat.getFont(context, R.font.firasans_medium);
    }

    public static Typeface getBoldFontFamily(Context context) {
        return ResourcesCompat.getFont(context, R.font.firaaans_bold);
    }

    public static Typeface getScriptBoldFontFamily(Context context) {
        return ResourcesCompat.getFont(context, R.font.scriptbl);
    }

    public static String getCachePath(Context context) {
        File file = new File(context.getCacheDir(), "temp");
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }



    public static void createWebPrintJob(Activity activity, WebView webView, View view, String str) {
        try {
//            new PdfPrint(new PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A4).setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600)).setMinMargins(PrintAttributes.Margins.NO_MARGINS).build()).print(activity, webView.createPrintDocumentAdapter(activity.getString(R.string.app_name) + " Document"), PdfPrint.getFileUri(activity, str), view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSnackbar(View view, String str, int i) {
        final Snackbar make = Snackbar.make(view, str, i);
        make.setAction("DISMISS", new View.OnClickListener() {
            /* class com.selfmentor.myweddingplanner.Comman.ConstantData.AnonymousClass3 */

            public void onClick(View view) {
                make.dismiss();
            }
        });
        make.show();
    }

    public static String convertToHtmlString(Context context, String str) {
        InputStream inputStream;
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            inputStream = context.getAssets().open(str);
        } catch (IOException e) {
            e.printStackTrace();
            inputStream = null;
        }
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Key.STRING_CHARSET_NAME));
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine);
                }
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            break;
        }
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getPublicPDFRootPath() {
        if (Build.VERSION.SDK_INT >= 29) {
            return Environment.DIRECTORY_DOWNLOADS + File.separator + DOWNLOAD_DIRECTORY;
        }
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), DOWNLOAD_DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    public static boolean isFileExists(Context context, String str, String str2) {
        if (Build.VERSION.SDK_INT < 29) {
            return new File(str, str2).exists();
        }
        Cursor query = MyApp.getInstance().getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_id"}, "relative_path like ? and lower(_display_name) = lower(?)", new String[]{"%" + str + "/%", str2}, null);
        if (query == null || query.getCount() <= 0) {
            return false;
        }
        query.close();
        return true;
    }

    public static boolean isHasPermissions(Context context, String... strArr) {
        return EasyPermissions.hasPermissions(context, strArr);
    }

    public static void requestPermissions(Context context, String str, int i, String... strArr) {
        EasyPermissions.requestPermissions((Activity) context, str, i, strArr);
    }


}
