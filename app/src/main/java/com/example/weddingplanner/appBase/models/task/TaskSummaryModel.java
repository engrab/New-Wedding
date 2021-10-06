package com.example.weddingplanner.appBase.models.task;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.example.weddingplanner.appBase.roomsDB.category.CategoryRowModel;
import net.lingala.zip4j.util.InternalZipConstants;

public class TaskSummaryModel extends BaseObservable {
    private String categoryId;
    private CategoryRowModel categoryRowModel;
    private long completedSubTask;
    private long completedTask;
    private long pendingSubTask;
    private long pendingTask;
    private long totalSubTask;
    private long totalTask;

    @Bindable
    public long getTotalTask() {
        return this.totalTask;
    }

    public void setTotalTask(long j) {
        this.totalTask = j;
        notifyChange();
    }

    @Bindable
    public long getPendingTask() {
        return this.pendingTask;
    }

    public void setPendingTask(long j) {
        this.pendingTask = j;
        notifyChange();
    }

    @Bindable
    public long getCompletedTask() {
        return this.completedTask;
    }

    public void setCompletedTask(long j) {
        this.completedTask = j;
        notifyChange();
    }

    @Bindable
    public long getTotalSubTask() {
        return this.totalSubTask;
    }

    public void setTotalSubTask(long j) {
        this.totalSubTask = j;
        notifyChange();
    }

    @Bindable
    public long getPendingSubTask() {
        return this.totalSubTask - this.completedSubTask;
    }

    public void setPendingSubTask(long j) {
        this.pendingSubTask = j;
        notifyChange();
    }

    @Bindable
    public long getCompletedSubTask() {
        return this.completedSubTask;
    }

    public void setCompletedSubTask(long j) {
        this.completedSubTask = j;
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

    public String getTasks() {
        return this.completedTask + InternalZipConstants.ZIP_FILE_SEPARATOR + this.totalTask;
    }

    public String getSubTasks() {
        return this.completedSubTask + InternalZipConstants.ZIP_FILE_SEPARATOR + this.totalSubTask;
    }
}
