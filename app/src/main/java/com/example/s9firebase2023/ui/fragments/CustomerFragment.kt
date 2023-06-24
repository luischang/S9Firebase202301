package com.example.s9firebase2023.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import com.example.s9firebase2023.R
import com.example.s9firebase2023.database.CustomerEntity

class CustomerFragment : Fragment() {

    companion object {
        fun newInstance() = CustomerFragment()
    }

    private lateinit var viewModel: CustomerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_customer, container, false)

        val txtFirstname: EditText = view.findViewById(R.id.txtFirstName)
        val txtLastName: EditText = view.findViewById(R.id.txtLastName)
        val txtPhoneNumber: EditText = view.findViewById(R.id.txtPhoneNumber)
        val btnSaveCustomer: Button = view.findViewById(R.id.btnSaveCustomer)

        //viewModel = ViewModelProvider(this)[CustomerViewModel::class.java]
        viewModel = ViewModelProvider(this)
            .get(CustomerViewModel::class.java)

        btnSaveCustomer.setOnClickListener {
            val customerEntity =
                CustomerEntity(txtFirstname.text.toString()
                            ,txtLastName.text.toString()
                            , txtPhoneNumber.text.toString())
            viewModel.saveCustomer(customerEntity)
            addObserver()
        }
        return view
    }

    private fun addObserver(){
       val observer = Observer<List<CustomerEntity>>{customers->
           if(customers!=null){
               var text= ""
               for(customer in customers){
                   text += "${customer.firstName} ${customer.lastName} ${customer.phoneNumber}"
                   Log.d("List customer is " , "Customer--> $text")
               }
           }
       }
        viewModel.customers.observe(viewLifecycleOwner, observer)
    }

}