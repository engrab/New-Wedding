package com.msint.weddingplanner.appBase.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.roomsDB.cost.CostRowModel;
import com.msint.weddingplanner.appBase.utils.RecyclerItemClick;
import com.msint.weddingplanner.databinding.RowCompanionBinding;
import com.msint.weddingplanner.databinding.RowCostBinding;

import java.util.ArrayList;

public class CostAdapter extends RecyclerView.Adapter {
    private ArrayList<CostRowModel> arrayList;
    private Context context;

    public RecyclerItemClick recyclerItemClick;

    public CostAdapter(Context context2, ArrayList<CostRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RowHolder(RowCostBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }


    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;
//            rowHolder.binding.setRowModel(this.arrayList.get(i));
//            rowHolder.binding.executePendingBindings();
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

private class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public RowCostBinding binding;

    public RowHolder(RowCostBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        binding.getRoot().setOnClickListener(this);
    }

    public void onClick(View view) {
        CostAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
    }
}
}
