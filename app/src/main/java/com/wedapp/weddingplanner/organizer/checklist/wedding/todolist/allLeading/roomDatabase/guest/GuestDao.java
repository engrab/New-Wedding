package com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.roomDatabase.guest;

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
    void delete(String currentId, String id);

    @Query("DELETE FROM guestList where isCompanion == 1 and eventId=:currentId and guestId=:guestId")
    void deleteAllComp(String currentId, String guestId);

    @Query("Select * FROM guestList where eventId=:currentId and guestId=:id")
    List<GuestRowModel> getAllComp(String currentId, String id);

    @Query("Select count(*) FROM guestList where eventId=:currentId and  isCompanion == 1")
    long getAllCompanionCount(String currentId);

    @Query("Select count(*) FROM guestList where eventId=:currentId and isCompanion == 1 and stageType =:ageStageType")
    long getAllCompanionTypeCount(String currentId, int ageStageType);

    @Query("Select count(*) FROM guestList where eventId=:currentId ")
    long getAllCount(String currentId);

    @Query("Select * FROM guestList where eventId=:currentId and isCompanion == 0")
    List<GuestRowModel> getAllGuest(String currentId);

    @Query("Select count(*) FROM guestList where eventId=:currentId and isCompanion == 0")
    long getAllGuestCount(String currentId);

    @Query("Select id FROM guestList where eventId=:currentId")
    List<String> getAllMarriage(String currentId);

    @Query("Select count(*) FROM guestList where eventId=:currentId and isCompanion == 0 and isInvitationSent ==:isInvitationSent")
    long getInvitationSentCount(String currentId, int isInvitationSent);

    @Insert
    long insert(GuestRowModel guestRowModel);

    @Update
    int update(GuestRowModel guestRowModel);
}
