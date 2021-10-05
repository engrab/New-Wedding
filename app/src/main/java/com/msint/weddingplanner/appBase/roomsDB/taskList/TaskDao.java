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
    void delete(String str, String str2);

    @Query("Select * FROM taskList where eventId=:currentId ")
    List<TaskRowModel> getAll(String str);

    @Query("Select id FROM taskList where eventId=:currentId and categoryId=:categoryId")
    List<String> getAll(String str, String str2);

    @Query("Select count(*) FROM taskList where eventId=:currentId ")
    long getAllCount(String str);

    @Query("Select count(*) FROM taskList where eventId=:currentId and isPending =:isPending ")
    long getAllCount(String str, int i);

    @Query("Select count(*) FROM taskList where eventId=:currentId and categoryId=:categoryId")
    long getAllCount(String str, String str2);

    @Query("Select count(*) FROM taskList where eventId=:currentId and isPending =:isPending and categoryId=:categoryId")
    long getAllCount(String str, String str2, int i);

    @Query("Select id FROM taskList where eventId=:currentId")
    List<String> getAllMarriage(String str);

    @Insert
    long insert(TaskRowModel taskRowModel);

    @Update
    int update(TaskRowModel taskRowModel);
}
