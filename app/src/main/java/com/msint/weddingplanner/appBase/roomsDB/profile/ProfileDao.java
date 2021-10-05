package com.msint.weddingplanner.appBase.roomsDB.profile;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.msint.weddingplanner.appBase.models.profile.ProfileRowModel;
import java.util.List;

@Dao
public interface ProfileDao {
    @Delete
    int delete(ProfileRowModel profileRowModel);

    @Query("DELETE FROM profileList")
    void deleteAll();

    @Query("Select * FROM profileList")
    List<ProfileRowModel> getAll();

    @Query("Select * FROM profileList where id=:id")
    ProfileRowModel getDetail(String id);

    @Query("SELECT COUNT(name) FROM profileList WHERE name =:name COLLATE NOCASE")
    int getNameExistCount(String name);

    @Query("Select id FROM profileList where isSelected = 1")
    String getSelectedId();

    @Insert
    long insert(ProfileRowModel profileRowModel);

    @Query("update profileList set isSelected =0")
    int setDeselectAll();

    @Query("update profileList set isSelected =1 where id =:id")
    int setSelection(String id);

    @Update
    int update(ProfileRowModel profileRowModel);
}
