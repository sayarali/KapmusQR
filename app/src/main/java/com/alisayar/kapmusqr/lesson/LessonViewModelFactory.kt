package com.alisayar.kapmusqr.lesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alisayar.kapmusqr.model.LessonModel

class LessonViewModelFactory(private val lessonModel: LessonModel): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LessonViewModel::class.java)){
            return LessonViewModel(lessonModel) as T
        }
        throw IllegalArgumentException("Bilinmeyen LessonViewModel class")
    }
}