package com.alisayar.kapmusqr.lesson.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alisayar.kapmusqr.model.LessonModel
import com.google.firebase.firestore.FirebaseFirestore

class EditLessonViewModel(private val lessonModel: LessonModel): ViewModel() {
   private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val newDersAdi = MutableLiveData<String>()
    val newDonem = MutableLiveData<String>()
    val newSinif = MutableLiveData<String>()
    val newDerslik = MutableLiveData<String>()
    val newGun = MutableLiveData<String>()
    val newSaat = MutableLiveData<String>()

    private val _lesson = MutableLiveData<LessonModel>()
    val lesson: LiveData<LessonModel> get() = _lesson
    private val _saveEvent = MutableLiveData<Boolean>()
    val saveEvent: LiveData<Boolean> get() = _saveEvent
    init {
        _lesson.value = lessonModel
    }


    fun save() {
        firestore.collection("Lessons").document(lessonModel.dersKodu).update(
            "dersAdi", newDersAdi.value,
            "donem", newDonem.value,
            "sinif", newSinif.value,
            "gun", newGun.value,
            "saat", newSaat.value,
            "derslik", newDerslik.value
        ).addOnCompleteListener {
            _saveEvent.value = true
        }
    }


}