package com.example.weddingplanner.allLeading.roomDatabase.vendor;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.example.weddingplanner.R;
import com.example.weddingplanner.allLeading.MyApp;
import com.example.weddingplanner.allLeading.appPref.AppPrefLeading;
import com.example.weddingplanner.allLeading.roomDatabase.category.CategoryRowModel;
import com.example.weddingplanner.allLeading.roomDatabase.payment.PaymentRowModel;
import com.example.weddingplanner.allLeading.utils.AppConstants;
import java.util.ArrayList;
import net.lingala.zip4j.util.InternalZipConstants;

@Entity(tableName = "vendorList")
public class VendorRowModel extends BaseObservable implements Parcelable {
    public static final Creator<VendorRowModel> CREATOR = new Creator<VendorRowModel>() {
        public VendorRowModel createFromParcel(Parcel parcel) {
            return new VendorRowModel(parcel);
        }

        public VendorRowModel[] newArray(int i) {
            return new VendorRowModel[i];
        }
    };
    private String address = "";
    @Ignore
    private ArrayList<PaymentRowModel> arrayList;
    private String categoryId;
    @Ignore
    private CategoryRowModel categoryRowModel;
    private String emailId = "";
    private String eventId = AppPrefLeading.getCurrentEvenId(MyApp.getInstance());
    private double expectedAmount = 0.0d;
    @PrimaryKey
    @NonNull


    private String id;
    private boolean isAddToBudget;
    @Ignore
    private boolean isEdit;
    @Ignore
    private boolean isExpanded;
    @Ignore
    private boolean isExpandedContact;
    private String name = "";
    private String note = "";
    @Ignore
    private double paidAmount = 0.0d;
    @Ignore
    private double pendingAmount = 0.0d;
    private String phoneNo = "";
    private int status = 1;
    private String webSite = "";

    public int describeContents() {
        return 0;
    }

    @Bindable
    public boolean isEdit() {
        return true;
    }

    public VendorRowModel() {
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
    public String getWebSite() {
        return this.webSite;
    }

    public void setWebSite(String str) {
        this.webSite = str;
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
    public double getExpectedAmount() {
        return this.expectedAmount;
    }

    public void setExpectedAmount(double d) {
        this.expectedAmount = d;
        notifyChange();
    }

    @Bindable
    public boolean isAddToBudget() {
        return this.isAddToBudget;
    }

    public void setAddToBudget(boolean z) {
        this.isAddToBudget = z;
        notifyChange();
    }

    @Bindable
    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
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

    public void setEdit(boolean z) {
        this.isEdit = z;
        notifyChange();
    }

    @Bindable
    public ArrayList<PaymentRowModel> getArrayList() {
        return this.arrayList;
    }

    public void setArrayList(ArrayList<PaymentRowModel> arrayList2) {
        this.arrayList = arrayList2;
        notifyChange();
    }

    @Bindable
    public double getPendingAmount() {
        return this.pendingAmount;
    }

    public void setPendingAmount(double d) {
        this.pendingAmount = d;
        notifyChange();
    }

    @Bindable
    public double getPaidAmount() {
        return this.paidAmount;
    }

    public void setPaidAmount(double d) {
        this.paidAmount = d;
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

    public String getExpectedAmountFormatted() {
        return AppConstants.getFormattedPrice(getExpectedAmount());
    }

    public String getPendingAmountFormatted() {
        return AppConstants.getFormattedPrice(getPendingAmount());
    }

    public String getPaidAmountFormatted() {
        return AppConstants.getFormattedPrice(getPaidAmount());
    }

    public String getBalanceFormatted() {
        double expectedAmount2 = getExpectedAmount() - (getPendingAmount() + getPaidAmount());
        StringBuilder sb = new StringBuilder();
        sb.append(expectedAmount2 > 1.0d ? "+" : "");
        sb.append(AppConstants.getFormattedPriceForMinus(expectedAmount2));
        return sb.toString();
    }

    public boolean isListData() {
        return getArrayList() != null && getArrayList().size() > 0;
    }

    public boolean isPending() {
        if (getArrayList() == null || getArrayList().size() <= 0) {
            return true;
        }
        for (int i = 0; i < getArrayList().size(); i++) {
            if (getArrayList().get(i).isPending()) {
                return true;
            }
        }
        return false;
    }

    public String getStatusText() {
        if (getArrayList() == null || getArrayList().size() <= 0) {
            return "Pending";
        }
        for (int i = 0; i < getArrayList().size(); i++) {
            if (getArrayList().get(i).isPending()) {
                return "Pending";
            }
        }
        return "Paid";
    }

    public int getStatusColor() {
        if (getStatusText().equalsIgnoreCase("Paid")) {
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

    protected VendorRowModel(Parcel parcel) {
        this.id = parcel.readString();
        this.categoryId = parcel.readString();
        this.name = parcel.readString();
        this.note = parcel.readString();
        this.phoneNo = parcel.readString();
        this.emailId = parcel.readString();
        this.webSite = parcel.readString();
        this.address = parcel.readString();
        this.expectedAmount = parcel.readDouble();
        boolean z = false;
        this.isAddToBudget = parcel.readByte() != 0;
        this.status = parcel.readInt();
        this.categoryRowModel = (CategoryRowModel) parcel.readParcelable(CategoryRowModel.class.getClassLoader());
        this.isEdit = parcel.readByte() != 0;
        this.arrayList = parcel.createTypedArrayList(PaymentRowModel.CREATOR);
        this.pendingAmount = parcel.readDouble();
        this.paidAmount = parcel.readDouble();
        this.isExpanded = parcel.readByte() != 0 ? true : z;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.categoryId);
        parcel.writeString(this.name);
        parcel.writeString(this.note);
        parcel.writeString(this.phoneNo);
        parcel.writeString(this.emailId);
        parcel.writeString(this.webSite);
        parcel.writeString(this.address);
        parcel.writeDouble(this.expectedAmount);
        parcel.writeByte(this.isAddToBudget ? (byte) 1 : 0);
        parcel.writeInt(this.status);
        parcel.writeParcelable(this.categoryRowModel, i);
        parcel.writeByte(this.isEdit ? (byte) 1 : 0);
        parcel.writeTypedList(this.arrayList);
        parcel.writeDouble(this.pendingAmount);
        parcel.writeDouble(this.paidAmount);
        parcel.writeByte(this.isExpanded ? (byte) 1 : 0);
    }
}
