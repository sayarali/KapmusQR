package com.alisayar.kapmusqr.lesson.addstudent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alisayar.kapmusqr.model.LessonModel
import com.google.firebase.firestore.FirebaseFirestore

class AddStudentViewModel(lessonModel: LessonModel): ViewModel() {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()



    init {



    }


}