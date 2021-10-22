package com.example.weddingplanner.allLeading.utils;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import com.example.weddingplanner.R;
import com.example.weddingplanner.allLeading.appPref.AppPrefLeading;
import com.example.weddingplanner.allLeading.view.AddEditProfileActivityLeading;
import com.example.weddingplanner.viewLeading.MainActivityDashboardLeading;

public class TermAndServiceActivity extends AppCompatActivity implements View.OnClickListener {
    public static String strPrivacyUri = "https://www.google.com/";
    public static String strTermsUri = "https://www.google.com/";


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_term_and_service);

        findViewById(R.id.tvPrivacyPolicy).setOnClickListener(this);
        findViewById(R.id.tvTerms).setOnClickListener(this);
        findViewById(R.id.tvAgreement).setOnClickListener(this);
        findViewById(R.id.addeptContinue).setOnClickListener(this);
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
        if (id == R.id.addeptContinue) {
            AppPrefLeading.setIsTermsAccept(this, true);
            GoToMainScreen();
        } else if (id == R.id.tvPrivacyPolicy) {
            uriparse(strPrivacyUri);
        } else if (id == R.id.tvTerms) {
            uriparse(strTermsUri);
        } else if (id == R.id.tvAgreement) {
            agreeAndContinueDialog();
        }
    }

    private void GoToMainScreen() {
        try {
            if (!AppPrefLeading.isFirstLaunch(this)) {
                startActivity(new Intent(this, MainActivityDashboardLeading.class));
            } else {
                startActivity(new Intent(this, AddEditProfileActivityLeading.class));
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
