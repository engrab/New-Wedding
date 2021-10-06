package com.example.weddingplanner.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginData implements Parcelable {
    public static final Creator<LoginData> CREATOR = new Creator<LoginData>() {

        @Override
        public LoginData createFromParcel(Parcel parcel) {
            return new LoginData(parcel);
        }

        @Override
        public LoginData[] newArray(int i) {
            return new LoginData[i];
        }
    };
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("login_from")
    @Expose
    private String loginFrom;
    @SerializedName("otpcode")
    @Expose
    private String otpcode;
    @SerializedName("partner_email_id")
    @Expose
    private String partnerEmailId;
    @SerializedName("partner_gender")
    @Expose
    private String partnerGender;
    @SerializedName("partner_name")
    @Expose
    private String partnerName;
    @SerializedName("photourl")
    @Expose
    private String photourl;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_gender")
    @Expose
    private String userGender;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_pass")
    @Expose
    private String userPass;
    @SerializedName("usercreated_timestamp")
    @Expose
    private String usercreatedTimestamp;
    @SerializedName("usermodified_timestamp")
    @Expose
    private String usermodifiedTimestamp;
    @SerializedName("valid_timestamp")
    @Expose
    private String validTimestamp;

    public int describeContents() {
        return 0;
    }

    public LoginData(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14) {
        this.userEmail = str;
        this.userName = str2;
        this.photourl = str3;
        this.userGender = str4;
        this.partnerName = str5;
        this.partnerGender = str6;
        this.partnerEmailId = str7;
        this.usercreatedTimestamp = str8;
        this.usermodifiedTimestamp = str9;
        this.isActive = str10;
        this.loginFrom = str11;
        this.userPass = str12;
        this.otpcode = str13;
        this.validTimestamp = str14;
    }

    protected LoginData(Parcel parcel) {
        this.userEmail = parcel.readString();
        this.userName = parcel.readString();
        this.photourl = parcel.readString();
        this.userGender = parcel.readString();
        this.partnerName = parcel.readString();
        this.partnerGender = parcel.readString();
        this.partnerEmailId = parcel.readString();
        this.usercreatedTimestamp = parcel.readString();
        this.usermodifiedTimestamp = parcel.readString();
        this.isActive = parcel.readString();
        this.loginFrom = parcel.readString();
        this.userPass = parcel.readString();
        this.otpcode = parcel.readString();
        this.validTimestamp = parcel.readString();
    }

    public LoginData(String str, String str2, String str3) {
        this.userEmail = str;
        this.userName = str2;
        this.loginFrom = str3;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String str) {
        this.userEmail = str;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String str) {
        this.userName = str;
    }

    public String getPhotourl() {
        return this.photourl;
    }

    public void setPhotourl(String str) {
        this.photourl = str;
    }

    public String getUserGender() {
        return this.userGender;
    }

    public void setUserGender(String str) {
        this.userGender = str;
    }

    public String getPartnerName() {
        return this.partnerName;
    }

    public void setPartnerName(String str) {
        this.partnerName = str;
    }

    public String getPartnerGender() {
        return this.partnerGender;
    }

    public void setPartnerGender(String str) {
        this.partnerGender = str;
    }

    public String getPartnerEmailId() {
        return this.partnerEmailId;
    }

    public void setPartnerEmailId(String str) {
        this.partnerEmailId = str;
    }

    public String getUsercreatedTimestamp() {
        return this.usercreatedTimestamp;
    }

    public void setUsercreatedTimestamp(String str) {
        this.usercreatedTimestamp = str;
    }

    public String getUsermodifiedTimestamp() {
        return this.usermodifiedTimestamp;
    }

    public void setUsermodifiedTimestamp(String str) {
        this.usermodifiedTimestamp = str;
    }

    public String getIsActive() {
        return this.isActive;
    }

    public void setIsActive(String str) {
        this.isActive = str;
    }

    public String getLoginFrom() {
        return this.loginFrom;
    }

    public void setLoginFrom(String str) {
        this.loginFrom = str;
    }

    public String getUserPass() {
        return this.userPass;
    }

    public void setUserPass(String str) {
        this.userPass = str;
    }

    public String getOtpcode() {
        return this.otpcode;
    }

    public void setOtpcode(String str) {
        this.otpcode = str;
    }

    public String getValidTimestamp() {
        return this.validTimestamp;
    }

    public void setValidTimestamp(String str) {
        this.validTimestamp = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.userEmail);
        parcel.writeString(this.userName);
        parcel.writeString(this.photourl);
        parcel.writeString(this.userGender);
        parcel.writeString(this.partnerName);
        parcel.writeString(this.partnerGender);
        parcel.writeString(this.partnerEmailId);
        parcel.writeString(this.usercreatedTimestamp);
        parcel.writeString(this.usermodifiedTimestamp);
        parcel.writeString(this.isActive);
        parcel.writeString(this.loginFrom);
        parcel.writeString(this.userPass);
        parcel.writeString(this.otpcode);
        parcel.writeString(this.validTimestamp);
    }

    public String PartnerName() {
        String str = this.partnerName;
        return (str == null || str.equals("")) ? " - " : this.partnerName;
    }

    public String PartnerGender() {
        String str = this.partnerGender;
        return (str == null || str.equals("")) ? " - " : this.partnerGender;
    }

    public String PartnerEmail() {
        String str = this.partnerEmailId;
        return (str == null || str.equals("")) ? " - " : this.partnerEmailId;
    }
}
