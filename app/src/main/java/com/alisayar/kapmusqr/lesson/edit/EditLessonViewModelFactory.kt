package com.alisayar.kapmusqr.lesson.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alisayar.kapmusqr.model.LessonModel

class EditLessonViewModelFactory(private val lessonModel: LessonModel): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EditLessonViewModel::class.java))
            return EditLessonViewModel(lessonModel) as T
        throw java.lang.IllegalArgumentException("Bilinmeyen EditLessonViewModel")
    }

}