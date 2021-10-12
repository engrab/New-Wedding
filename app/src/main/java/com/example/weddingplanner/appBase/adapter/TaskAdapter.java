package com.example.weddingplanner.appBase.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weddingplanner.appBase.roomsDB.taskList.TaskRowModel;
import com.example.weddingplanner.appBase.utils.RecyclerItemClick;
import com.example.weddingplanner.databinding.RowTaskBinding;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter {
    private ArrayList<TaskRowModel> arrayList;
    private Context context;

    public RecyclerItemClick recyclerItemClick;

    public TaskAdapter(Context context2, ArrayList<TaskRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RowHolder(RowTaskBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;
            rowHolder.binding.imgIcon.setImageResource(arrayList.get(i).getCategoryRowModel().getImgResId());
            rowHolder.binding.tvDate.setText(arrayList.get(i).getDateFormatted());
            rowHolder.binding.tvStatus.setText(arrayList.get(i).getStatusText());
            rowHolder.binding.tvTitle.setText(arrayList.get(i).getName());
            rowHolder.binding.tvSubtask.setText(arrayList.get(i).getSubTaskText());

//            rowHolder.binding.setRowModel(this.arrayList.get(i));
//            rowHolder.binding.executePendingBindings();
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    private class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowTaskBinding binding;

        public RowHolder(RowTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        public void onClick(View view) {
            TaskAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }
}
