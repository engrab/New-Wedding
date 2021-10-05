package com.msint.weddingplanner.appBase.roomsDB.dbVerson;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface DbVersionDao {
    @Delete
    int delete(DbVersionRowModel dbVersionRowModel);

    @Query("DELETE FROM categoryList")
    void deleteAllCategory();

    @Query("DELETE FROM costList")
    void deleteAllCost();

    @Query("DELETE FROM guestList")
    void deleteAllGuest();

    @Query("DELETE FROM paymentList")
    void deleteAllPayment();

    @Query("DELETE FROM profileList")
    void deleteAllProfile();

    @Query("DELETE FROM subTaskList")
    void deleteAllSubTask();

    @Query("DELETE FROM taskList")
    void deleteAllTask();

    @Query("DELETE FROM vendorList")
    void deleteAllVendor();

    @Query("DELETE FROM dbVersionList")
    void deleteAlldbVersion();

    @Query("Select * FROM dbVersionList ")
    List<DbVersionRowModel> getAll();

    @Query("Select * FROM dbVersionList where versionNumber=:versionNumber")
    DbVersionRowModel getDetail(int versionNumber);

    @Insert
    long insert(DbVersionRowModel dbVersionRowModel);

    @Update
    int update(DbVersionRowModel dbVersionRowModel);
}
