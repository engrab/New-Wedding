package com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.models.image;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.MyApp;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils.AppConstants;

public class ImageRowModel extends BaseObservable {


    private String id;
    private String imageUrl;
    private boolean isSelected;

    public ImageRowModel() {
    }

    public ImageRowModel(String str) {
        this.imageUrl = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
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
