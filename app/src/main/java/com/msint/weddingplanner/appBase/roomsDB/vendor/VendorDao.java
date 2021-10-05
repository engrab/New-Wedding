package com.msint.weddingplanner.appBase.roomsDB.vendor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface VendorDao {
    @Delete
    int delete(VendorRowModel vendorRowModel);

    @Query("DELETE FROM vendorList where eventId=:currentId and id=:id")
    void delete(String str, String str2);

    @Query("Select * FROM vendorList where eventId=:currentId")
    List<VendorRowModel> getAll(String str);

    @Query("Select id FROM vendorList where eventId=:currentId and categoryId=:categoryId")
    List<String> getAll(String str, String str2);

    @Query("Select count(*) FROM vendorList where eventId=:currentId")
    long getAllCount(String str);

    @Query("Select id FROM vendorList where eventId=:currentId")
    List<String> getAllMarriage(String str);

    @Query("Select sum(expectedAmount) FROM vendorList where eventId=:currentId")
    double getAllTotal(String str);

    @Query("Select sum(expectedAmount) FROM vendorList where eventId=:currentId and categoryId=:categoryId")
    double getAllTotal(String str, String str2);

    @Query("Select sum(paymentList.amount) FROM vendorList left join paymentList on paymentList.parentId = vendorList.id where eventId=:currentId and isPending=:isPending")
    double getTotal(String str, int i);

    @Query("Select sum(paymentList.amount) FROM vendorList left join paymentList on paymentList.parentId = vendorList.id where eventId=:currentId and isPending=:isPending and categoryId=:categoryId")
    double getTotal(String str, int i, String str2);

    @Insert
    long insert(VendorRowModel vendorRowModel);

    @Update
    int update(VendorRowModel vendorRowModel);
}
