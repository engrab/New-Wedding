package com.msint.weddingplanner.appBase.roomsDB.cost;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface CostDao {
    @Delete
    int delete(CostRowModel costRowModel);

    @Query("DELETE FROM costList where eventId=:currentId and id=:id")
    void delete(String str, String str2);

    @Query("Select * FROM costList where eventId=:currentId")
    List<CostRowModel> getAll(String str);

    @Query("Select id FROM costList where eventId=:currentId and categoryId=:categoryId")
    List<String> getAll(String str, String str2);

    @Query("Select id FROM costList where eventId=:currentId")
    List<String> getAllMarriage(String str);

    @Query("Select sum(expectedAmount) FROM costList where eventId=:currentId")
    double getAllTotal(String str);

    @Query("Select sum(expectedAmount) FROM costList where eventId=:currentId and categoryId=:categoryId")
    double getAllTotal(String str, String str2);

    @Query("Select sum(paymentList.amount) FROM costList left join paymentList on paymentList.parentId = costList.id where eventId=:currentId and isPending=:isPending")
    double getTotal(String str, int i);

    @Query("Select sum(paymentList.amount) FROM costList left join paymentList on paymentList.parentId = costList.id where eventId=:currentId and isPending=:isPending and categoryId=:categoryId")
    double getTotal(String str, int i, String str2);

    @Insert
    long insert(CostRowModel costRowModel);

    @Update
    int update(CostRowModel costRowModel);
}
