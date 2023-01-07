package com.alisayar.kapmusqr.teacher.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alisayar.kapmusqr.model.LessonModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TeacherHomeViewModel: ViewModel() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _lessonList = MutableLiveData<List<LessonModel>?>()
    val lessonList: LiveData<List<LessonModel>?> get() = _lessonList

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> get() = _isRefreshing

    init {
        getLessonsData()
    }


    fun getLessonsData(){
        _isRefreshing.value = true
        val lessonListTemp = arrayListOf<LessonModel>()
        firestore.collection("Lessons").whereEqualTo("ogretimGorevlisi", auth.currentUser!!.uid).addSnapshotListener { value, error ->

            if(error != null){
                _isRefreshing.value = false
            } else {
                if(value != null && !value.isEmpty){
                    val documents = value.documents
                    for (doc in documents){
                        val dersKodu = doc["dersKodu"].toString()
                        val dersAdi = doc["dersAdi"].toString()
                        val donem = doc["donem"].toString()
                        val sinif = doc["sinif"].toString()
                        val derslik = doc["derslik"].toString()
                        val gun = doc["gun"].toString()
                        val saat = doc["saat"].toString()
                        val ogretimGorevlisiId = doc["ogretimGorevlisi"].toString()
                        val lessonModel = LessonModel(dersKodu, dersAdi, donem, sinif, derslik, gun, saat, ogretimGorevlisiId)
                        lessonListTemp.add(lessonModel)
                        lessonListTemp.sortByDescending {
                            it.dersAdi
                        }
                        _lessonList.value = lessonListTemp
                        _isRefreshing.value = false
                    }
                }
                _isRefreshing.value = false
            }
        }


    }

}