package com.alisayar.kapmusqr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alisayar.kapmusqr.databinding.ActivityMainBinding
import com.alisayar.kapmusqr.login.LoginActivity
import com.alisayar.kapmusqr.student.StudentMainActivity
import com.alisayar.kapmusqr.teacher.TeacherMainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        if(auth.currentUser == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val id = auth.currentUser!!.uid
            try {
                firestore.collection("Teachers").document(id).get().addOnSuccessListener {
                    if(it.exists()){
                        println("Öğretmen")
                        val intentTeacher = Intent(this, TeacherMainActivity::class.java)
                        startActivity(intentTeacher)
                        finish()
                    }
                }
                firestore.collection("Students").document(id).get().addOnSuccessListener {
                    if(it.exists()){
                        println("Öğrenci")
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