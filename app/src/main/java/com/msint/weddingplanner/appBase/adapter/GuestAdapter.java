package com.msint.weddingplanner.appBase.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.roomsDB.AppDataBase;
import com.msint.weddingplanner.appBase.roomsDB.guest.GuestRowModel;
import com.msint.weddingplanner.appBase.utils.RecyclerItemClick;
import com.msint.weddingplanner.databinding.RowCompanionBinding;
import com.msint.weddingplanner.databinding.RowGuestBinding;

import java.util.ArrayList;

public class GuestAdapter extends RecyclerView.Adapter {

    public ArrayList<GuestRowModel> arrayList;

    public Context context;

    public RecyclerItemClick recyclerItemClick;

    public GuestAdapter(Context context2, ArrayList<GuestRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        return new RowHolder(RowGuestBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }


    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
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

        public RowGuestBinding binding;

        public RowHolder(RowGuestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
            this.binding.imgCheck.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (view.getId() == R.id.imgCheck) {
                ((GuestRowModel) GuestAdapter.this.arrayList.get(getAdapterPosition())).setInvitationSent(true ^ ((GuestRowModel) GuestAdapter.this.arrayList.get(getAdapterPosition())).isInvitationSent());
                AppDataBase.getAppDatabase(GuestAdapter.this.context).guestDao().update((GuestRowModel) GuestAdapter.this.arrayList.get(getAdapterPosition()));
                GuestAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 2);
                return;
            }
            GuestAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }
}
