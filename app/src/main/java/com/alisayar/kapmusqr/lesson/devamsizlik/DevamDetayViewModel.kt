package com.alisayar.kapmusqr.lesson.devamsizlik

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alisayar.kapmusqr.model.LessonModel
import com.alisayar.kapmusqr.model.StudentDevamsizlikModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.ByteArrayOutputStream

class DevamDetayViewModel(private val hafta: String, private val lessonModel: LessonModel): ViewModel() {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storage: FirebaseStorage = FirebaseStorage.getInstance()

    private val _haftaAdi = MutableLiveData<String>()
    val haftaAdi: LiveData<String> get() = _haftaAdi

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> get() = _isRefreshing

    private val _qrUrl = MutableLiveData<String>()
    val qrUrl: LiveData<String> get() = _qrUrl

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage


    var gelenlerOgrNoList = listOf<String>()

    private val _tumOgrencilerList = MutableLiveData<List<StudentDevamsizlikModel>?>()
    val tumOgrencilerList: LiveData<List<StudentDevamsizlikModel>?> get() = _tumOgrencilerList

    private val _gelenOgrencilerList = MutableLiveData<List<StudentDevamsizlikModel>?>()
    val gelenOgrencilerList: LiveData<List<StudentDevamsizlikModel>?> get() = _gelenOgrencilerList

    private val _gelmeyenOgrencilerList = MutableLiveData<List<StudentDevamsizlikModel>?>()
    val gelmeyenOgrencilerList: LiveData<List<StudentDevamsizlikModel>?> get() = _gelmeyenOgrencilerList
    init {
        _haftaAdi.value = hafta
        createQr()
        getQr()

        getGelenOgrenciList()
        gelmisMi()
    }

    private fun gelmisMi() {
        val tempOgrenciList = arrayListOf<StudentDevamsizlikModel>()
        if(gelenlerOgrNoList.isNotEmpty()){
            firestore.collection("Lessons").document(lessonModel.dersKodu).get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    if(task.result["ogrenciler"] != null){
                        val ogrenciList = task.result["ogrenciler"] as List<String>
                        for (i in ogrenciList){
                            if(i in gelenlerOgrNoList){
                                firestore.collection("Students").whereEqualTo("studentNo", i).addSnapshotListener { value, error ->
                                    if(error != null){
                                        _isRefreshing.value = false
                                    } else {
                                        if(value != null && !value.isEmpty) {
                                            val documents = value.documents
                                            for (doc in documents){
                                                val studentModel = StudentDevamsizlikModel(doc["studentNo"].toString(), doc["name"].toString() +" "+ doc["surname"].toString(), true)
                                                tempOgrenciList.add(studentModel)

                                            }
                                            tempOgrenciList.sortedBy {
                                                it.ogrenciNo
                                            }
                                            _tumOgrencilerList.value = tempOgrenciList
                                        }
                                    }
                                }
                            } else {
                                firestore.collection("Students").whereEqualTo("studentNo", i).addSnapshotListener { value, error ->
                                    if(error != null){
                                        _isRefreshing.value = false
                                    } else {
                                        if(value != null && !value.isEmpty) {
                                            val documents = value.documents
                                            for (doc in documents){
                                                val studentModel = StudentDevamsizlikModel(doc["studentNo"].toString(), doc["name"].toString() +" "+ doc["surname"].toString(), false)
                                                tempOgrenciList.add(studentModel)

                                            }
                                            tempOgrenciList.sortedBy {
                                                it.ogrenciNo
                                            }
                                            _tumOgrencilerList.value = tempOgrenciList
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }


    }

    private fun getQr() {
        firestore.collection("Lessons").document(lessonModel.dersKodu).collection("Devam").document(lessonModel.dersAdi + " " + hafta).get().addOnCompleteListener {
            _qrUrl.value = it.result["qrCodeUrl"].toString()
        }
    }

    private fun createQr() {
        firestore.collection("Lessons").document(lessonModel.dersKodu).collection("Devam").document(lessonModel.dersAdi + " " + hafta).get().addOnSuccessListener {
            if(it.exists()){

            } else {
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.encodeBitmap("${lessonModel.dersAdi} $hafta", BarcodeFormat.QR_CODE, 400, 400)
                val reference = storage.reference
                val imageReference =reference.child("qrCodes")
                    .child("${lessonModel.dersAdi} ${hafta}.jpg")
                imageReference.putBytes(bitmapToByteArray(bitmap)).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        imageReference.downloadUrl.addOnCompleteListener {
                            if (it.isSuccessful){
                                val hashMap = hashMapOf<String, Any>()
                                hashMap["qrCodeUrl"] = it.result.toString()
                                hashMap["id"] = lessonModel.dersAdi + " " + hafta
                                hashMap["gelenOgrenciler"] = arrayListOf("00")
                                firestore.collection("Lessons").document(lessonModel.dersKodu).collection("Devam").document(lessonModel.dersAdi + " " + hafta).set(hashMap).addOnCompleteListener {
                                    getQr()
                                    getGelenOgrenciList()
                                    gelmisMi()
                                }
                            }
                        }
                    }
                }.addOnFailureListener {
                    _toastMessage.value = it.localizedMessage
                }
            }
        }
    }
    fun getGelenOgrenciList(){
        try {
            firestore.collection("Lessons").document(lessonModel.dersKodu).collection("Devam").document(lessonModel.dersAdi + " " + hafta).get().addOnSuccessListener { task ->

                if(task.exists()){
                    val tempOgrenciList = arrayListOf<StudentDevamsizlikModel>()
                    firestore.collection("Lessons").document(lessonModel.dersKodu).collection("Devam")
                        .document(lessonModel.dersAdi + " " + hafta)
                        .addSnapshotListener { value, error ->
                        if(error != null){

                        } else {
                            if(value != null && value.exists()){
                                val data = value.data
                                if(data != null){
                                    if(data["gelenOgrenciler"] != null){
                                        gelenlerOgrNoList = data["gelenOgrenciler"] as List<String>
                                        gelmisMi()
                                    }
                                }
                            }
                        }
                    }


                }

            }

        } catch (e: Exception){
            println(e.localizedMessage)
        }
    }



    private fun bitmapToByteArray(bitmap: Bitmap) : ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }
}