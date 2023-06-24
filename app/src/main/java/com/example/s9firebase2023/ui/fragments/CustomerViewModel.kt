package com.example.s9firebase2023.ui.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.s9firebase2023.database.CustomerEntity
import com.example.s9firebase2023.database.CustomerRepository

class CustomerViewModel (application: Application): AndroidViewModel(application) {
    private var repository =  CustomerRepository(application)
    val customers = repository.getCustomers()

    fun saveCustomer(customerEntity: CustomerEntity){
        repository.insert(customerEntity)
    }

}