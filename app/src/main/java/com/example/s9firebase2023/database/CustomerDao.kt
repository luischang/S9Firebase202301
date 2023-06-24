package com.example.s9firebase2023.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CustomerDao {
    @Insert
    fun insert(customerEntity: CustomerEntity)
    @Update
    fun update(customerEntity: CustomerEntity)
    @Delete
    fun delete(customerEntity: CustomerEntity)
    @Query("SELECT * FROM customer ORDER BY last_name")
    fun getCustomerOrderByLastName(): LiveData<List<CustomerEntity>>
}