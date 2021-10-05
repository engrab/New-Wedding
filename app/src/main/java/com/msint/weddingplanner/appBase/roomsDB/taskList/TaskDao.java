package com.msint.weddingplanner.appBase.roomsDB.taskList;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface TaskDao {
    @Delete
    int delete(TaskRowModel taskRowModel);

    @Query("DELETE FROM taskList where eventId=:currentId and id=:id")
    void delete(String currentId, String id);

    @Query("Select * FROM taskList where eventId=:currentId ")
    List<TaskRowModel> getAll(String currentId);

    @Query("Select id FROM taskList where eventId=:currentId and categoryId=:categoryId")
    List<String> getAll(String currentId, String categoryId);

    @Query("Select count(*) FROM taskList where eventId=:currentId ")
    long getAllCount(String currentId);

    @Query("Select count(*) FROM taskList where eventId=:currentId and isPending =:isPending ")
    long getAllCount(String currentId, int isPending);

    @Query("Select count(*) FROM taskList where eventId=:currentId and categoryId=:categoryId")
    long getAllCount(String currentId, String categoryId);

    @Query("Select count(*) FROM taskList where eventId=:currentId and isPending =:isPending and categoryId=:categoryId")
    long getAllCount(String currentId, String isPending, int categoryId);

    @Query("Select id FROM taskList where eventId=:currentId")
    List<String> getAllMarriage(String currentId);

    @Insert
    long insert(TaskRowModel taskRowModel);

    @Update
    int update(TaskRowModel taskRowModel);
}
