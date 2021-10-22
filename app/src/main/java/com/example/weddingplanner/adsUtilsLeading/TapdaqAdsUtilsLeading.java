package com.example.weddingplanner.adsUtilsLeading;

import android.app.Activity;

import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.listeners.TMAdListener;

public class TapdaqAdsUtilsLeading {


    public static int AdsCounter = 0;

    public static void loadInterstitial(Activity context) {
        Tapdaq.getInstance().loadVideo(context, "default", new VideoListener());
        loadStaticInterstitial(context);
    }

    private static void loadStaticInterstitial(Activity activity) {
        Tapdaq.getInstance().loadInterstitial(activity, "default", new InterstitialListener());
    }

    public static void showInterstitial(Activity activity) {

        if (Tapdaq.getInstance().isInterstitialReady(activity, "default")) {
            Tapdaq.getInstance().showInterstitial(activity, "default", new InterstitialListener());
        } else if (Tapdaq.getInstance().isVideoReady(activity, "default")) {
            Tapdaq.getInstance().showVideo(activity, "default", new VideoListener());
        } else {
            loadInterstitial(activity);
        }
    }

    public static class VideoListener extends TMAdListener {

        @Override
        public void didLoad() {
            // Ready to display the interstitial

        }

    }

    public static class InterstitialListener extends TMAdListener {

        @Override
        public void didLoad() {
            // Ready to display the interstitial
        }

    }

}
