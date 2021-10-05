package com.msint.weddingplanner.appBase.utils;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.appPref.AppPref;
import com.msint.weddingplanner.appBase.view.AddEditProfileActivity;
import com.msint.weddingplanner.appBase.view.MainActivityDashboard;

public class WeddingDisclosure extends AppCompatActivity implements View.OnClickListener {
    public static String strPrivacyUri = "https://www.google.com/";
    public static String strTermsUri = "https://www.google.com/";


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_disclosure);
    }

    public void agreeAndContinueDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We would like to inform you regarding the 'Consent to Collection and Use Of Data'\n\nTo backup and restore wedding data into local phone, allow access to storage permission.\n\nRead Contacts permission, you can select guest contact to insert details.\n\nWe store your data on your device only, we don’t store them on our server.");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    public void uriparse(String str) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.addFlags(1208483840);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(strPrivacyUri)));
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.agreeAndContinue) {
            AppPref.setIsTermsAccept(this, true);
            GoToMainScreen();
        } else if (id == R.id.privacyPolicy) {
            uriparse(strPrivacyUri);
        } else if (id == R.id.termsOfService) {
            uriparse(strTermsUri);
        } else if (id == R.id.userAgreement) {
            agreeAndContinueDialog();
        }
    }

    private void GoToMainScreen() {
        try {
            if (!AppPref.isFirstLaunch(this)) {
                startActivity(new Intent(this, MainActivityDashboard.class));
            } else {
                startActivity(new Intent(this, AddEditProfileActivity.class));
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
