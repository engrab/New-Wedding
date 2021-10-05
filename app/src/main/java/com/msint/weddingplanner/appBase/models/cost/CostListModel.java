package com.msint.weddingplanner.appBase.models.cost;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.msint.weddingplanner.appBase.roomsDB.cost.CostRowModel;
import java.util.ArrayList;

public class CostListModel extends BaseObservable {
    private ArrayList<CostRowModel> arrayList;
    private String noDataDetail;
    private int noDataIcon;
    private String noDataText;

    @Bindable
    public ArrayList<CostRowModel> getArrayList() {
        return this.arrayList;
    }

    public void setArrayList(ArrayList<CostRowModel> arrayList2) {
        this.arrayList = arrayList2;
        notifyChange();
    }

    public int getNoDataIcon() {
        return this.noDataIcon;
    }

    public void setNoDataIcon(int i) {
        this.noDataIcon = i;
    }

    public String getNoDataText() {
        return this.noDataText;
    }

    public void setNoDataText(String str) {
        this.noDataText = str;
    }

    public String getNoDataDetail() {
        return this.noDataDetail;
    }

    public void setNoDataDetail(String str) {
        this.noDataDetail = str;
    }

    public boolean isListData() {
        return getArrayList() != null && getArrayList().size() > 0;
    }
}
