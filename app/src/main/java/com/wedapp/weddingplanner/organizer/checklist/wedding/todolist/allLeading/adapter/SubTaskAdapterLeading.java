package com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.weddingplanner.R;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.roomDatabase.AppDataBase;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.roomDatabase.taskList.SubTaskRowModel;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils.RecyclerItemClick;
import com.example.weddingplanner.databinding.RowSubTaskBinding;

import java.util.ArrayList;

public class SubTaskAdapterLeading extends RecyclerView.Adapter {

    public ArrayList<SubTaskRowModel> arrayList;

    public Context context;

    public RecyclerItemClick recyclerItemClick;

    public SubTaskAdapterLeading(Context context2, ArrayList<SubTaskRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RowHolder(RowSubTaskBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;

            rowHolder.binding.title.setText(arrayList.get(i).getName());
            rowHolder.binding.desc.setText(arrayList.get(i).getNote());
//            rowHolder.binding.setRowModel(this.arrayList.get(i));
//            rowHolder.binding.executePendingBindings();
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    private class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowSubTaskBinding binding;

        public RowHolder(RowSubTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
            this.binding.imgCheck.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (view.getId() == R.id.imgCheck) {
                SubTaskAdapterLeading.this.arrayList.get(getAdapterPosition()).setPending(true ^ SubTaskAdapterLeading.this.arrayList.get(getAdapterPosition()).isPending());
                AppDataBase.getAppDatabase(SubTaskAdapterLeading.this.context).subTaskDao().update(SubTaskAdapterLeading.this.arrayList.get(getAdapterPosition()));
                SubTaskAdapterLeading.this.recyclerItemClick.onClick(getAdapterPosition(), 2);
                return;
            }
            SubTaskAdapterLeading.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }
}
