package com.alisayar.kapmusqr.teacher.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TeacherProfileViewModelFactory(private val teacherId: String): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TeacherProfileViewModel::class.java)){
            return TeacherProfileViewModel(teacherId) as T
        }
        throw java.lang.IllegalArgumentException("Bilinmeyen TeacherViewModel")
    }
}