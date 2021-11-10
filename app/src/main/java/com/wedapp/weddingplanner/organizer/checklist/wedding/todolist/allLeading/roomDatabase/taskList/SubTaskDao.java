package com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.roomDatabase.taskList;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface SubTaskDao {
    @Delete
    int delete(SubTaskRowModel subTaskRowModel);

    @Query("DELETE FROM subTaskList")
    void deleteAll();

    @Query("DELETE FROM subTaskList where taskId=:taskId")
    void deleteAll(String taskId);

    @Query("Select * FROM subTaskList")
    List<SubTaskRowModel> getAll();

    @Query("Select * FROM subTaskList where taskId=:parentId")
    List<SubTaskRowModel> getAll(String parentId);

    @Query("Select count(*) FROM subTaskList left join taskList on subTaskList.taskId = taskList.id where taskList.eventId =:currentId ")
    long getAllCount(String currentId);

    @Query("Select count(*) FROM subTaskList left join taskList on subTaskList.taskId = taskList.id where taskList.eventId =:currentId and taskList.categoryId =:categoryId")
    long getAllCount(String currentId, String categoryId);

    @Query("Select count(*) FROM subTaskList left join taskList on subTaskList.taskId = taskList.id where taskList.eventId =:currentId and subTaskList.isPending=0 ")
    long getCompletedCount(String currentId);

    @Query("Select count(*) FROM subTaskList left join taskList on subTaskList.taskId = taskList.id where taskList.eventId =:currentId and taskList.categoryId =:categoryId and subTaskList.isPending=0 ")
    long getCompletedCount(String currentId, String categoryId);

    @Insert
    long insert(SubTaskRowModel subTaskRowModel);

    @Update
    int update(SubTaskRowModel subTaskRowModel);
}
