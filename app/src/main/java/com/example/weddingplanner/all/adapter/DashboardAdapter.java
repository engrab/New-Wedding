package com.example.weddingplanner.all.adapter;

import android.content.Context;

import android.graphics.drawable.Drawable;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weddingplanner.R;
import com.example.weddingplanner.all.models.drawer.DrawerRowModel;
import com.example.weddingplanner.all.utils.RecyclerItemClick;
import com.example.weddingplanner.databinding.RowDashboardItemBinding;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter {
    private ArrayList<DrawerRowModel> arrayList;
    private Context context;

    public RecyclerItemClick recyclerItemClick;

    public DashboardAdapter(Context context2, ArrayList<DrawerRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RowHolder(RowDashboardItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }


    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;
            rowHolder.binding.linSub.setBackground(getBgDrawable(i));
//            rowHolder.binding.setRowModel(this.arrayList.get(i));
//            rowHolder.binding.executePendingBindings();
        }
    }

    private Drawable getBgDrawable(int i) {
        switch (this.arrayList.get(i).getConsPosition()) {
            case 10:
                return this.context.getResources().getDrawable(R.drawable.task_list_bg_gradient);
            case 11:
                return this.context.getResources().getDrawable(R.drawable.guest_list_bg_gradient);
            case 12:
                return this.context.getResources().getDrawable(R.drawable.budget_list_bg_gradient);
            case 13:
                return this.context.getResources().getDrawable(R.drawable.vendor_list_bg_gradient);
            default:
                return this.context.getResources().getDrawable(R.drawable.vendor_list_bg_gradient);
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    private class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowDashboardItemBinding binding;

        public RowHolder(RowDashboardItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        public void onClick(View view) {
            DashboardAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }
}
