package com.msint.weddingplanner.appBase.roomsDB.guest;

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
import java.util.ArrayList;

@Entity(tableName = "guestList")
public class GuestRowModel extends BaseObservable implements Parcelable {
    public static final Creator<GuestRowModel> CREATOR = new Creator<GuestRowModel>() {
        public GuestRowModel createFromParcel(Parcel parcel) {
            return new GuestRowModel(parcel);
        }

        public GuestRowModel[] newArray(int i) {
            return new GuestRowModel[i];
        }
    };
    private String address;
    @Ignore
    private ArrayList<GuestRowModel> arrayList;
    @Ignore
    private long companions;
    private String emailId;
    private String eventId;
    private int genderType;
    private String guestId;
    @PrimaryKey
    @NonNull


    private String id;
    private boolean isCompanion;
    @Ignore
    private boolean isEdit;
    @Ignore
    private boolean isExpanded;
    @Ignore
    private boolean isExpandedContact;
    private boolean isInvitationSent;
    private String name;
    private String note;
    private String phoneNo;
    private int stageType;

    public int describeContents() {
        return 0;
    }

    @Bindable
    public boolean isEdit() {
        return true;
    }

    public GuestRowModel() {
        this.eventId = AppPref.getCurrentEvenId(MyApp.getInstance());
        this.name = "";
        this.genderType = 1;
        this.stageType = 1;
        this.note = "";
        this.phoneNo = "";
        this.emailId = "";
        this.address = "";
        this.guestId = "";
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
    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
        notifyChange();
    }

    @Bindable
    public int getGenderType() {
        return this.genderType;
    }

    public void setGenderType(int i) {
        this.genderType = i;
        notifyChange();
    }

    @Bindable
    public int getStageType() {
        return this.stageType;
    }

    public void setStageType(int i) {
        this.stageType = i;
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
    public String getPhoneNo() {
        return this.phoneNo;
    }

    public void setPhoneNo(String str) {
        this.phoneNo = str;
        notifyChange();
    }

    @Bindable
    public String getEmailId() {
        return this.emailId;
    }

    public void setEmailId(String str) {
        this.emailId = str;
        notifyChange();
    }

    @Bindable
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String str) {
        this.address = str;
        notifyChange();
    }

    @Bindable
    public boolean isInvitationSent() {
        return this.isInvitationSent;
    }

    public void setInvitationSent(boolean z) {
        this.isInvitationSent = z;
        notifyChange();
    }

    public boolean isCompanion() {
        return this.isCompanion;
    }

    public void setCompanion(boolean z) {
        this.isCompanion = z;
    }

    public String getGuestId() {
        return this.guestId;
    }

    public void setGuestId(String str) {
        this.guestId = str;
    }

    public void setEdit(boolean z) {
        this.isEdit = z;
        notifyChange();
    }

    @Bindable
    public ArrayList<GuestRowModel> getArrayList() {
        return this.arrayList;
    }

    public void setArrayList(ArrayList<GuestRowModel> arrayList2) {
        this.arrayList = arrayList2;
        notifyChange();
    }

    @Bindable
    public boolean isExpanded() {
        return this.isExpanded;
    }

    public void setExpanded(boolean z) {
        this.isExpanded = z;
        notifyChange();
    }

    @Bindable
    public boolean isExpandedContact() {
        return this.isExpandedContact;
    }

    public void setExpandedContact(boolean z) {
        this.isExpandedContact = z;
        notifyChange();
    }

    public long getCompanions() {
        return this.companions;
    }

    public void setCompanions(long j) {
        this.companions = j;
    }

    public boolean isListData() {
        return getArrayList() != null && getArrayList().size() > 0;
    }

    public String getInvitationText() {
        return this.isInvitationSent ? "Invitation sent" : "Invitation not send";
    }

    public int getStatusColor() {
        if (getInvitationText().equalsIgnoreCase("Invitation sent")) {
            return MyApp.getInstance().getApplicationContext().getResources().getColor(R.color.green);
        }
        return MyApp.getInstance().getApplicationContext().getResources().getColor(R.color.red);
    }

    public String getCurrentIdPref() {
        return AppPref.getCurrentEvenId(MyApp.getInstance());
    }

    protected GuestRowModel(Parcel parcel) {
        this.eventId = AppPref.getCurrentEvenId(MyApp.getInstance());
        this.name = "";
        boolean z = true;
        this.genderType = 1;
        this.stageType = 1;
        this.note = "";
        this.phoneNo = "";
        this.emailId = "";
        this.address = "";
        this.guestId = "";
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.genderType = parcel.readInt();
        this.stageType = parcel.readInt();
        this.note = parcel.readString();
        this.phoneNo = parcel.readString();
        this.emailId = parcel.readString();
        this.address = parcel.readString();
        this.isInvitationSent = parcel.readByte() != 0;
        this.isCompanion = parcel.readByte() != 0;
        this.guestId = parcel.readString();
        this.isEdit = parcel.readByte() != 0;
        this.arrayList = parcel.createTypedArrayList(CREATOR);
        this.isExpanded = parcel.readByte() != 0;
        this.isExpandedContact = parcel.readByte() == 0 ? false : z;
        this.companions = parcel.readLong();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.name);
        parcel.writeInt(this.genderType);
        parcel.writeInt(this.stageType);
        parcel.writeString(this.note);
        parcel.writeString(this.phoneNo);
        parcel.writeString(this.emailId);
        parcel.writeString(this.address);
        parcel.writeByte(this.isInvitationSent ? (byte) 1 : 0);
        parcel.writeByte(this.isCompanion ? (byte) 1 : 0);
        parcel.writeString(this.guestId);
        parcel.writeByte(this.isEdit ? (byte) 1 : 0);
        parcel.writeTypedList(this.arrayList);
        parcel.writeByte(this.isExpanded ? (byte) 1 : 0);
        parcel.writeByte(this.isExpandedContact ? (byte) 1 : 0);
        parcel.writeLong(this.companions);
    }
}
