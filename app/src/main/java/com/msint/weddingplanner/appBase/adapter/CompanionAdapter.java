package com.msint.weddingplanner.appBase.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.roomsDB.AppDataBase;
import com.msint.weddingplanner.appBase.roomsDB.guest.GuestRowModel;
import com.msint.weddingplanner.appBase.utils.RecyclerItemClick;
import com.msint.weddingplanner.databinding.RowCompanionBinding;
import java.util.ArrayList;

public class CompanionAdapter extends RecyclerView.Adapter {

    public ArrayList<GuestRowModel> arrayList;

    public Context context;

    public RecyclerItemClick recyclerItemClick;

    public CompanionAdapter(Context context2, ArrayList<GuestRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
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
            rowHolder.binding.setRowModel(this.arrayList.get(i));
            rowHolder.binding.executePendingBindings();
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
                CompanionAdapter.this.arrayList.get(getAdapterPosition()).setInvitationSent(true ^ CompanionAdapter.this.arrayList.get(getAdapterPosition()).isInvitationSent());
                AppDataBase.getAppDatabase(CompanionAdapter.this.context).guestDao().update(CompanionAdapter.this.arrayList.get(getAdapterPosition()));
                CompanionAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 2);
                return;
            }
            CompanionAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }
}
