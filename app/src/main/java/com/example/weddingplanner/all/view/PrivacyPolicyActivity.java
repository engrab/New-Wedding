package com.example.weddingplanner.all.view;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.weddingplanner.R;
import com.example.weddingplanner.all.models.toolbar.ToolbarModel;
import com.example.weddingplanner.databinding.ActivityPrivacyPolicyBinding;

public class PrivacyPolicyActivity extends AppCompatActivity implements View.OnClickListener {
    
    public ActivityPrivacyPolicyBinding binding;
    private String strUrl = "";
    public ToolbarModel toolbarModel;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setToolbar();
        setOnClicks();
        initMethods();
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(getString(R.string.drawerTitlePrivacyPolicy));
//        this.binding.includedToolbar.setModel(this.toolbarModel);
    }

    private void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.imgBack) {
            onBackPressed();
        }
    }

    private void initMethods() {
        loadUrl();
    }

    public void loadUrl() {
        try {
            WebSettings settings = this.binding.privacyWebView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
            this.binding.progress.setVisibility(View.VISIBLE);
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            this.binding.privacyWebView.setOnKeyListener(new backInWB());
            this.binding.privacyWebView.setWebViewClient(new MyCustomizedWebClient());
            this.binding.privacyWebView.loadUrl(this.strUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyCustomizedWebClient extends WebViewClient {
        public void onReceivedHttpAuthRequest(WebView webView, HttpAuthHandler httpAuthHandler, String str, String str2) {
        }

        private MyCustomizedWebClient() {
        }

        public void onPageFinished(WebView webView, String str) {
            PrivacyPolicyActivity.this.binding.progress.setVisibility(View.GONE);
            super.onPageFinished(webView, str);
        }
    }

    class backInWB implements View.OnKeyListener {
        backInWB() {
        }

        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() != 0) {
                return false;
            }
            WebView webView = (WebView) view;
            if (i != 4 || !webView.canGoBack()) {
                return false;
            }
            webView.goBack();
            return true;
        }
    }

    public void onBackPressed() {
        try {
            if (this.binding.privacyWebView == null) {
                return;
            }
            if (this.binding.privacyWebView.canGoBack()) {
                this.binding.privacyWebView.goBack();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
