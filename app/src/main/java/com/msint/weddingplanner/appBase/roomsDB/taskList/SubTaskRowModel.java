package com.msint.weddingplanner.appBase.roomsDB.taskList;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "subTaskList")
public class SubTaskRowModel extends BaseObservable implements Parcelable {
    public static final Creator<SubTaskRowModel> CREATOR = new Creator<SubTaskRowModel>() {
        public SubTaskRowModel createFromParcel(Parcel parcel) {
            return new SubTaskRowModel(parcel);
        }

        public SubTaskRowModel[] newArray(int i) {
            return new SubTaskRowModel[i];
        }
    };
    @PrimaryKey
    @NonNull

    /* renamed from: id */
    private String f537id;
    private boolean isPending;
    private String name;
    private String note;
    private String taskId;

    public int describeContents() {
        return 0;
    }

    public SubTaskRowModel() {
        this.name = "";
        this.note = "";
        this.isPending = true;
    }

    @NonNull
    public String getId() {
        return this.f537id;
    }

    public void setId(@NonNull String str) {
        this.f537id = str;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String str) {
        this.taskId = str;
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
    public boolean isPending() {
        return this.isPending;
    }

    public void setPending(boolean z) {
        this.isPending = z;
        notifyChange();
    }

    public String getStatusText() {
        return !this.isPending ? "Done" : "Pending";
    }

    protected SubTaskRowModel(Parcel parcel) {
        this.name = "";
        this.note = "";
        boolean z = true;
        this.isPending = true;
        this.f537id = parcel.readString();
        this.taskId = parcel.readString();
        this.name = parcel.readString();
        this.note = parcel.readString();
        this.isPending = parcel.readByte() == 0 ? false : z;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f537id);
        parcel.writeString(this.taskId);
        parcel.writeString(this.name);
        parcel.writeString(this.note);
        parcel.writeByte(this.isPending ? (byte) 1 : 0);
    }
}
