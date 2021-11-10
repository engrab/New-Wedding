package com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.models.drawer.DrawerRowModelLeading;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils.RecyclerItemClick;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.databinding.RowDrawerItemBinding;

import java.util.ArrayList;

public class DrawerAdapterLeading extends RecyclerView.Adapter {
    private ArrayList<DrawerRowModelLeading> arrayList;
    private Context context;

    public RecyclerItemClick recyclerItemClick;

    public DrawerAdapterLeading(Context context2, ArrayList<DrawerRowModelLeading> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        return new RowHolder(RowDrawerItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
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

        public RowDrawerItemBinding binding;

        public RowHolder(RowDrawerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
            this.binding.linMain.setOnClickListener(this);
        }

        public void onClick(View view) {
            DrawerAdapterLeading.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }
}
