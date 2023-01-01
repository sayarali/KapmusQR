package com.alisayar.kapmusqr.teacher.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alisayar.kapmusqr.model.TeacherModel
import com.google.firebase.firestore.FirebaseFirestore

class TeacherProfileViewModel(private val teacherId: String): ViewModel() {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _ogretimGorevlisi = MutableLiveData<TeacherModel>()
    val ogretimGorevlisi: LiveData<TeacherModel> get() = _ogretimGorevlisi
    private val _ogretimGorevlisiAd = MutableLiveData<String>()
    val ogretimGorevlisiAd: LiveData<String> get() = _ogretimGorevlisiAd

    private val _toastError = MutableLiveData<String>()
    val toastError: LiveData<String> get() = _toastError

    init {
        println(teacherId)
        getTeacherData(teacherId)
    }


    private fun getTeacherData(teacherId: String){
        try {
            firestore.collection("Teachers").document(teacherId).get().addOnCompleteListener {
                if(it.isSuccessful){
                    val teacherModel = TeacherModel(
                        it.result["id"].toString(),
                        it.result["personalNo"].toString(),
                        it.result["email"].toString(),
                        it.result["name"].toString(),
                        it.result["surname"].toString(),
                        it.result["roomNumber"].toString(),
                        it.result["phoneNumber"].toString(),
                        it.result["ppUrl"].toString(),
                    )
                    _ogretimGorevlisi.value = teacherModel
                    _ogretimGorevlisiAd.value = teacherModel.name + " " + teacherModel.surname

                }
            }.addOnFailureListener {
                _toastError.value = it.localizedMessage
            }
        } catch (e: Exception){
            println(e.localizedMessage)
        }
    }
}