package com.msint.weddingplanner.appBase.roomsDB.payment;

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
    void deleteAll(String str);

    @Query("Select * FROM paymentList where parentId=:parentId")
    List<PaymentRowModel> getAll(String str);

    @Query("Select count(*) FROM paymentList inner join costList on costList.id = paymentList.parentId where costList.eventId =:currentId and categoryId =:categoryId and type =:costType")
    long getAllCountCost(String str, String str2, int i);

    @Query("Select count(*) FROM paymentList left join costList on paymentList.parentId = costList.id where costList.eventId =:currentId and paymentList.type =:costType")
    long getAllCountCostType(String str, int i);

    @Query("select count(distinct parentid) cnt from paymentList inner join costList on costList.id = paymentList.parentId where costList.eventId =:currentId and type =:costType and parentid not in( select distinct parentid from paymentList inner join costList on costList.id = paymentList.parentId where costList.eventId =:currentId and type =:costType and isPending = 1 and type =:costType)")
    long getAllCountPaidByTypeCost(String str, int i);

    @Query("select count(distinct parentid) cnt from paymentList inner join vendorList on vendorList.id = paymentList.parentId where vendorList.eventId =:currentId and type =:costType and parentid not in( select distinct parentid from paymentList inner join vendorList on vendorList.id = paymentList.parentId where vendorList.eventId =:currentId and type =:costType and isPending = 1 and type =:costType)")
    long getAllCountPaidByTypeVendor(String str, int i);

    @Query("Select count(*) FROM paymentList left join costList on paymentList.parentId = costList.id where costList.eventId =:currentId and paymentList.type =:costType and paymentList.isPending =0")
    long getAllCountPaidCostType(String str, int i);

    @Query("Select count(*) FROM paymentList left join vendorList on paymentList.parentId = vendorList.id where vendorList.eventId =:currentId and paymentList.type =:costType and paymentList.isPending =0")
    long getAllCountPaidVendorType(String str, int i);

    @Query("select (select count(distinct parentid) cnt from paymentList inner join costList on costList.id = paymentList.parentId where costList.eventId =:currentId and isPending = 1 and type =:costType ) + (select count(id) cnt2 from costList where eventId =:currentId and id not in (select distinct parentid from paymentList where type =:costType )) cnt;")
    long getAllCountPendingCost(String str, int i);

    @Query("select (select count(distinct parentid) cnt from paymentList inner join vendorList on vendorList.id = paymentList.parentId where vendorList.eventId =:currentId and isPending = 1 and type =:costType ) + (select count(id) cnt2 from vendorList where eventId =:currentId and id not in (select distinct parentid from paymentList where type =:costType )) cnt;")
    long getAllCountPendingVendor(String str, int i);

    @Query("Select count(*) FROM paymentList inner join vendorList on vendorList.id = paymentList.parentId where vendorList.eventId =:currentId and categoryId =:categoryId and type =:costType")
    long getAllCountVendor(String str, String str2, int i);

    @Query("Select count(*) FROM paymentList left join vendorList on paymentList.parentId = vendorList.id where vendorList.eventId =:currentId and paymentList.type =:costType")
    long getAllCountVendorType(String str, int i);

    @Query("Select count(*) FROM paymentList inner join costList on costList.id = paymentList.parentId where costList.eventId =:currentId and categoryId =:categoryId and isPending = 0 and type =:costType")
    long getCompletedCountCost(String str, String str2, int i);

    @Query("Select count(*) FROM paymentList inner join vendorList on vendorList.id = paymentList.parentId where vendorList.eventId =:currentId and categoryId =:categoryId and isPending = 0 and type =:costType")
    long getCompletedCountVendor(String str, String str2, int i);

    @Query("select sum(amount) from paymentList where parentId=:parentId and isPending =:isPending")
    double getTotal(String str, int i);

    @Insert
    long insert(PaymentRowModel paymentRowModel);

    @Update
    int update(PaymentRowModel paymentRowModel);
}
