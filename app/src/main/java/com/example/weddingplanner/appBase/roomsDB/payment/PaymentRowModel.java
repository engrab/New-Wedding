package com.example.weddingplanner.appBase.roomsDB.payment;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.example.weddingplanner.appBase.utils.AppConstants;
import com.example.weddingplanner.appBase.utils.Constants;

@Entity(tableName = "paymentList")
public class PaymentRowModel extends BaseObservable implements Parcelable {
    public static final Creator<PaymentRowModel> CREATOR = new Creator<PaymentRowModel>() {
        public PaymentRowModel createFromParcel(Parcel parcel) {
            return new PaymentRowModel(parcel);
        }

        public PaymentRowModel[] newArray(int i) {
            return new PaymentRowModel[i];
        }
    };
    private double amount = 0.0d;
    private long dateInMillis;
    @PrimaryKey
    @NonNull


    private String id;
    private boolean isPending = true;
    private String name = "";
    private String parentId;
    private int type = 1;

    public int describeContents() {
        return 0;
    }

    public PaymentRowModel() {
    }

    @NonNull
    public String getId() {
        return this.id;
    }

    public void setId(@NonNull String str) {
        this.id = str;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String str) {
        this.parentId = str;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    @Bindable
    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
        notifyChange();
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double d) {
        this.amount = d;
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

    public String getAmountFormatted() {
        return AppConstants.getFormattedPrice(getAmount());
    }

    public String getDateFormatted() {
        return AppConstants.getFormattedDate(getDateInMillis(), Constants.DATE_FORMAT_DATE_DB);
    }

    public String getStatusText() {
        return this.isPending ? "Pending" : "Paid";
    }

    protected PaymentRowModel(Parcel parcel) {
        boolean z = true;
        this.id = parcel.readString();
        this.parentId = parcel.readString();
        this.type = parcel.readInt();
        this.name = parcel.readString();
        this.amount = parcel.readDouble();
        this.dateInMillis = parcel.readLong();
        this.isPending = parcel.readByte() == 0 ? false : z;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.parentId);
        parcel.writeInt(this.type);
        parcel.writeString(this.name);
        parcel.writeDouble(this.amount);
        parcel.writeLong(this.dateInMillis);
        parcel.writeByte(this.isPending ? (byte) 1 : 0);
    }
}
