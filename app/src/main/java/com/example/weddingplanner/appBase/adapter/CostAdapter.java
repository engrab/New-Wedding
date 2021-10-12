package com.example.weddingplanner.appBase.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weddingplanner.appBase.roomsDB.cost.CostRowModel;
import com.example.weddingplanner.appBase.utils.RecyclerItemClick;
import com.example.weddingplanner.databinding.RowCompanionBinding;
import com.example.weddingplanner.databinding.RowCostBinding;

import java.util.ArrayList;

public class CostAdapter extends RecyclerView.Adapter {
    private ArrayList<CostRowModel> arrayList;
    private Context context;

    public RecyclerItemClick recyclerItemClick;

    public CostAdapter(Context context, ArrayList<CostRowModel> arrayList, RecyclerItemClick recyclerItemClick2) {
        this.context = context;
        this.arrayList = arrayList;
        this.recyclerItemClick = recyclerItemClick2;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RowHolder(RowCostBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }


    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;
//            rowHolder.binding.imgIcon.setImageResource((int) arrayList.get(i).getExpectedAmount());
            rowHolder.binding.tvAmount.setText(arrayList.get(i).getExpectedAmount()+"");
            rowHolder.binding.tvPending.setText(arrayList.get(i).getStatusText());
            rowHolder.binding.tvStatus.setText(arrayList.get(i).getId());

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
