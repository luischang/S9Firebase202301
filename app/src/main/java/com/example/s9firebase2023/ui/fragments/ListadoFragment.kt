package com.example.s9firebase2023.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.s9firebase2023.R
import com.example.s9firebase2023.adapter.CourseAdapter
import com.example.s9firebase2023.model.CourseModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class ListadoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_listado, container, false)
        val db = FirebaseFirestore.getInstance()
        var lstCourses: List<CourseModel>
        val rvCourse: RecyclerView = view.findViewById(R.id.rvCourse)

        db.collection("courses")
            .addSnapshotListener{snap, e->
                if(e!=null){
                    Log.w("Firebase", "Error al consultar la colección de cursos")
//                    Snackbar
//                        .make(
//                            findViewById(android.R.id.content),
//                            "Error al consultar",
//                           Snackbar.LENGTH_LONG
//                        ).show()
                return@addSnapshotListener
                }
                lstCourses = snap!!.documents.map { document ->
                    CourseModel(document["description"].toString(),
                                document["score"].toString())
                }

                rvCourse.adapter = CourseAdapter(lstCourses)
                rvCourse.layoutManager = LinearLayoutManager(requireContext())

            }
        return view
    }
}