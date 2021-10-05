package com.msint.weddingplanner.appBase.models.guest;


import androidx.databinding.BaseObservable;

import net.lingala.zip4j.util.InternalZipConstants;

public class GuestSummaryModel extends BaseObservable {
    private long adult;
    private long baby;
    private long child;
    private long pendingInvitation;
    private long sendInvitation;
    private long totalCompanion;
    private long totalGuest;

    public long getTotalGuest() {
        return this.totalGuest;
    }

    public void setTotalGuest(long j) {
        this.totalGuest = j;
    }

    public long getPendingInvitation() {
        return this.totalGuest - this.sendInvitation;
    }

    public void setPendingInvitation(long j) {
        this.pendingInvitation = j;
    }

    public long getSendInvitation() {
        return this.sendInvitation;
    }

    public void setSendInvitation(long j) {
        this.sendInvitation = j;
    }

    public long getTotalCompanion() {
        return this.totalCompanion;
    }

    public void setTotalCompanion(long j) {
        this.totalCompanion = j;
    }

    public long getAdult() {
        return this.adult;
    }

    public void setAdult(long j) {
        this.adult = j;
    }

    public long getChild() {
        return this.child;
    }

    public void setChild(long j) {
        this.child = j;
    }

    public long getBaby() {
        return this.baby;
    }

    public void setBaby(long j) {
        this.baby = j;
    }

    public String getInvitations() {
        return this.sendInvitation + InternalZipConstants.ZIP_FILE_SEPARATOR + this.totalGuest;
    }

    public long getTotalGuests() {
        return this.totalGuest + this.totalCompanion;
    }
}
