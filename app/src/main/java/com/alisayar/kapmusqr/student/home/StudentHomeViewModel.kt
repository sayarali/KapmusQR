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
        firestore.collection("Students").document(auth.currentUser!!.uid).addSnapshotListener { value1, error1 ->
            if(error1 == null){
                if(value1 != null && value1.exists()){
                    if(value1["derslerim"] != null){
                        val dersKoduList = value1["derslerim"] as List<*>
                        for (i in dersKoduList){
                            firestore.collection("Lessons").document(i.toString()).addSnapshotListener { value, error ->
                                if(error == null){
                                    if(value != null && value.exists()){
                                        val model = LessonModel(
                                            value["dersKodu"].toString(),
                                            value["dersAdi"].toString(),
                                            value["donem"].toString(),
                                            value["sinif"].toString(),
                                            value["derslik"].toString(),
                                            value["gun"].toString(),
                                            value["saat"].toString(),
                                            value["ogretimGorevlisi"].toString()
                                        )
                                        lessonListTemp.add(model)
                                        lessonListTemp.sortByDescending { lessonModel ->
                                            lessonModel.dersAdi
                                        }
                                        _lessonList.value = lessonListTemp
                                    }
                                }
                            }
                        }
                        _isRefreshing.value = false
                    }
                }
            }
        }







//        firestore.collection("Students").document(auth.currentUser!!.uid).get().addOnCompleteListener { it ->
//            if(it.isSuccessful){
//                if(it.result["derslerim"] != null){
//                    val dersKoduList = it.result["derslerim"] as List<String>
//                    println("ilk$dersKoduList")
//                    for (i in dersKoduList){
//                        firestore.collection("Lessons").document(i).get().addOnCompleteListener { task ->
//                            val model = LessonModel(
//                                task.result["dersKodu"].toString(),
//                                task.result["dersAdi"].toString(),
//                                task.result["donem"].toString(),
//                                task.result["sinif"].toString(),
//                                task.result["derslik"].toString(),
//                                task.result["gun"].toString(),
//                                task.result["saat"].toString(),
//                                task.result["ogretimGorevlisi"].toString()
//                            )
//                            lessonListTemp.add(model)
//                            lessonListTemp.sortByDescending { lessonModel ->
//                                lessonModel.dersAdi
//                            }
//                        }
//                        println(lessonListTemp)
//                        _lessonList.value = lessonListTemp
//                        _isRefreshing.value = false
//                    }
//                }
//            }
//        }



    }
}