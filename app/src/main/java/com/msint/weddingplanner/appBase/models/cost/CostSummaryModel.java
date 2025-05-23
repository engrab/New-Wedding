package com.msint.weddingplanner.appBase.models.cost;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.msint.weddingplanner.appBase.roomsDB.category.CategoryRowModel;
import com.msint.weddingplanner.appBase.utils.AppConstants;
import net.lingala.zip4j.util.InternalZipConstants;

public class CostSummaryModel extends BaseObservable {
    private String categoryId;
    private CategoryRowModel categoryRowModel;
    private double completedCost;
    private long completedPayment;
    private double pendingCost;
    private long pendingPayment;
    private double totalBudget;
    private double totalCost;
    private long totalPayment;

    @Bindable
    public double getTotalBudget() {
        return this.totalBudget;
    }

    public void setTotalBudget(double d) {
        this.totalBudget = d;
        notifyChange();
    }

    @Bindable
    public double getTotalCost() {
        return this.totalCost;
    }

    public void setTotalCost(double d) {
        this.totalCost = d;
        notifyChange();
    }

    @Bindable
    public double getPendingCost() {
        return this.pendingCost;
    }

    public void setPendingCost(double d) {
        this.pendingCost = d;
        notifyChange();
    }

    @Bindable
    public double getCompletedCost() {
        return this.completedCost;
    }

    public void setCompletedCost(double d) {
        this.completedCost = d;
        notifyChange();
    }

    @Bindable
    public long getTotalPayment() {
        return this.totalPayment;
    }

    public void setTotalPayment(long j) {
        this.totalPayment = j;
        notifyChange();
    }

    @Bindable
    public long getPendingPayment() {
        return this.totalPayment - this.completedPayment;
    }

    public void setPendingPayment(long j) {
        this.pendingPayment = j;
        notifyChange();
    }

    @Bindable
    public long getCompletedPayment() {
        return this.completedPayment;
    }

    public void setCompletedPayment(long j) {
        this.completedPayment = j;
        notifyChange();
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
    public CategoryRowModel getCategoryRowModel() {
        return this.categoryRowModel;
    }

    public void setCategoryRowModel(CategoryRowModel categoryRowModel2) {
        this.categoryRowModel = categoryRowModel2;
        notifyChange();
    }

    public String getCosts() {
        return getCompletedCostFormatted() + InternalZipConstants.ZIP_FILE_SEPARATOR + getTotalCostFormatted();
    }

    public String getPayments() {
        return this.completedPayment + InternalZipConstants.ZIP_FILE_SEPARATOR + this.totalPayment;
    }

    public String getTotalBudgetFormatted() {
        return AppConstants.getFormattedPrice(this.totalBudget);
    }

    public String getTotalCostFormatted() {
        return AppConstants.getFormattedPrice(this.totalCost);
    }

    public String getPendingCostFormatted() {
        return AppConstants.getFormattedPrice(this.pendingCost);
    }

    public String getCompletedCostFormatted() {
        return AppConstants.getFormattedPrice(this.completedCost);
    }

    public String getBalanceFormatted() {
        double totalCost2 = getTotalCost() - (getPendingCost() + getCompletedCost());
        StringBuilder sb = new StringBuilder();
        sb.append(totalCost2 > 1.0d ? "+" : "");
        sb.append(AppConstants.getFormattedPriceForMinus(totalCost2));
        return sb.toString();
    }
}
