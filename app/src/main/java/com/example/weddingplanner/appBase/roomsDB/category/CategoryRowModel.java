package com.example.weddingplanner.appBase.roomsDB.category;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import com.example.weddingplanner.appBase.MyApp;
import com.example.weddingplanner.appBase.utils.AppConstants;

@Entity(tableName = "categoryList")
public class CategoryRowModel extends BaseObservable implements Parcelable {
    public static final Creator<CategoryRowModel> CREATOR = new Creator<CategoryRowModel>() {
        public CategoryRowModel createFromParcel(Parcel parcel) {
            return new CategoryRowModel(parcel);
        }

        public CategoryRowModel[] newArray(int i) {
            return new CategoryRowModel[i];
        }
    };
    private String iconType;
    @PrimaryKey
    @NonNull


    private String id;
    private boolean isDefault;
    @Ignore
    private boolean isSelected;
    private String name = "";

    public int describeContents() {
        return 0;
    }

    public CategoryRowModel() {
    }

    public CategoryRowModel(@NonNull String str, String str2, String str3) {
        this.id = str;
        this.name = str2;
        this.iconType = str3;
    }

    public CategoryRowModel(@NonNull String str, String str2, String str3, boolean z) {
        this.id = str;
        this.name = str2;
        this.iconType = str3;
        this.isDefault = z;
    }

    @NonNull
    public String getId() {
        return this.id;
    }

    public void setId(@NonNull String str) {
        this.id = str;
    }

    @Bindable
    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
        notifyChange();
    }

    @Bindable
    public String getIconType() {
        return this.iconType;
    }

    public void setIconType(String str) {
        this.iconType = str;
        notifyChange();
    }

    public boolean isDefault() {
        return this.isDefault;
    }

    public void setDefault(boolean z) {
        this.isDefault = z;
    }

    @Bindable
    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
        notifyChange();
    }

    public String getImageUrl() {
        return AppConstants.getResourcePathWithPackage(MyApp.getInstance().getApplicationContext()) + AppConstants.getResIdUsingCategoryType(getIconType());
    }

    public int getImgResId() {
        return AppConstants.getResIdUsingCategoryType(getIconType());
    }

    protected CategoryRowModel(Parcel parcel) {
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.iconType = parcel.readString();
        boolean z = false;
        this.isDefault = parcel.readByte() != 0;
        this.isSelected = parcel.readByte() != 0 ? true : z;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.iconType);
        parcel.writeByte(this.isDefault ? (byte) 1 : 0);
        parcel.writeByte(this.isSelected ? (byte) 1 : 0);
    }
}
