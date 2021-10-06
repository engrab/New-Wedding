package com.example.weddingplanner.appBase.roomsDB.vendor;

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
    void delete(String currentId, String id);

    @Query("Select * FROM vendorList where eventId=:currentId")
    List<VendorRowModel> getAll(String currentId);

    @Query("Select id FROM vendorList where eventId=:currentId and categoryId=:categoryId")
    List<String> getAll(String currentId, String categoryId);

    @Query("Select count(*) FROM vendorList where eventId=:currentId")
    long getAllCount(String currentId);

    @Query("Select id FROM vendorList where eventId=:currentId")
    List<String> getAllMarriage(String currentId);

    @Query("Select sum(expectedAmount) FROM vendorList where eventId=:currentId")
    double getAllTotal(String currentId);

    @Query("Select sum(expectedAmount) FROM vendorList where eventId=:currentId and categoryId=:categoryId")
    double getAllTotal(String currentId, String categoryId);

    @Query("Select sum(paymentList.amount) FROM vendorList left join paymentList on paymentList.parentId = vendorList.id where eventId=:currentId and isPending=:isPending")
    double getTotal(String currentId, int isPending);

    @Query("Select sum(paymentList.amount) FROM vendorList left join paymentList on paymentList.parentId = vendorList.id where eventId=:currentId and isPending=:isPending and categoryId=:categoryId")
    double getTotal(String currentId, int isPending, String categoryId);

    @Insert
    long insert(VendorRowModel vendorRowModel);

    @Update
    int update(VendorRowModel vendorRowModel);
}
