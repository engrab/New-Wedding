package com.example.weddingplanner.allLeading.roomDatabase.cost;

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
    void delete(String currentId, String id);

    @Query("Select * FROM costList where eventId=:currentId")
    List<CostRowModel> getAll(String currentId);

    @Query("Select id FROM costList where eventId=:currentId and categoryId=:categoryId")
    List<String> getAll(String currentId, String categoryId);

    @Query("Select id FROM costList where eventId=:currentId")
    List<String> getAllMarriage(String currentId);

    @Query("Select sum(expectedAmount) FROM costList where eventId=:currentId")
    double getAllTotal(String currentId);

    @Query("Select sum(expectedAmount) FROM costList where eventId=:currentId and categoryId=:categoryId")
    double getAllTotal(String currentId, String categoryId);

    @Query("Select sum(paymentList.amount) FROM costList left join paymentList on paymentList.parentId = costList.id where eventId=:currentId and isPending=:isPending")
    double getTotal(String currentId, int isPending);

    @Query("Select sum(paymentList.amount) FROM costList left join paymentList on paymentList.parentId = costList.id where eventId=:currentId and isPending=:isPending and categoryId=:categoryId")
    double getTotal(String currentId, int isPending, String categoryId);

    @Insert
    long insert(CostRowModel costRowModel);

    @Update
    int update(CostRowModel costRowModel);
}
