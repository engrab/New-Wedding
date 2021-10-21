package com.example.weddingplanner.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weddingplanner.R;
import com.example.weddingplanner.databinding.ActivityCurrencyBinding;
import com.example.weddingplanner.view.adapter.CurrencyAdapter;
import com.example.weddingplanner.view.currency.Currency;
import com.example.weddingplanner.view.interfaces.setOniClick;
import com.example.weddingplanner.view.model.ConstantData;
import com.example.weddingplanner.view.model.CurrencyModel;
import com.example.weddingplanner.view.param.Params;

import java.util.ArrayList;
import java.util.List;

public class CurrencyActivity extends AppCompatActivity {
    private ActivityCurrencyBinding binding;
    private CurrencyAdapter currencyAdapter;
    private List<CurrencyModel> currencyList;
    private CurrencyModel currencyModel;
    private CurrencyModel currencyModelData;
    private List<CurrencyModel> filterCurrency;
    private boolean isfilter = false;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityCurrencyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        this.currencyList = Currency.CurrencyList();
        this.binding.toollbar.imgBack.setVisibility(View.VISIBLE);
        this.binding.toollbar.cardDone.setVisibility(View.VISIBLE);
        this.binding.toollbar.tvTitle.setText(getResources().getString(R.string.currency_title));
        initView();
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            this.currencyModel = intent.getParcelableExtra(Params.CURRENCY_VALUE);
        }
        CurrencyModel currencyModel2 = this.currencyModel;
        if (currencyModel2 != null) {
            List<CurrencyModel> list = this.currencyList;
            list.set(list.indexOf(currencyModel2), this.currencyModel);
            this.currencyAdapter.notifyItemChanged(this.currencyList.indexOf(this.currencyModel));
        }
    }

    private void initView() {
        setAdapterRecyclerView();
        ClickEvent();
    }

    private void ClickEvent() {
        this.binding.toollbar.imgBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                CurrencyActivity.this.onBackPressed();
            }
        });
        this.binding.toollbar.imgDone.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (!ConstantData.isNetworkAvailable(CurrencyActivity.this)) {
                    CurrencyActivity currencyActivity = CurrencyActivity.this;
                    ConstantData.toastShort(currencyActivity, currencyActivity.getString(R.string.no_internet_available));
                    return;
                }
                Intent intent = CurrencyActivity.this.getIntent();
                intent.putExtra(Params.CURRENCY, CurrencyActivity.this.currencyModel);
                CurrencyActivity.this.setResult(-1, intent);
                CurrencyActivity.this.finish();
            }
        });
        this.binding.etSearch.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                CurrencyActivity.this.isfilter = true;
                CurrencyActivity.this.filter(editable.toString());
            }
        });
        this.binding.imgClose.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                CurrencyActivity.this.lambda$ClickEvent$0$CurrencyActivity(view);
            }
        });
    }

    public void lambda$ClickEvent$0$CurrencyActivity(View view) {
        this.binding.etSearch.getText().clear();
        this.isfilter = false;
        filter("");
    }


    private void filter(String str) {
        this.filterCurrency = new ArrayList();
        for (CurrencyModel currencyModel2 : this.currencyList) {
            if (currencyModel2.getCurrencyName().toLowerCase().contains(str.toLowerCase()) || currencyModel2.getCurrency().toLowerCase().contains(str.toLowerCase())) {
                this.filterCurrency.add(currencyModel2);
            }
        }
        this.currencyAdapter.filterList(this.filterCurrency);
    }

    private void setAdapterRecyclerView() {
        this.binding.recyclerView.setHasFixedSize(true);
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.currencyAdapter = new CurrencyAdapter(this.currencyList);
        this.binding.recyclerView.setAdapter(this.currencyAdapter);
        this.currencyAdapter.setOniClick(new setOniClick() {

            @Override
            public void selectLongClick(int i) {
            }

            @Override
            public void selectCurrency(int i) {
                if (CurrencyActivity.this.isfilter) {
                    CurrencyActivity.this.filterCurrency.set(CurrencyActivity.this.filterCurrency.indexOf(CurrencyActivity.this.filterCurrency.get(i)), CurrencyActivity.this.filterCurrency.get(i));
                    CurrencyActivity.this.currencyAdapter.notifyItemChanged(CurrencyActivity.this.filterCurrency.indexOf(CurrencyActivity.this.filterCurrency.get(i)));
                    if (!ConstantData.isNetworkAvailable(CurrencyActivity.this)) {
                        CurrencyActivity currencyActivity = CurrencyActivity.this;
                        ConstantData.toastShort(currencyActivity, currencyActivity.getString(R.string.no_internet_available));
                        return;
                    }
                    Intent intent = CurrencyActivity.this.getIntent();
                    intent.putExtra(Params.CURRENCY, CurrencyActivity.this.filterCurrency.get(i));
                    CurrencyActivity.this.setResult(-1, intent);
                    CurrencyActivity.this.finish();
                    return;
                }
                CurrencyActivity.this.currencyList.set(CurrencyActivity.this.currencyList.indexOf(CurrencyActivity.this.currencyList.get(i)), CurrencyActivity.this.currencyList.get(i));
                CurrencyActivity.this.currencyAdapter.notifyItemChanged(CurrencyActivity.this.currencyList.indexOf(CurrencyActivity.this.currencyList.get(i)));
                Intent intent2 = CurrencyActivity.this.getIntent();
                intent2.putExtra(Params.CURRENCY, CurrencyActivity.this.currencyList.get(i));
                CurrencyActivity.this.setResult(-1, intent2);
                CurrencyActivity.this.finish();
            }
        });
    }
}
