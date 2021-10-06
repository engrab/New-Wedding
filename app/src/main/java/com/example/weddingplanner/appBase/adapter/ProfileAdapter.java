package com.example.weddingplanner.appBase.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.weddingplanner.R;
import com.example.weddingplanner.appBase.models.profile.ProfileRowModel;
import com.example.weddingplanner.appBase.utils.RecyclerItemClick;
import com.example.weddingplanner.databinding.RowImageIconBinding;
import com.example.weddingplanner.databinding.RowProfileBinding;
import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter {
    private ArrayList<ProfileRowModel> arrayList;
    private Context context;

    public RecyclerItemClick recyclerItemClick;

    public ProfileAdapter(Context context2, ArrayList<ProfileRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RowHolder(RowProfileBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));


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

        public RowProfileBinding binding;

        public RowHolder(RowProfileBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.imgSelect.setOnClickListener(this);
            binding.getRoot().setOnClickListener(this);
        }

        public void onClick(View view) {
            if (view.getId() == R.id.imgSelect) {
                ProfileAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 2);
            } else {
                ProfileAdapter.this.recyclerItemClick.onClick(getAdapterPosition(), 1);
            }
        }
    }
}
