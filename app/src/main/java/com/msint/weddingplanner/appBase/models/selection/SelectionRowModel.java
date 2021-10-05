package com.msint.weddingplanner.appBase.models.selection;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.msint.weddingplanner.appBase.utils.AppConstants;

public class SelectionRowModel extends BaseObservable {


    private String f531id;
    private String imageUrl;
    private boolean isSelected;
    private String label;
    private String value;
    private int viewType;

    public SelectionRowModel(String str) {
        this.label = str;
    }

    public SelectionRowModel(String str, String str2) {
        this.f531id = str;
        this.label = str2;
    }

    public String getId() {
        return this.f531id;
    }

    public void setId(String str) {
        this.f531id = str;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String str) {
        this.label = str;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String str) {
        this.imageUrl = str;
    }

    @Bindable
    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
        notifyChange();
    }

    public int getViewType() {
        return this.viewType;
    }

    public void setViewType(int i) {
        this.viewType = i;
    }

    public int getImageIcon() {
        return AppConstants.getResIdUsingCategoryType(getId());
    }
}
