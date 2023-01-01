package com.alisayar.kapmusqr.student.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StudentProfileViewModelFactory(private val studentId: String): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StudentProfileViewModel::class.java))
            return StudentProfileViewModel(studentId) as T
        throw java.lang.IllegalArgumentException("Bilinmeyen StudentProfileViewModel")
    }
}