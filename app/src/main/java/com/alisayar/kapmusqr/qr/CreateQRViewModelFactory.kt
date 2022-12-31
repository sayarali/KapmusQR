package com.alisayar.kapmusqr.qr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alisayar.kapmusqr.model.LessonModel

class CreateQRViewModelFactory(private val lessonModel: LessonModel): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CreateQRViewModel::class.java)){
            return CreateQRViewModel(lessonModel) as T
        }
        throw IllegalArgumentException("Bilinmeyen CreateQr ViewModel Sınıfı")
    }

}