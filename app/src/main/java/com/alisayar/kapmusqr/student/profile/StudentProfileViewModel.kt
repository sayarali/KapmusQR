package com.alisayar.kapmusqr.student.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alisayar.kapmusqr.model.StudentModel
import com.alisayar.kapmusqr.model.TeacherModel
import com.google.firebase.firestore.FirebaseFirestore

class StudentProfileViewModel(private val studentId: String): ViewModel() {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _ogrenci = MutableLiveData<StudentModel>()
    val ogrenci: LiveData<StudentModel> get() = _ogrenci
    private val _ogrenciAd = MutableLiveData<String>()
    val ogrenciAd: LiveData<String> get() = _ogrenciAd

    private val _toastError = MutableLiveData<String>()
    val toastError: LiveData<String> get() = _toastError

    init {
        getStudentData(studentId)
    }

    private fun getStudentData(studentId: String){
        try {
            firestore.collection("Students").document(studentId).get().addOnCompleteListener {
                if(it.isSuccessful){
                    val studentModel = StudentModel(
                        it.result["id"].toString(),
                        it.result["studentNo"].toString(),
                        it.result["email"].toString(),
                        it.result["name"].toString(),
                        it.result["surname"].toString(),
                        it.result["phoneNumber"].toString(),
                        it.result["ppUrl"].toString(),
                    )
                    _ogrenci.value = studentModel
                    _ogrenciAd.value = studentModel.name + " " + studentModel.surname

                }
            }.addOnFailureListener {
                _toastError.value = it.localizedMessage
            }
        } catch (e: Exception){
            println(e.localizedMessage)
        }
    }
}