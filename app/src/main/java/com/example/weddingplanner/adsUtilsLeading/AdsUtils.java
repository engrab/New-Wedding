package com.example.weddingplanner.adsUtilsLeading;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.example.weddingplanner.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AdsUtils {

    static InterstitialAd mInterstitialAd;

    public static void loadInterstitial(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, context.getResources().getString(R.string.app_inters), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                mInterstitialAd = null;
            }
        });
    }

    public static void showInterstitial(final Context context) {
        if (mInterstitialAd != null) {
            mInterstitialAd.show((Activity) context);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    loadInterstitial(context);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    loadInterstitial(context);
                }
            });
        }
    }

    public static AdView showBanner(Context context, LinearLayout linearLayout) {
        AdView adView = new AdView(context);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(context.getResources().getString(R.string.app_banner));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        linearLayout.removeAllViews();
        linearLayout.addView(adView);
        return adView;
    }
    public static AdView ShowMediumBanner(Context context, LinearLayout linearLayout) {
        AdView adView = new AdView(context);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(context.getResources().getString(R.string.app_banner));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        linearLayout.removeAllViews();
        linearLayout.addView(adView);
        return adView;
    }


}
