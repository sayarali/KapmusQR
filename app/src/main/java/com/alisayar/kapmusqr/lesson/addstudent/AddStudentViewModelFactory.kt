package com.alisayar.kapmusqr.lesson.addstudent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alisayar.kapmusqr.model.LessonModel

class AddStudentViewModelFactory(private val lessonModel: LessonModel): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddStudentViewModel::class.java))
            return AddStudentViewModel(lessonModel) as T
        throw IllegalArgumentException("Bilinmeyen ViewModel Class")
    }
}