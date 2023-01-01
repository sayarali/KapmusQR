package com.alisayar.kapmusqr.student.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alisayar.kapmusqr.model.LessonModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StudentHomeViewModel: ViewModel() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _lessonList = MutableLiveData<List<LessonModel>?>()
    val lessonList: LiveData<List<LessonModel>?> get() = _lessonList

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> get() = _isRefreshing

    private var studentNo = ""
    init {
        getLessonsData()
    }



    fun getLessonsData(){
        _isRefreshing.value = true
        val lessonListTemp = arrayListOf<LessonModel>()
        firestore.collection("Students").document(auth.currentUser!!.uid).get().addOnCompleteListener {
            if(it.isSuccessful){
                if(it.result["derslerim"] != null){
                    val dersKoduList = it.result["derslerim"] as List<String>
                    for (i in dersKoduList){
                        firestore.collection("Lessons").document(i).get().addOnCompleteListener {
                            val model = LessonModel(
                                it.result["dersKodu"].toString(),
                                it.result["dersAdi"].toString(),
                                it.result["donem"].toString(),
                                it.result["sinif"].toString(),
                                it.result["derslik"].toString(),
                                it.result["gun"].toString(),
                                it.result["saat"].toString(),
                                it.result["ogretimGorevlisi"].toString()
                            )
                            lessonListTemp.add(model)
                            lessonListTemp.sortedBy {
                                it.dersAdi
                            }
                            _lessonList.value = lessonListTemp
                            _isRefreshing.value = false
                        }
                    }
                }
            }
        }



    }
}