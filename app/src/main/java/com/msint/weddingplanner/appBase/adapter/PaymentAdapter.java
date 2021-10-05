package com.msint.weddingplanner.appBase.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.roomsDB.AppDataBase;
import com.msint.weddingplanner.appBase.roomsDB.payment.PaymentRowModel;
import com.msint.weddingplanner.appBase.utils.RecyclerItemClick;
import com.msint.weddingplanner.databinding.RowImageIconBinding;
import com.msint.weddingplanner.databinding.RowPaymentBinding;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter {

    public ArrayList<PaymentRowModel> arrayList;

    public Context context;

    public RecyclerItemClick recyclerItemClick;

    public PaymentAdapter(Context context2, ArrayList<PaymentRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        return new RowHolder(RowPaymentBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
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

        public RowPaymentBinding binding;

        public RowHolder(RowPaymentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
            this.binding.imgCheck.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (view.getId() == R.id.imgCheck) {
                ((PaymentRowModel) PaymentAdapter.this.arrayList.get(getAdapterPosition())).setPending(true ^ ((PaymentRowModel) PaymentAdapter.this.arrayList.get(getAdapterPosition())).isPending());
                AppDataBase.getAppDatabase(PaymentAdapter.this.context).paymentDao().update((PaymentRowModel) PaymentAdapter.this.arrayList.get(getAdapterPosition()));
                PaymentAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 2);
                return;
            }
            PaymentAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }
}
