package com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.models.selection.SelectionRowModel;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils.RecyclerItemClick;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.databinding.RowSelectionItemBinding;

import java.util.ArrayList;

public class SelectionAdapterLeading extends RecyclerView.Adapter {

    public ArrayList<SelectionRowModel> arrayList;
    private Context context;

    public boolean isSelection;

    public RecyclerItemClick recyclerItemClick;

    public SelectionAdapterLeading(Context context2, boolean z, ArrayList<SelectionRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.context = context2;
        this.isSelection = z;
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RowHolder(RowSelectionItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;
            rowHolder.binding.title.setText(arrayList.get(i).getLabel());
//            rowHolder.binding.setRowModel(this.arrayList.get(i));
//            rowHolder.binding.executePendingBindings();
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    private class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowSelectionItemBinding binding;

        public RowHolder(RowSelectionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        public void onClick(View view) {
            if (SelectionAdapterLeading.this.isSelection) {
                SelectionAdapterLeading.this.selectionAll(false);
                ((SelectionRowModel) SelectionAdapterLeading.this.arrayList.get(getAdapterPosition())).setSelected(!((SelectionRowModel) SelectionAdapterLeading.this.arrayList.get(getAdapterPosition())).isSelected());
            }
            SelectionAdapterLeading.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }


    public void selectionAll(boolean z) {
        for (int i = 0; i < this.arrayList.size(); i++) {
            this.arrayList.get(i).setSelected(z);
        }
    }
}
