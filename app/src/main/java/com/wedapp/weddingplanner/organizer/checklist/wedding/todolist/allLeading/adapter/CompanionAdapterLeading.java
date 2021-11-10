package com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.weddingplanner.R;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.roomDatabase.AppDataBase;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.roomDatabase.guest.GuestRowModel;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils.RecyclerItemClick;
import com.example.weddingplanner.databinding.RowCompanionBinding;

import java.util.ArrayList;

public class CompanionAdapterLeading extends RecyclerView.Adapter {

    public ArrayList<GuestRowModel> arrayList;

    public Context context;

    public RecyclerItemClick recyclerItemClick;

    public CompanionAdapterLeading(Context context2, ArrayList<GuestRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
    }

    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new RowHolder(RowCompanionBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;
            rowHolder.binding.tvName.setText(arrayList.get(i).getName());
//            rowHolder.binding.setRowModel(this.arrayList.get(i));
//            rowHolder.binding.executePendingBindings();
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    private class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowCompanionBinding binding;

        public RowHolder(RowCompanionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        public void onClick(View view) {
            if (view.getId() == R.id.imgCheck) {
                CompanionAdapterLeading.this.arrayList.get(getAdapterPosition()).setInvitationSent(true ^ CompanionAdapterLeading.this.arrayList.get(getAdapterPosition()).isInvitationSent());
                AppDataBase.getAppDatabase(CompanionAdapterLeading.this.context).guestDao().update(CompanionAdapterLeading.this.arrayList.get(getAdapterPosition()));
                CompanionAdapterLeading.this.recyclerItemClick.onClick(getAdapterPosition(), 2);
                return;
            }
            CompanionAdapterLeading.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }
}
