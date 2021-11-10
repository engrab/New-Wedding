package com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.example.weddingplanner.R;

public class AdConstants {
    public static String AD_BANNER = "ca-app-pub-3940256099942544/6300978111";
    public static String AD_FULL = "ca-app-pub-3940256099942544/1033173712";
    public static boolean isAdShown = false;
    public static boolean npaflag = false;
    public static String[] publisherIds = {"abcd"};

    private static native String strAdBanner();

    private static native String strAdFull();

    private static native String strPublisherId();

//    static {
//        System.loadLibrary("native-lib");
//    }

    public static void showPersonalizeDialog(boolean z, Context context, String str, String str2, String str3, String str4, AdMobTwoButtonDialogListener adMobTwoButtonDialogListener) {
        final AdMobTwoButtonDialogListener adMobTwoButtonDialogListener2 = adMobTwoButtonDialogListener;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_dialog_addmob);
        boolean z2 = false;
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        RadioButton radioButton = dialog.findViewById(R.id.radioPersonalized);
        RadioButton radioButton2 = dialog.findViewById(R.id.radioNonPersonalized);
        final RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        Button button = dialog.findViewById(R.id.btnOk);
        ((TextView) dialog.findViewById(R.id.txtTitle)).setText(str);
        ((TextView) dialog.findViewById(R.id.txtDesc1)).setText(str2);
        ((TextView) dialog.findViewById(R.id.txtDesc2)).setText(str3);
        ((TextView) dialog.findViewById(R.id.txtDesc3)).setText(str4);
        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                adMobTwoButtonDialogListener2.onCancel();
            }
        });
        if (!z) {
            Log.d(" consentStatus setti", "" + ConsentInformation.getInstance(context).getConsentStatus());
            radioButton2.setChecked(ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.NON_PERSONALIZED);
            if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.PERSONALIZED) {
                z2 = true;
            }
            radioButton.setChecked(z2);
        }
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                adMobTwoButtonDialogListener2.onOk(radioGroup.getCheckedRadioButtonId() == R.id.radioPersonalized);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void setnpa(Context context) {
        if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.PERSONALIZED) {
            npaflag = false;
        }
        if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.NON_PERSONALIZED) {
            npaflag = true;
        }
        Log.d("consentStatus setting", "" + ConsentInformation.getInstance(context).getConsentStatus());
    }
}
