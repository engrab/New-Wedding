package com.msint.weddingplanner.appBase.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.models.image.ImageRowModel;
import com.msint.weddingplanner.appBase.utils.RecyclerItemClick;
import com.msint.weddingplanner.databinding.RowCompanionBinding;
import com.msint.weddingplanner.databinding.RowImageIconBinding;
import com.msint.weddingplanner.databinding.RowImageLargeBinding;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter {

    public ArrayList<ImageRowModel> arrayList;
    private final Context context;
    private final boolean isIcon;

    public RecyclerItemClick recyclerItemClick;

    public ImageAdapter(boolean z, Context context2, ArrayList<ImageRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
        this.isIcon = z;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (this.isIcon) {
            return new RowHolder(RowImageIconBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
        }
        return new RowHolderLarge(RowImageLargeBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));

    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;
            rowHolder.binding.setRowModel(this.arrayList.get(i));
            rowHolder.binding.executePendingBindings();
        } else if (viewHolder instanceof RowHolderLarge) {
            RowHolderLarge rowHolderLarge = (RowHolderLarge) viewHolder;
            rowHolderLarge.binding.setRowModel(this.arrayList.get(i));
            rowHolderLarge.binding.executePendingBindings();
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    private class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowImageIconBinding binding;

        public RowHolder(RowImageIconBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        public void onClick(View view) {
            ImageAdapter.this.selectionAll(false);
            ImageAdapter.this.arrayList.get(getAdapterPosition()).setSelected(!ImageAdapter.this.arrayList.get(getAdapterPosition()).isSelected());
            ImageAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }

    private class RowHolderLarge extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowImageLargeBinding binding;

        public RowHolderLarge(RowImageLargeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        public void onClick(View view) {
            ImageAdapter.this.selectionAll(false);
            ImageAdapter.this.arrayList.get(getAdapterPosition()).setSelected(!ImageAdapter.this.arrayList.get(getAdapterPosition()).isSelected());
            ImageAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }


    public void selectionAll(boolean z) {
        for (int i = 0; i < this.arrayList.size(); i++) {
            this.arrayList.get(i).setSelected(z);
        }
    }
}
