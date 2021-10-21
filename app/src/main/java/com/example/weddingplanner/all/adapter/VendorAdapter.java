package com.example.weddingplanner.all.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weddingplanner.all.roomsDB.vendor.VendorRowModel;
import com.example.weddingplanner.all.utils.RecyclerItemClick;
import com.example.weddingplanner.databinding.RowVendorBinding;

import java.util.ArrayList;

public class VendorAdapter extends RecyclerView.Adapter {
    private ArrayList<VendorRowModel> arrayList;
    private Context context;

    public RecyclerItemClick recyclerItemClick;

    public VendorAdapter(Context context2, ArrayList<VendorRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
    }

    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RowHolder(RowVendorBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;
            rowHolder.binding.imgIcon.setImageResource(arrayList.get(i).getCategoryRowModel().getImgResId());
            rowHolder.binding.tvName.setText(arrayList.get(i).getName());
            rowHolder.binding.tvAmount.setText(arrayList.get(i).getExpectedAmount()+"");
            rowHolder.binding.tvStatus.setText("Pending : "+arrayList.get(i).getPendingAmount());
            rowHolder.binding.tvStatus1.setText("Paid : "+arrayList.get(i).getPaidAmount());
            rowHolder.binding.tvPending.setText(arrayList.get(i).getStatusText());

//            rowHolder.binding.setRowModel(this.arrayList.get(i));
//            rowHolder.binding.executePendingBindings();
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    private class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowVendorBinding binding;

        public RowHolder(RowVendorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        public void onClick(View view) {
            VendorAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }
}
