package com.alisayar.kapmusqr.lesson.devamsizlik

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alisayar.kapmusqr.model.LessonModel

class DevamDetayViewModelFactory(private val hafta: String, private val lessonModel: LessonModel): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DevamDetayViewModel::class.java))
            return DevamDetayViewModel(hafta, lessonModel) as T
        throw IllegalArgumentException()
    }


}