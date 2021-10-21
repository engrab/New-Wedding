package com.example.weddingplanner.view.currency;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrencyModel implements Parcelable {
    public static final Creator<CurrencyModel> CREATOR = new Creator<CurrencyModel>() {
        /* class com.selfmentor.myweddingplanner.model.currency.CurrencyModel.AnonymousClass1 */

        @Override // android.os.Parcelable.Creator
        public CurrencyModel createFromParcel(Parcel parcel) {
            return new CurrencyModel(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public CurrencyModel[] newArray(int i) {
            return new CurrencyModel[i];
        }
    };
    private String Currency;
    private String CurrencyName;
    private String CurrencySymbol;
    private boolean isSelect = false;

    public int describeContents() {
        return 0;
    }

    public CurrencyModel() {
    }

    public CurrencyModel(String str) {
        this.Currency = str;
    }

    public CurrencyModel(String str, String str2, String str3, boolean z) {
        this.Currency = str;
        this.CurrencySymbol = str2;
        this.CurrencyName = str3;
        this.isSelect = z;
    }

    public CurrencyModel(Parcel parcel) {
        boolean z = false;
        this.Currency = parcel.readString();
        this.CurrencySymbol = parcel.readString();
        this.CurrencyName = parcel.readString();
        this.isSelect = parcel.readByte() != 0 ? true : z;
    }

    public String getCurrency() {
        return this.Currency;
    }

    public void setCurrency(String str) {
        this.Currency = str;
    }

    public String getCurrencySymbol() {
        return this.CurrencySymbol;
    }

    public void setCurrencySymbol(String str) {
        this.CurrencySymbol = str;
    }

    public String getCurrencyName() {
        return this.CurrencyName;
    }

    public void setCurrencyName(String str) {
        this.CurrencyName = str;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean z) {
        this.isSelect = z;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && ((CurrencyModel) obj).getCurrency().equals(this.Currency);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.Currency);
        parcel.writeString(this.CurrencySymbol);
        parcel.writeString(this.CurrencyName);
        parcel.writeByte(this.isSelect ? (byte) 1 : 0);
    }
}
