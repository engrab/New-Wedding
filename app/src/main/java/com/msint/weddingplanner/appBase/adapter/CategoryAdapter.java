package com.msint.weddingplanner.appBase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.roomsDB.category.CategoryRowModel;
import com.msint.weddingplanner.appBase.utils.RecyclerItemClick;
import com.msint.weddingplanner.databinding.RowCategoryBinding;
import com.msint.weddingplanner.databinding.RowCategoryManageBinding;
import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter {

    public ArrayList<CategoryRowModel> arrayList;
    private Context context;
    private boolean isManage;

    public RecyclerItemClick recyclerItemClick;

    public CategoryAdapter(Context context2, boolean z, ArrayList<CategoryRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
        this.isManage = z;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (!this.isManage) {
            return new RowHolder(RowCategoryBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));

        }
        return new RowManageHolder(RowCategoryManageBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));

    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;

        } else if (viewHolder instanceof RowManageHolder) {
            RowManageHolder rowManageHolder = (RowManageHolder) viewHolder;
            rowManageHolder.binding.setRowModel(this.arrayList.get(i));
            rowManageHolder.binding.executePendingBindings();
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    private class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowCategoryBinding binding;

        public RowHolder(RowCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        public void onClick(View view) {
            CategoryAdapter.this.selectionAll(false);
            ((CategoryRowModel) CategoryAdapter.this.arrayList.get(getAdapterPosition())).setSelected(!((CategoryRowModel) CategoryAdapter.this.arrayList.get(getAdapterPosition())).isSelected());
            CategoryAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }


    public void selectionAll(boolean z) {
        for (int i = 0; i < this.arrayList.size(); i++) {
            this.arrayList.get(i).setSelected(z);
        }
    }

    private class RowManageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowCategoryManageBinding binding;

        public RowManageHolder(RowCategoryManageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
            this.binding.imgEdit.setOnClickListener(this);
            this.binding.imgDelete.setOnClickListener(this);
        }

        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.imgDelete) {
                CategoryAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 2);
            } else if (id == R.id.imgEdit) {
                CategoryAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
            }
        }
    }
}
