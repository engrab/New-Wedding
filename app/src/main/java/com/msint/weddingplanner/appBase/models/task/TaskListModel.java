package com.msint.weddingplanner.appBase.models.task;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.msint.weddingplanner.appBase.roomsDB.taskList.TaskRowModel;
import java.util.ArrayList;

public class TaskListModel extends BaseObservable {
    private ArrayList<TaskRowModel> arrayList;
    private String noDataDetail;
    private int noDataIcon;
    private String noDataText;

    @Bindable
    public ArrayList<TaskRowModel> getArrayList() {
        return this.arrayList;
    }

    public void setArrayList(ArrayList<TaskRowModel> arrayList2) {
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
