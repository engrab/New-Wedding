package com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.roomDatabase.payment;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface PaymentDao {
    @Delete
    int delete(PaymentRowModel paymentRowModel);

    @Query("DELETE FROM paymentList where parentId=:parentId")
    void deleteAll(String parentId);

    @Query("Select * FROM paymentList where parentId=:parentId")
    List<PaymentRowModel> getAll(String parentId);

    @Query("Select count(*) FROM paymentList inner join costList on costList.id = paymentList.parentId where costList.eventId =:currentId and categoryId =:categoryId and type =:costType")
    long getAllCountCost(String currentId, String categoryId, int costType);

    @Query("Select count(*) FROM paymentList left join costList on paymentList.parentId = costList.id where costList.eventId =:currentId and paymentList.type =:costType")
    long getAllCountCostType(String currentId, int costType);

    @Query("select count(distinct parentid) cnt from paymentList inner join costList on costList.id = paymentList.parentId where costList.eventId =:currentId and type =:costType and parentid not in( select distinct parentid from paymentList inner join costList on costList.id = paymentList.parentId where costList.eventId =:currentId and type =:costType and isPending = 1 and type =:costType)")
    long getAllCountPaidByTypeCost(String currentId, int costType);

    @Query("select count(distinct parentid) cnt from paymentList inner join vendorList on vendorList.id = paymentList.parentId where vendorList.eventId =:currentId and type =:costType and parentid not in( select distinct parentid from paymentList inner join vendorList on vendorList.id = paymentList.parentId where vendorList.eventId =:currentId and type =:costType and isPending = 1 and type =:costType)")
    long getAllCountPaidByTypeVendor(String currentId, int costType);

    @Query("Select count(*) FROM paymentList left join costList on paymentList.parentId = costList.id where costList.eventId =:currentId and paymentList.type =:costType and paymentList.isPending =0")
    long getAllCountPaidCostType(String currentId, int costType);

    @Query("Select count(*) FROM paymentList left join vendorList on paymentList.parentId = vendorList.id where vendorList.eventId =:currentId and paymentList.type =:costType and paymentList.isPending =0")
    long getAllCountPaidVendorType(String currentId, int costType);

    @Query("select (select count(distinct parentid) cnt from paymentList inner join costList on costList.id = paymentList.parentId where costList.eventId =:currentId and isPending = 1 and type =:costType ) + (select count(id) cnt2 from costList where eventId =:currentId and id not in (select distinct parentid from paymentList where type =:costType )) cnt;")
    long getAllCountPendingCost(String currentId, int costType);

    @Query("select (select count(distinct parentid) cnt from paymentList inner join vendorList on vendorList.id = paymentList.parentId where vendorList.eventId =:currentId and isPending = 1 and type =:costType ) + (select count(id) cnt2 from vendorList where eventId =:currentId and id not in (select distinct parentid from paymentList where type =:costType )) cnt;")
    long getAllCountPendingVendor(String currentId, int costType);

    @Query("Select count(*) FROM paymentList inner join vendorList on vendorList.id = paymentList.parentId where vendorList.eventId =:currentId and categoryId =:categoryId and type =:costType")
    long getAllCountVendor(String currentId, String categoryId, int costType);

    @Query("Select count(*) FROM paymentList left join vendorList on paymentList.parentId = vendorList.id where vendorList.eventId =:currentId and paymentList.type =:costType")
    long getAllCountVendorType(String currentId, int costType);

    @Query("Select count(*) FROM paymentList inner join costList on costList.id = paymentList.parentId where costList.eventId =:currentId and categoryId =:categoryId and isPending = 0 and type =:costType")
    long getCompletedCountCost(String currentId, String categoryId, int costType);

    @Query("Select count(*) FROM paymentList inner join vendorList on vendorList.id = paymentList.parentId where vendorList.eventId =:currentId and categoryId =:categoryId and isPending = 0 and type =:costType")
    long getCompletedCountVendor(String currentId, String categoryId, int costType);

    @Query("select sum(amount) from paymentList where parentId=:parentId and isPending =:isPending")
    double getTotal(String parentId, int isPending);

    @Insert
    long insert(PaymentRowModel paymentRowModel);

    @Update
    int update(PaymentRowModel paymentRowModel);
}
