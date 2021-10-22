package com.example.weddingplanner.allLeading.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weddingplanner.R;
import com.example.weddingplanner.allLeading.roomDatabase.category.CategoryRowModel;
import com.example.weddingplanner.allLeading.utils.RecyclerItemClick;
import com.example.weddingplanner.databinding.RowCategoryBinding;
import com.example.weddingplanner.databinding.RowCategoryManageBinding;
import java.util.ArrayList;

public class CategoryAdapterLeading extends RecyclerView.Adapter {

    public ArrayList<CategoryRowModel> arrayList;
    private final Context context;
    private final boolean isManage;
    private int checkedPosition = 0;

    public RecyclerItemClick recyclerItemClick;

    public CategoryAdapterLeading(Context context2, boolean z, ArrayList<CategoryRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
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

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;
            rowHolder.bind(arrayList.get(position));

//            rowHolder.binding.imgIcon.setImageResource(arrayList.get(position).getImgResId());
//            rowHolder.binding.title.setText(arrayList.get(position).getName());



        } else if (viewHolder instanceof RowManageHolder) {
            RowManageHolder rowManageHolder = (RowManageHolder) viewHolder;
            rowManageHolder.binding.imgIcon.setImageResource(arrayList.get(position).getImgResId());
            rowManageHolder.binding.title.setText(arrayList.get(position).getName());
//            rowManageHolder.binding.setRowModel(this.arrayList.get(i));
//            rowManageHolder.binding.executePendingBindings();
        }
    }
    public CategoryRowModel getSelected() {
        if (checkedPosition != -1) {
            return arrayList.get(checkedPosition);
        }
        return null;
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    private class RowHolder extends RecyclerView.ViewHolder {

        public RowCategoryBinding binding;

        public RowHolder(RowCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
//            binding.getRoot().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    selectionAll(false);
//                    arrayList.get(getAdapterPosition()).setSelected(!arrayList.get(getAdapterPosition()).isSelected());
//                    recyclerItemClick.onClick(getAdapterPosition(), 1);
//
//                }
//            });
        }


        void bind(final CategoryRowModel categoryRowModel) {
            if (checkedPosition == -1) {
                binding.image.setVisibility(View.GONE);
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    binding.image.setVisibility(View.VISIBLE);
                } else {
                    binding.image.setVisibility(View.GONE);
                }
            }
            binding.title.setText(categoryRowModel.getName());
            binding.imgIcon.setImageResource(categoryRowModel.getImgResId());
            binding.image.setImageResource(R.drawable.save);
            binding.image.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);


            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.image.setVisibility(View.VISIBLE);
                    if (checkedPosition != getAdapterPosition()) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                        selectionAll(false);
                        arrayList.get(getAdapterPosition()).setSelected(!arrayList.get(getAdapterPosition()).isSelected());
                        recyclerItemClick.onClick(getAdapterPosition(), 1);

                    }
                }
            });
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
                recyclerItemClick.onClick(getAdapterPosition(), 2);
            } else if (id == R.id.imgEdit) {
                recyclerItemClick.onClick(getAdapterPosition(), 1);
            }
        }
    }
}
