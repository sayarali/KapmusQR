package com.alisayar.kapmusqr.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alisayar.kapmusqr.R
import com.alisayar.kapmusqr.databinding.ActivityLoginBinding
import com.alisayar.kapmusqr.student.StudentMainActivity
import com.alisayar.kapmusqr.teacher.TeacherMainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        if(auth.currentUser != null){
            val id = auth.currentUser!!.uid

            try {
                firestore.collection("Teachers").document(id).get().addOnSuccessListener {
                    if(it.exists()){
                        val intentTeacher = Intent(this, TeacherMainActivity::class.java)
                        startActivity(intentTeacher)
                        finish()
                    }
                }
                firestore.collection("Students").document(id).get().addOnSuccessListener {
                    if(it.exists()){
                        val intentStudent = Intent(this, StudentMainActivity::class.java)
                        startActivity(intentStudent)
                        finish()
                    }
                }
            } catch (e: Exception){
                println(e)
            }
        }





    }
}