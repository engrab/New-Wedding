package com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weddingplanner.R;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.roomDatabase.AppDataBase;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.roomDatabase.payment.PaymentRowModel;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils.RecyclerItemClick;
import com.example.weddingplanner.databinding.RowPaymentBinding;

import java.util.ArrayList;

public class PaymentAdapterLeading extends RecyclerView.Adapter {

    public ArrayList<PaymentRowModel> arrayList;

    public Context context;

    public RecyclerItemClick recyclerItemClick;

    public PaymentAdapterLeading(Context context2, ArrayList<PaymentRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
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
            rowHolder.binding.tvTitle.setText(arrayList.get(i).getName());
            rowHolder.binding.tvAmount.setText(arrayList.get(i).getAmountFormatted());
            rowHolder.binding.tvDate.setText(arrayList.get(i).getDateFormatted());

//            rowHolder.binding.setRowModel(this.arrayList.get(i));
//            rowHolder.binding.executePendingBindings();
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
                ((PaymentRowModel) PaymentAdapterLeading.this.arrayList.get(getAdapterPosition())).setPending(true ^ ((PaymentRowModel) PaymentAdapterLeading.this.arrayList.get(getAdapterPosition())).isPending());
                AppDataBase.getAppDatabase(PaymentAdapterLeading.this.context).paymentDao().update((PaymentRowModel) PaymentAdapterLeading.this.arrayList.get(getAdapterPosition()));
                PaymentAdapterLeading.this.recyclerItemClick.onClick(getAdapterPosition(), 2);
                return;
            }
            PaymentAdapterLeading.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }
}
