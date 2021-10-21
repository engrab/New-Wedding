package com.example.weddingplanner.backup;

public class RestoreRowModel {
    String DateModified;
    long lastModifiedDate;
    String path;
    String size;
    long timestamp;
    String title;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String str) {
        this.size = str;
    }

    public String getDateModified() {
        return this.DateModified;
    }

    public void setDateModified(String str) {
        this.DateModified = str;
    }

    public Long getTimestamp() {
        return Long.valueOf(this.timestamp);
    }

    public void setTimestamp(Long l) {
        this.timestamp = l.longValue();
    }

    public long getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public void setLastModifiedDate(long j) {
        this.lastModifiedDate = j;
    }
}
