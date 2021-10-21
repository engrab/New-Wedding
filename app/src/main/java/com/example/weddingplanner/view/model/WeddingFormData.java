package com.example.weddingplanner.view.model;


import com.example.weddingplanner.all.MyApp;

public class WeddingFormData {
    private String access_budget = "";
    private String access_collaborators = "";
    private String access_guests = "";
    private String access_setting = "";
    private String access_tasks = "";
    private String access_vendors = "";
    private String address;
    private String collaboration_code;
    private String currency = "USD";
    private String expireTime = "0";
    private String is_purchase_active = "0";
    private String last_activation_time = "0";
    private String orderId = "";
    private String partner_email_id;
    private String partner_gender;
    private String partner_name;
    private String purchaseTime = "0";
    private String purchaseToken = "";
    private String purchase_createTime = "0";
    private String subscriptionId = "";
    private String token;
    private String total_budget = "0";
    private String update_datetime = "0";
    private String user_email;
    private String user_gender;
    private String user_name;
    private String user_type = "";
    private String wedding_cover_image;
    private String wedding_datetime;
    private String wedding_id;
    private String wedding_name;

    public WeddingFormData() {
    }

    public WeddingFormData(String user_name, String partner_name, String user_gender, String partner_gender,
                           String partner_email_id, String wedding_name, String wedding_datetime, String address, String total_buget,
                           String currency) {

        this.user_name = user_name;
        this.partner_name = partner_name;
        this.user_gender = user_gender;
        this.partner_gender = partner_gender;
        this.partner_email_id = partner_email_id;
        this.wedding_name = wedding_name;
        this.wedding_datetime = wedding_datetime;
        this.address = address;
        this.total_budget = total_buget;
        this.currency = currency;

    }

    public String getUser_email() {
        return this.user_email;
    }

    public void setUser_email(String str) {
        this.user_email = str;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String str) {
        this.user_name = str;
    }

    public String getPartner_name() {
        return this.partner_name;
    }

    public void setPartner_name(String str) {
        this.partner_name = str;
    }

    public String getUser_gender() {
        return this.user_gender;
    }

    public void setUser_gender(String str) {
        this.user_gender = str;
    }

    public String getPartner_gender() {
        return this.partner_gender;
    }

    public void setPartner_gender(String str) {
        this.partner_gender = str;
    }

    public String getPartner_email_id() {
        return this.partner_email_id;
    }

    public void setPartner_email_id(String str) {
        this.partner_email_id = str;
    }

    public String getWedding_name() {
        return this.wedding_name;
    }

    public void setWedding_name(String str) {
        this.wedding_name = str;
    }

    public String getWedding_datetime() {
        return this.wedding_datetime;
    }

    public void setWedding_datetime(String str) {
        this.wedding_datetime = str;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public String getTotal_budget() {
        return this.total_budget;
    }

    public void setTotal_budget(String str) {
        this.total_budget = str;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String str) {
        this.currency = str;
    }

    public String getCollaboration_code() {
        return this.collaboration_code;
    }

    public void setCollaboration_code(String str) {
        this.collaboration_code = str;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String str) {
        this.token = str;
    }

    public String getWedding_id() {
        return this.wedding_id;
    }

    public void setWedding_id(String str) {
        this.wedding_id = str;
    }

    public String getSubscriptionId() {
        return this.subscriptionId;
    }

    public void setSubscriptionId(String str) {
        this.subscriptionId = str;
    }

    public String getPurchaseToken() {
        return this.purchaseToken;
    }

    public void setPurchaseToken(String str) {
        this.purchaseToken = str;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String str) {
        this.orderId = str;
    }

    public String getPurchaseTime() {
        return this.purchaseTime;
    }

    public void setPurchaseTime(String str) {
        this.purchaseTime = str;
    }

    public String getExpireTime() {
        return this.expireTime;
    }

    public void setExpireTime(String str) {
        this.expireTime = str;
    }

    public String getPurchase_createTime() {
        return this.purchase_createTime;
    }

    public void setPurchase_createTime(String str) {
        this.purchase_createTime = str;
    }

    public String getIs_purchase_active() {
        return this.is_purchase_active;
    }

    public void setIs_purchase_active(String str) {
        this.is_purchase_active = str;
    }

    public String getLast_activation_time() {
        return this.last_activation_time;
    }

    public void setLast_activation_time(String str) {
        this.last_activation_time = str;
    }

    public String getAccess_tasks() {
        return this.access_tasks;
    }

    public void setAccess_tasks(String str) {
        this.access_tasks = str;
    }

    public String getAccess_budget() {
        return this.access_budget;
    }

    public void setAccess_budget(String str) {
        this.access_budget = str;
    }

    public String getAccess_guests() {
        return this.access_guests;
    }

    public void setAccess_guests(String str) {
        this.access_guests = str;
    }

    public String getAccess_vendors() {
        return this.access_vendors;
    }

    public void setAccess_vendors(String str) {
        this.access_vendors = str;
    }

    public String getAccess_collaborators() {
        return this.access_collaborators;
    }

    public void setAccess_collaborators(String str) {
        this.access_collaborators = str;
    }

    public String getAccess_setting() {
        return this.access_setting;
    }

    public void setAccess_setting(String str) {
        this.access_setting = str;
    }

    public String getUser_type() {
        return this.user_type;
    }

    public void setUser_type(String str) {
        this.user_type = str;
    }

    public String getUpdate_datetime() {
        return this.update_datetime;
    }

    public void setUpdate_datetime(String str) {
        this.update_datetime = str;
    }

    public String getWedding_cover_image() {
        return this.wedding_cover_image;
    }

    public void setWedding_cover_image(String str) {
        this.wedding_cover_image = str;
    }

    public String Date() {
        return ConstantData.getLongToStringDate(Long.valueOf(Long.parseLong(this.wedding_datetime)));
    }

    public String Time() {
        return ConstantData.getLongToStringTimeAMPM(Long.valueOf(Long.parseLong(this.wedding_datetime)));
    }

    public String Address() {
        String str = this.address;
        return (str == null || str.equals("")) ? " - " : this.address;
    }

    public String WeddingBudget() {
        String str = this.total_budget;
        if (str == null || str.equals("")) {
            return ConstantData.getFormatedAmount(MyApp.getInstance(), 0.0d);
        }
        return ConstantData.getFormatedAmount(MyApp.getInstance(), Double.parseDouble(this.total_budget));
    }
}
