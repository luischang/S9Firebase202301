package com.example.s9firebase2023

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.s9firebase2023.model.UserModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val txtFullName: EditText = findViewById(R.id.txtFullName)
        val txtCountry: EditText = findViewById(R.id.txtCountry)
        val txtEmail: EditText = findViewById(R.id.txtEmailRegister)
        val txtPassword: EditText = findViewById(R.id.txtPasswordRegister)
        val btnSaveRegister: Button = findViewById(R.id.btnSaveRegister)
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("users")

        btnSaveRegister.setOnClickListener{
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()
            val fullName = txtFullName.text.toString()
            val country = txtCountry.text.toString()

            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){ task ->
                    if(task.isSuccessful){

                        val user: FirebaseUser? = auth.currentUser
                        val uid = user?.uid

                        val userModel = UserModel(email,password,fullName,country,uid.toString())
                        collectionRef.add(userModel)
                            .addOnSuccessListener { documentReference ->

                            }.addOnFailureListener{error ->
                                Snackbar
                                    .make(
                                        findViewById(android.R.id.content),
                                        "Error al registrar: $error",
                                        Snackbar.LENGTH_LONG
                                    ).show()
                            }
                        Snackbar
                            .make(
                                findViewById(android.R.id.content),
                                "Registro exitoso: $uid",
                                Snackbar.LENGTH_LONG
                            ).show()
                        startActivity(Intent(this,LoginActivity::class.java))
                    }
                }


        }

    }
}