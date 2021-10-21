package com.example.weddingplanner.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.example.weddingplanner.databinding.ViewCurrencyadapterBinding;
import com.example.weddingplanner.view.interfaces.setOniClick;
import com.example.weddingplanner.view.model.CurrencyModel;

import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {
    private List<CurrencyModel> CurrencyModelList;
    private setOniClick oniClick;

    public CurrencyAdapter(List<CurrencyModel> list) {
        this.CurrencyModelList = list;
    }

    public void setOniClick(setOniClick setoniclick) {
        this.oniClick = setoniclick;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(ViewCurrencyadapterBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (this.CurrencyModelList != null) {
            TextView textView = viewHolder.binding.tvCurrency;
            textView.setText(this.CurrencyModelList.get(i).getCurrency() + ", " + this.CurrencyModelList.get(i).getCurrencyName());
            if (this.CurrencyModelList.get(i).isSelect()) {
                viewHolder.binding.imgSelection.setVisibility(View.VISIBLE);
            } else {
                viewHolder.binding.imgSelection.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.CurrencyModelList.size();
    }

    public void filterList(List<CurrencyModel> list) {
        this.CurrencyModelList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewCurrencyadapterBinding binding;
        View view;

        public ViewHolder(ViewCurrencyadapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.tvCurrency.setOnClickListener(view -> CurrencyAdapter.this.oniClick.selectCurrency(ViewHolder.this.getAdapterPosition()));
        }
    }
}
