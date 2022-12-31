package com.alisayar.kapmusqr.qr

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alisayar.kapmusqr.model.LessonModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.BarcodeFormat
import com.google.zxing.pdf417.encoder.BarcodeMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.encoder.Encoder
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.ByteArrayOutputStream

class CreateQRViewModel(private val lessonModel: LessonModel): ViewModel() {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()


    val hafta = MutableLiveData<String>()

    private val _haftaError = MutableLiveData<String>()
    val haftaError: LiveData<String> get() = _haftaError

    private val _bitmap = MutableLiveData<Bitmap>()
    val bitmap: LiveData<Bitmap> get() = _bitmap

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage
    init {

    }

    fun create(){
        var isError = false
        if(hafta.value.toString() == "" || hafta.value == null){
            _haftaError.value = "Lütfen hafta seçiniz."
            isError = true
        } else
            _haftaError.value = ""

        if(!isError){
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap("${lessonModel.dersAdi} ${hafta.value}", BarcodeFormat.QR_CODE, 400, 400)
            _bitmap.value = bitmap
            val reference = storage.reference
            val imageReference =reference.child("qrCodes")
                .child("${lessonModel.dersAdi} ${hafta.value}.jpg")
            imageReference.putBytes(bitmapToByteArray(bitmap)).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    imageReference.downloadUrl.addOnCompleteListener {
                        if (it.isSuccessful){
                            val hashMap = hashMapOf<String, Any>()
                            hashMap["qrCodeUrl"] = it.result.toString()
                            hashMap["id"] = lessonModel.dersAdi + " " + hafta.value
                            firestore.collection("Lessons").document(lessonModel.dersKodu).collection("Devam Durumları").document(lessonModel.dersAdi + " " + hafta.value).set(hashMap)
                        }
                    }
                }
            }.addOnFailureListener {
                _toastMessage.value = it.localizedMessage
            }
            }
    }

    private fun bitmapToByteArray(bitmap: Bitmap) : ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }
}