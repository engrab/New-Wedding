package com.example.weddingplanner.all.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weddingplanner.R;
import com.example.weddingplanner.all.models.image.ImageRowModel;
import com.example.weddingplanner.all.utils.RecyclerItemClick;
import com.example.weddingplanner.databinding.RowImageIconBinding;
import com.example.weddingplanner.databinding.RowImageLargeBinding;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter {

    public ArrayList<ImageRowModel> arrayList;
    private final Context context;
    private final boolean isIcon;
    private int isSelectedP;
    private int checkedPosition = 0;

    public RecyclerItemClick recyclerItemClick;

    public ImageAdapter(boolean z, Context context2, ArrayList<ImageRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
        this.isIcon = z;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        if (this.isIcon) {
            return new RowHolder(RowImageIconBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
//        }
//        return new RowHolderLarge(RowImageLargeBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));

    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;
            rowHolder.bind(arrayList.get(i));
//            if (arrayList.get(i).isSelected()){
//                rowHolder.binding.llImage.setBackgroundColor(Color.parseColor("#1976D2"));
//                rowHolder.binding.imgFolder.setImageResource(arrayList.get(i).getImgResId());
//            } else {
//
//                rowHolder.binding.imgFolder.setImageResource(arrayList.get(i).getImgResId());
//            }

//            rowHolder.binding.setRowModel(this.arrayList.get(i));
//            rowHolder.binding.executePendingBindings();
        }
//        else if (viewHolder instanceof RowHolderLarge) {
//            RowHolderLarge rowHolderLarge = (RowHolderLarge) viewHolder;
//            if (arrayList.get(i).isSelected()){
//                rowHolderLarge.binding.llImage.setBackgroundColor(Color.parseColor("#1976D2"));
//                rowHolderLarge.binding.imageViewLarge.setImageResource(arrayList.get(i).getImgResId());
//            } else {
//
//                rowHolderLarge.binding.imageViewLarge.setImageResource(arrayList.get(i).getImgResId());
//            }
////            rowHolderLarge.binding.setRowModel(this.arrayList.get(i));
////            rowHolderLarge.binding.executePendingBindings();
//        }
    }
    public ImageRowModel getSelected() {
        if (checkedPosition != -1) {
            return arrayList.get(checkedPosition);
        }
        return null;
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    private class RowHolder extends RecyclerView.ViewHolder{

        public RowImageIconBinding binding;

        public RowHolder(RowImageIconBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
//            binding.getRoot().setOnClickListener(this);
        }

//        public void onClick(View view) {
//            ImageAdapter.this.selectionAll(false);
//            ImageAdapter.this.arrayList.get(getAdapterPosition()).setSelected(!ImageAdapter.this.arrayList.get(getAdapterPosition()).isSelected());
//            ImageAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
//
//        }

        void bind(final ImageRowModel im) {
            if (checkedPosition == -1) {
                binding.imgFolder.setVisibility(View.GONE);
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    binding.imgFolder.setVisibility(View.VISIBLE);
                } else {
                    binding.imgFolder.setVisibility(View.GONE);
                }
            }
            binding.imgIcon.setImageResource(im.getImgResId());
            binding.imgFolder.setImageResource(R.drawable.save);
            binding.imgFolder.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.imgFolder.setVisibility(View.VISIBLE);
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

    private class RowHolderLarge extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowImageLargeBinding binding;

        public RowHolderLarge(RowImageLargeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        public void onClick(View view) {
            ImageAdapter.this.selectionAll(false);
            ImageAdapter.this.arrayList.get(getAdapterPosition()).setSelected(!arrayList.get(getAdapterPosition()).isSelected());
            ImageAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 1);

        }
    }


    public void selectionAll(boolean z) {
        for (int i = 0; i < this.arrayList.size(); i++) {
            this.arrayList.get(i).setSelected(z);
        }
    }
}
