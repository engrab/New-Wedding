package com.msint.weddingplanner.appBase.roomsDB.taskList;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.MyApp;
import com.msint.weddingplanner.appBase.appPref.AppPref;
import com.msint.weddingplanner.appBase.roomsDB.category.CategoryRowModel;
import com.msint.weddingplanner.appBase.utils.AppConstants;
import com.msint.weddingplanner.appBase.utils.Constants;
import java.util.ArrayList;
import net.lingala.zip4j.util.InternalZipConstants;

@Entity(tableName = "taskList")
public class TaskRowModel extends BaseObservable implements Parcelable {
    public static final Creator<TaskRowModel> CREATOR = new Creator<TaskRowModel>() {
        public TaskRowModel createFromParcel(Parcel parcel) {
            return new TaskRowModel(parcel);
        }

        public TaskRowModel[] newArray(int i) {
            return new TaskRowModel[i];
        }
    };
    @Ignore
    private ArrayList<SubTaskRowModel> arrayList;
    private String categoryId;
    @Ignore
    private CategoryRowModel categoryRowModel;
    private long dateInMillis;
    private String eventId;
    @PrimaryKey
    @NonNull


    private String id;
    @Ignore
    private boolean isEdit;
    private boolean isPending;
    private String name;
    private String note;

    public int describeContents() {
        return 0;
    }

    @Bindable
    public boolean isEdit() {
        return true;
    }

    public TaskRowModel() {
        this.eventId = AppPref.getCurrentEvenId(MyApp.getInstance());
        this.name = "";
        this.note = "";
        this.isPending = true;
    }

    @NonNull
    public String getId() {
        return this.id;
    }

    public void setId(@NonNull String str) {
        this.id = str;
    }

    public String getEventId() {
        return this.eventId;
    }

    public void setEventId(String str) {
        this.eventId = str;
    }

    @Bindable
    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String str) {
        this.categoryId = str;
        notifyChange();
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
    public String getNote() {
        return this.note;
    }

    public void setNote(String str) {
        this.note = str;
        notifyChange();
    }

    @Bindable
    public long getDateInMillis() {
        return this.dateInMillis;
    }

    public void setDateInMillis(long j) {
        this.dateInMillis = j;
        notifyChange();
    }

    @Bindable
    public boolean isPending() {
        return this.isPending;
    }

    public void setPending(boolean z) {
        this.isPending = z;
        notifyChange();
    }

    @Bindable
    public CategoryRowModel getCategoryRowModel() {
        return this.categoryRowModel;
    }

    public void setCategoryRowModel(CategoryRowModel categoryRowModel2) {
        this.categoryRowModel = categoryRowModel2;
        notifyChange();
    }

    @Bindable
    public ArrayList<SubTaskRowModel> getArrayList() {
        return this.arrayList;
    }

    public void setArrayList(ArrayList<SubTaskRowModel> arrayList2) {
        this.arrayList = arrayList2;
        notifyChange();
    }

    public void setEdit(boolean z) {
        this.isEdit = z;
        notifyChange();
    }

    public String getDateFormatted() {
        return AppConstants.getFormattedDate(getDateInMillis(), Constants.DATE_FORMAT_DATE_DB);
    }

    public String getStatusText() {
        return !this.isPending ? "Done" : "Pending";
    }

    public int getStatusColor() {
        if (getStatusText().equalsIgnoreCase("Done")) {
            return MyApp.getInstance().getApplicationContext().getResources().getColor(R.color.green);
        }
        return MyApp.getInstance().getApplicationContext().getResources().getColor(R.color.red);
    }

    public String getSubTaskText() {
        int i;
        if (getArrayList() == null || getArrayList().size() <= 0) {
            return 0 + InternalZipConstants.ZIP_FILE_SEPARATOR + 0;
        }
        if (getArrayList() == null || getArrayList().size() <= 0) {
            i = 0;
        } else {
            i = 0;
            for (int i2 = 0; i2 < getArrayList().size(); i2++) {
                if (!getArrayList().get(i2).isPending()) {
                    i++;
                }
            }
        }
        return i + InternalZipConstants.ZIP_FILE_SEPARATOR + getArrayList().size();
    }

    public boolean isListData() {
        return getArrayList() != null && getArrayList().size() > 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.categoryId);
        parcel.writeString(this.name);
        parcel.writeString(this.note);
        parcel.writeLong(this.dateInMillis);
        parcel.writeByte(this.isPending ? (byte) 1 : 0);
        parcel.writeParcelable(this.categoryRowModel, i);
        parcel.writeTypedList(this.arrayList);
        parcel.writeByte(this.isEdit ? (byte) 1 : 0);
    }

    protected TaskRowModel(Parcel parcel) {
        this.eventId = AppPref.getCurrentEvenId(MyApp.getInstance());
        this.name = "";
        this.note = "";
        boolean z = true;
        this.isPending = true;
        this.id = parcel.readString();
        this.categoryId = parcel.readString();
        this.name = parcel.readString();
        this.note = parcel.readString();
        this.dateInMillis = parcel.readLong();
        this.isPending = parcel.readByte() != 0;
        this.categoryRowModel = (CategoryRowModel) parcel.readParcelable(CategoryRowModel.class.getClassLoader());
        this.arrayList = parcel.createTypedArrayList(SubTaskRowModel.CREATOR);
        this.isEdit = parcel.readByte() == 0 ? false : z;
    }
}
