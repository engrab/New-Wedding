package com.msint.weddingplanner.appBase.roomsDB.guest;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface GuestDao {
    @Delete
    int delete(GuestRowModel guestRowModel);

    @Query("DELETE FROM guestList where eventId=:currentId and id=:id")
    void delete(String str, String str2);

    @Query("DELETE FROM guestList where isCompanion == 1 and eventId=:currentId and guestId=:guestId")
    void deleteAllComp(String str, String str2);

    @Query("Select * FROM guestList where eventId=:currentId and guestId=:id")
    List<GuestRowModel> getAllComp(String str, String str2);

    @Query("Select count(*) FROM guestList where eventId=:currentId and  isCompanion == 1")
    long getAllCompanionCount(String str);

    @Query("Select count(*) FROM guestList where eventId=:currentId and isCompanion == 1 and stageType =:ageStageType")
    long getAllCompanionTypeCount(String str, int i);

    @Query("Select count(*) FROM guestList where eventId=:currentId ")
    long getAllCount(String str);

    @Query("Select * FROM guestList where eventId=:currentId and isCompanion == 0")
    List<GuestRowModel> getAllGuest(String str);

    @Query("Select count(*) FROM guestList where eventId=:currentId and isCompanion == 0")
    long getAllGuestCount(String str);

    @Query("Select id FROM guestList where eventId=:currentId")
    List<String> getAllMarriage(String str);

    @Query("Select count(*) FROM guestList where eventId=:currentId and isCompanion == 0 and isInvitationSent ==:isInvitationSent")
    long getInvitationSentCount(String str, int i);

    @Insert
    long insert(GuestRowModel guestRowModel);

    @Update
    int update(GuestRowModel guestRowModel);
}
