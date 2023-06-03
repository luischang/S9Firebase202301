package com.example.s9firebase2023

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = FirebaseFirestore.getInstance()
        val tvCurso: TextView = findViewById(R.id.tvCurso)
        val tvNota: TextView = findViewById(R.id.tvNota)

        db.collection("courses")
            .addSnapshotListener { snapshots, e ->

                if (e != null) {
                    Log.w("Firebase", "Error al consultar la colección de cursos")
//                    Snackbar
//                        .make(
//                            findViewById(android.R.id.content),
//                            "Error al consultar",
//                            Snackbar.LENGTH_LONG
//                        ).show()
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
//                            Snackbar
//                                .make(
//                                    findViewById(android.R.id.content),
//                                    "Consultando la colección",
//                                    Snackbar.LENGTH_LONG
//                                ).show()
                            tvCurso.text = dc.document.data["description"].toString()
                            tvNota.text = dc.document.data["score"].toString()
                        }

                        DocumentChange.Type.REMOVED -> {
                            Snackbar
                                .make(
                                    findViewById(android.R.id.content),
                                    "Eliminando documentos de la colección",
                                    Snackbar.LENGTH_LONG
                                ).show()
                        }

                        else -> {
                            Log.w("Firebase", "Error al consultar la colección de cursos")
                        }
                    }
                }
            }
    }
}