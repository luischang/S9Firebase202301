package com.example.s9firebase2023.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.s9firebase2023.R
import com.example.s9firebase2023.model.CourseModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class RegistroFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_registro, container, false)

        val txtCourse: EditText = view.findViewById(R.id.txtCourse)
        val txtScore: EditText = view.findViewById(R.id.txtScore)
        val btnSaveCourse: Button = view.findViewById(R.id.btnSaveCourse)
        val db = FirebaseFirestore.getInstance()
        val collectionRef= db.collection("courses")

        btnSaveCourse.setOnClickListener {
            val curso = txtCourse.text.toString()
            val nota = txtScore.text.toString()
            val nuevoCurso = CourseModel(curso,nota)

            collectionRef.add(nuevoCurso)
                .addOnSuccessListener { documentReference ->
                    Snackbar.make(view,
                            "Registro de curso exitoso: ID-> ${documentReference.id}",
                            Snackbar.LENGTH_LONG
                        ).show()
                }
        }
        return view
    }
}