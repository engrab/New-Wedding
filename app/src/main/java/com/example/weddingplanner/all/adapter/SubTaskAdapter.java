package com.example.weddingplanner.all.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.weddingplanner.R;
import com.example.weddingplanner.all.roomsDB.AppDataBase;
import com.example.weddingplanner.all.roomsDB.taskList.SubTaskRowModel;
import com.example.weddingplanner.all.utils.RecyclerItemClick;
import com.example.weddingplanner.databinding.RowSubTaskBinding;
import java.util.ArrayList;

public class SubTaskAdapter extends RecyclerView.Adapter {

    public ArrayList<SubTaskRowModel> arrayList;

    public Context context;

    public RecyclerItemClick recyclerItemClick;

    public SubTaskAdapter(Context context2, ArrayList<SubTaskRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
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
                SubTaskAdapter.this.arrayList.get(getAdapterPosition()).setPending(true ^ SubTaskAdapter.this.arrayList.get(getAdapterPosition()).isPending());
                AppDataBase.getAppDatabase(SubTaskAdapter.this.context).subTaskDao().update(SubTaskAdapter.this.arrayList.get(getAdapterPosition()));
                SubTaskAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 2);
                return;
            }
            SubTaskAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }
}
