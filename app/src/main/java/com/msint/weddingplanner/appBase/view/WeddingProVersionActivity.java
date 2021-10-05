package com.msint.weddingplanner.appBase.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.annotation.Nullable;
import android.support.p004v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.appPref.AppPref;
import com.msint.weddingplanner.appBase.baseClass.BaseActivityBinding;
import com.msint.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.msint.weddingplanner.databinding.ActivityWeddingProVersionBinding;
import java.util.ArrayList;
import java.util.List;

public class WeddingProVersionActivity extends BaseActivityBinding implements PurchasesUpdatedListener {
    static final String TAG = "INAPP";

    public ActivityWeddingProVersionBinding binding;
    boolean isServiceConnected;
    private BillingClient mBillingClient;
    boolean mIsPremium = false;
    Activity mactivity;
    SkuDetails skuDetail;
    String skuID = "wedding_planner_adsfree";
    public ToolbarModel toolbarModel;

    /* access modifiers changed from: protected */
    public void setBinding() {
        this.binding = (ActivityWeddingProVersionBinding) DataBindingUtil.setContentView(this, R.layout.activity_wedding_pro_version);
        this.mactivity = this;
    }

    /* access modifiers changed from: protected */
    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(getString(R.string.drawerTitleProVersion));
        this.binding.includedToolbar.setModel(this.toolbarModel);
    }

    /* access modifiers changed from: protected */
    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.cardBuyPlan.setOnClickListener(this);
    }

    /* access modifiers changed from: protected */
    public void initMethods() {
        try {
            if (this.mactivity != null) {
                this.mBillingClient = BillingClient.newBuilder(this.mactivity).setListener(this).build();
                if (!AppPref.getIsAdfree(this)) {
                    setUpBilling();
                } else {
                    changeAlreadyOwnItem();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cardBuyPlan) {
            buyPlan();
        } else if (id == R.id.imgBack) {
            onBackPressed();
        }
    }

    public void changeAlreadyOwnItem() {
        try {
            this.binding.textDesc.setText("The item is purchased. (Ads Free)");
            this.binding.cardPrice.setVisibility(View.GONE);
            this.binding.cardBuyPlan.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpBilling() {
        this.mBillingClient.startConnection(new BillingClientStateListener() {
            public void onBillingSetupFinished(int i) {
                if (i == 0) {
                    WeddingProVersionActivity.this.isServiceConnected = true;
                    Log.e(WeddingProVersionActivity.TAG, "isServiceConnected == " + WeddingProVersionActivity.this.isServiceConnected);
                    WeddingProVersionActivity.this.OkBillingProcess();
                }
            }

            public void onBillingServiceDisconnected() {
                Log.e(WeddingProVersionActivity.TAG, "isServiceConnected == " + WeddingProVersionActivity.this.isServiceConnected);
                WeddingProVersionActivity.this.isServiceConnected = false;
            }
        });
    }

    public void OkBillingProcess() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.skuID);
        SkuDetailsParams.Builder newBuilder = SkuDetailsParams.newBuilder();
        newBuilder.setSkusList(arrayList).setType(BillingClient.SkuType.INAPP);
        this.mBillingClient.querySkuDetailsAsync(newBuilder.build(), new SkuDetailsResponseListener() {
            public void onSkuDetailsResponse(int i, List<SkuDetails> list) {
                if (i == 0 && list != null) {
                    for (SkuDetails next : list) {
                        String sku = next.getSku();
                        String price = next.getPrice();
                        if (WeddingProVersionActivity.this.skuID.equals(sku) && WeddingProVersionActivity.this.binding.textPrice != null) {
                            try {
                                WeddingProVersionActivity.this.skuDetail = next;
                                WeddingProVersionActivity.this.binding.textPrice.setText(price);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    private void buyPlan() {
        if (this.isServiceConnected) {
            try {
                if (this.skuDetail != null) {
                    int launchBillingFlow = this.mBillingClient.launchBillingFlow(this.mactivity, BillingFlowParams.newBuilder().setSkuDetails(this.skuDetail).build());
                    Log.e("responseCode", launchBillingFlow + "==========>");
                    return;
                }
                setUpBilling();
                Toast.makeText(this.mactivity, "Server Error try once again..", 0).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setUpBilling();
            Toast.makeText(this.mactivity, "Server Error try once again..", 0).show();
        }
    }

    public void onPurchasesUpdated(int i, @Nullable List<Purchase> list) {
        if (i == 0 && list != null) {
            this.mIsPremium = true;
            AppPref.setIsAdfree(this.mactivity, this.mIsPremium);
            Log.e(TAG, "Purchased Ok");
            changeAlreadyOwnItem();
            restartapp("Purchased Successfully Done.");
        } else if (i == 1) {
            Log.e(TAG, "Purchased Canceled");
        } else if (i == 7) {
            this.mIsPremium = true;
            AppPref.setIsAdfree(this.mactivity, this.mIsPremium);
            changeAlreadyOwnItem();
            Log.e(TAG, "Purchased already item");
            restartapp("The item already purchased.");
        }
    }

    public void restartapp(String str) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage((CharSequence) str + " Restart Application for Pro Version!").setCancelable(false).setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent launchIntentForPackage = WeddingProVersionActivity.this.getBaseContext().getPackageManager().getLaunchIntentForPackage(WeddingProVersionActivity.this.getBaseContext().getPackageName());
                    launchIntentForPackage.addFlags(335577088);
                    WeddingProVersionActivity.this.startActivity(launchIntentForPackage);
                    WeddingProVersionActivity.this.finish();
                    System.exit(0);
                }
            });
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
