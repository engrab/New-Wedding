package com.example.weddingplanner.all.roomsDB.dbVerson;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dbVersionList")
public class DbVersionRowModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String versionDesc = "";
    private int versionNumber;

    public DbVersionRowModel() {
    }

    public DbVersionRowModel(int i) {
        this.versionNumber = i;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public int getVersionNumber() {
        return this.versionNumber;
    }

    public void setVersionNumber(int i) {
        this.versionNumber = i;
    }

    public String getVersionDesc() {
        return this.versionDesc;
    }

    public void setVersionDesc(String str) {
        this.versionDesc = str;
    }
}
