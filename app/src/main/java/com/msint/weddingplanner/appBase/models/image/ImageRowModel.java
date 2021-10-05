package com.msint.weddingplanner.appBase.models.image;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.msint.weddingplanner.appBase.MyApp;
import com.msint.weddingplanner.appBase.utils.AppConstants;

public class ImageRowModel extends BaseObservable {

    /* renamed from: id */
    private String f529id;
    private String imageUrl;
    private boolean isSelected;

    public ImageRowModel() {
    }

    public ImageRowModel(String str) {
        this.imageUrl = str;
    }

    public String getId() {
        return this.f529id;
    }

    public void setId(String str) {
        this.f529id = str;
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

    public int getImgResId() {
        return AppConstants.getResIdUsingCategoryType(getId());
    }

    public String getImageUrlIcon() {
        return AppConstants.getResourcePathWithPackage(MyApp.getInstance().getApplicationContext()) + getImgResId();
    }
}
