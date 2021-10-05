package com.msint.weddingplanner.appBase.models.profile;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import android.os.Parcel;
import android.os.Parcelable;
import com.msint.weddingplanner.appBase.utils.AppConstants;
import com.msint.weddingplanner.appBase.utils.Constants;

@Entity(tableName = "profileList")
public class ProfileRowModel extends BaseObservable implements Parcelable {
    public static final Creator<ProfileRowModel> CREATOR = new Creator<ProfileRowModel>() {
        public ProfileRowModel createFromParcel(Parcel parcel) {
            return new ProfileRowModel(parcel);
        }

        public ProfileRowModel[] newArray(int i) {
            return new ProfileRowModel[i];
        }
    };
    private String address;
    private double budget;
    private long dateTimeInMillis;
    private String emailId;
    private int genderType;
    @PrimaryKey
    @NonNull

    /* renamed from: id */
    private String f530id;
    private boolean isSelected;
    private String name;
    private String partnerAddress;
    private String partnerEmailId;
    private int partnerGenderType;
    private String partnerName;
    private String partnerPhoneNo;
    private String phoneNo;
    private String weddingName;

    public int describeContents() {
        return 0;
    }

    public ProfileRowModel(ProfileRowModel profileRowModel) {
        this.name = "Groom";
        this.phoneNo = "";
        this.address = "";
        this.emailId = "";
        this.partnerName = "Bride";
        this.partnerPhoneNo = "";
        this.partnerAddress = "";
        this.partnerEmailId = "";
        this.weddingName = "";
        this.genderType = 1;
        this.partnerGenderType = 2;
        this.dateTimeInMillis = 0;
        this.budget = 0.0d;
        this.name = profileRowModel.name;
        this.phoneNo = profileRowModel.phoneNo;
        this.address = profileRowModel.address;
        this.emailId = profileRowModel.emailId;
        this.partnerName = profileRowModel.partnerName;
        this.partnerPhoneNo = profileRowModel.partnerPhoneNo;
        this.partnerAddress = profileRowModel.partnerAddress;
        this.partnerEmailId = profileRowModel.partnerEmailId;
        this.weddingName = profileRowModel.weddingName;
        this.genderType = profileRowModel.genderType;
        this.partnerGenderType = profileRowModel.partnerGenderType;
        this.dateTimeInMillis = profileRowModel.dateTimeInMillis;
        this.budget = profileRowModel.budget;
    }

    public ProfileRowModel() {
        this.name = "Groom";
        this.phoneNo = "";
        this.address = "";
        this.emailId = "";
        this.partnerName = "Bride";
        this.partnerPhoneNo = "";
        this.partnerAddress = "";
        this.partnerEmailId = "";
        this.weddingName = "";
        this.genderType = 1;
        this.partnerGenderType = 2;
        this.dateTimeInMillis = 0;
        this.budget = 0.0d;
    }

    @NonNull
    public String getId() {
        return this.f530id;
    }

    public void setId(@NonNull String str) {
        this.f530id = str;
    }

    @Bindable
    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
        notifyChange();
    }

    public String getPhoneNo() {
        return this.phoneNo;
    }

    public void setPhoneNo(String str) {
        this.phoneNo = str;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public void setEmailId(String str) {
        this.emailId = str;
    }

    @Bindable
    public String getPartnerName() {
        return this.partnerName;
    }

    public void setPartnerName(String str) {
        this.partnerName = str;
        notifyChange();
    }

    public String getPartnerPhoneNo() {
        return this.partnerPhoneNo;
    }

    public void setPartnerPhoneNo(String str) {
        this.partnerPhoneNo = str;
    }

    public String getPartnerAddress() {
        return this.partnerAddress;
    }

    public void setPartnerAddress(String str) {
        this.partnerAddress = str;
    }

    public String getPartnerEmailId() {
        return this.partnerEmailId;
    }

    public void setPartnerEmailId(String str) {
        this.partnerEmailId = str;
    }

    @Bindable
    public String getWeddingName() {
        return this.weddingName;
    }

    public void setWeddingName(String str) {
        this.weddingName = str;
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
    public int getPartnerGenderType() {
        return this.partnerGenderType;
    }

    public void setPartnerGenderType(int i) {
        this.partnerGenderType = i;
        notifyChange();
    }

    @Bindable
    public long getDateTimeInMillis() {
        return this.dateTimeInMillis;
    }

    public void setDateTimeInMillis(long j) {
        this.dateTimeInMillis = j;
        notifyChange();
    }

    @Bindable
    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
        notifyChange();
    }

    public double getBudget() {
        return this.budget;
    }

    public void setBudget(double d) {
        this.budget = d;
    }

    public String getDateFormatted() {
        return AppConstants.getFormattedDate(getDateTimeInMillis(), Constants.DATE_FORMAT_DATE_DB);
    }

    public String getTimeFormatted() {
        return AppConstants.getFormattedDate(getDateTimeInMillis(), Constants.DATE_FORMAT_TIME);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f530id);
        parcel.writeString(this.name);
        parcel.writeString(this.phoneNo);
        parcel.writeString(this.address);
        parcel.writeString(this.emailId);
        parcel.writeString(this.partnerName);
        parcel.writeString(this.partnerPhoneNo);
        parcel.writeString(this.partnerAddress);
        parcel.writeString(this.partnerEmailId);
        parcel.writeString(this.weddingName);
        parcel.writeInt(this.genderType);
        parcel.writeInt(this.partnerGenderType);
        parcel.writeLong(this.dateTimeInMillis);
        parcel.writeDouble(this.budget);
        parcel.writeByte(this.isSelected ? (byte) 1 : 0);
    }

    protected ProfileRowModel(Parcel parcel) {
        this.name = "Groom";
        this.phoneNo = "";
        this.address = "";
        this.emailId = "";
        this.partnerName = "Bride";
        this.partnerPhoneNo = "";
        this.partnerAddress = "";
        this.partnerEmailId = "";
        this.weddingName = "";
        boolean z = true;
        this.genderType = 1;
        this.partnerGenderType = 2;
        this.dateTimeInMillis = 0;
        this.budget = 0.0d;
        this.f530id = parcel.readString();
        this.name = parcel.readString();
        this.phoneNo = parcel.readString();
        this.address = parcel.readString();
        this.emailId = parcel.readString();
        this.partnerName = parcel.readString();
        this.partnerPhoneNo = parcel.readString();
        this.partnerAddress = parcel.readString();
        this.partnerEmailId = parcel.readString();
        this.weddingName = parcel.readString();
        this.genderType = parcel.readInt();
        this.partnerGenderType = parcel.readInt();
        this.dateTimeInMillis = parcel.readLong();
        this.budget = parcel.readDouble();
        this.isSelected = parcel.readByte() == 0 ? false : z;
    }
}
