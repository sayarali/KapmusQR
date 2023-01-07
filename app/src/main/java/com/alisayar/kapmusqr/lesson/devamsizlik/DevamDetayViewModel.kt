package com.alisayar.kapmusqr.lesson.devamsizlik

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alisayar.kapmusqr.model.LessonModel
import com.alisayar.kapmusqr.model.StudentDevamsizlikModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.github.g0dkar.qrcode.QRCode
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.text.Format
import java.util.*

class DevamDetayViewModel(private val hafta: String, private val lessonModel: LessonModel): ViewModel() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
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



    private val _ogretmenMi = MutableLiveData<Boolean>()
    val ogretmenMi: LiveData<Boolean> get() = _ogretmenMi


    init {
        _haftaAdi.value = hafta
        kullaniciOgretmenMi()
        createQr()
        getQr()
        getGelenOgrenciList()
        //getOgrenciList()
    }

    fun getOgrenciList() {
        val tempOgrenciList = arrayListOf<StudentDevamsizlikModel>()
        firestore.collection("Lessons").document(lessonModel.dersKodu)
            .collection("Devam")
            .document(lessonModel.dersAdi + " " + hafta)
            .addSnapshotListener { value1, error1 ->
                if(error1 == null){
                    if(value1 != null && value1.exists()){
                        if(value1["gelenOgrenciler"] != null){
                            val gelenOgrencilerList = value1["gelenOgrenciler"] as List<*>
                            println("gelen: $gelenOgrencilerList")
                            firestore.collection("Lessons").document(lessonModel.dersKodu).addSnapshotListener { value, error ->
                                if(error == null){
                                    if(value != null && value.exists()){
                                        if(value["ogrenciler"] != null){
                                            val tumOgrencilerListe = value["ogrenciler"] as List<*>
                                            println("tüm: $tumOgrencilerListe")
                                            tempOgrenciList.clear()
                                            for (i in tumOgrencilerListe){
                                                if(i in gelenOgrencilerList){
                                                    firestore.collection("Students").whereEqualTo("studentNo", i).addSnapshotListener { valueA, errorA ->
                                                        if(errorA == null){
                                                            if(valueA != null && !valueA.isEmpty){
                                                                val doc = valueA.documents[0]
                                                                val studentModel = StudentDevamsizlikModel(
                                                                    doc["id"].toString(),
                                                                    doc["studentNo"].toString(),
                                                                    doc["name"].toString() +" "+ doc["surname"].toString(),
                                                                    true)
                                                                tempOgrenciList.add(studentModel)
//                                                                tempOgrenciList.sortBy {
//                                                                    it.ogrenciNo
//                                                                }
                                                                _tumOgrencilerList.value = tempOgrenciList
                                                                println(tumOgrencilerList.value)
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    firestore.collection("Students").whereEqualTo("studentNo", i).addSnapshotListener { valueA, errorA ->
                                                        if(errorA == null){
                                                            if(valueA != null && !valueA.isEmpty){
                                                                val doc = valueA.documents[0]
                                                                val studentModel = StudentDevamsizlikModel(
                                                                    doc["id"].toString(),
                                                                    doc["studentNo"].toString(),
                                                                    doc["name"].toString() +" "+ doc["surname"].toString(),
                                                                    false)

                                                                tempOgrenciList.add(studentModel)
//                                                                tempOgrenciList.sortBy {
//                                                                    it.ogrenciNo
//                                                                }
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

                    }
                }
        }


    }

    fun ggetOgrenciList() {
        val tempOgrenciList = arrayListOf<StudentDevamsizlikModel>()
        firestore.collection("Lessons").document(lessonModel.dersKodu).collection("Devam").document(lessonModel.dersAdi + " " + hafta).get().addOnSuccessListener { task ->
            if(task.exists()){
                firestore.collection("Lessons").document(lessonModel.dersKodu).collection("Devam")
                    .document(lessonModel.dersAdi + " " + hafta)
                    .addSnapshotListener { value, error ->
                        if(error != null){} else {
                            if(value != null && value.exists()){
                                val data = value.data
                                if(data != null){
                                    if(data["gelenOgrenciler"] != null){
                                        val gelenlerOgrNoList = data["gelenOgrenciler"] as List<*>
                                        firestore.collection("Lessons").document(lessonModel.dersKodu).get().addOnCompleteListener { task ->
                                            if(task.isSuccessful){
                                                if(task.result["ogrenciler"] != null){
                                                    val ogrenciList = task.result["ogrenciler"] as List<*>
                                                    for (i in ogrenciList){
                                                        if(i in gelenlerOgrNoList){
                                                            firestore.collection("Students").whereEqualTo("studentNo", i).addSnapshotListener { value, error ->
                                                                println("gelenler$i")
                                                                if(error != null){
                                                                    _isRefreshing.value = false
                                                                } else {
                                                                    if(value != null && !value.isEmpty) {
                                                                        val documents = value.documents
                                                                        for (doc in documents){
                                                                            val studentModel = StudentDevamsizlikModel(doc["id"].toString(), doc["studentNo"].toString(), doc["name"].toString() +" "+ doc["surname"].toString(), true)
                                                                            tempOgrenciList.add(studentModel)

                                                                        }
                                                                        tempOgrenciList.sortBy {
                                                                            it.ogrenciNo
                                                                        }
                                                                        _tumOgrencilerList.value = tempOgrenciList
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            println("gelmeyenler$i")
                                                            firestore.collection("Students").whereEqualTo("studentNo", i).addSnapshotListener { value, error ->
                                                                if(error != null){
                                                                    _isRefreshing.value = false
                                                                } else {
                                                                    if(value != null && !value.isEmpty) {
                                                                        val documents = value.documents
                                                                        for (doc in documents){
                                                                            val studentModel = StudentDevamsizlikModel(doc["id"].toString(), doc["studentNo"].toString(), doc["name"].toString() +" "+ doc["surname"].toString(), false)
                                                                            tempOgrenciList.add(studentModel)

                                                                        }
                                                                        tempOgrenciList.sortBy {
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
                            }
                        }
                    }


            }

        }


    }

    private fun gelmisMi() {
        val tempOgrenciList = arrayListOf<StudentDevamsizlikModel>()
        if(gelenlerOgrNoList.isNotEmpty()){
            firestore.collection("Lessons").document(lessonModel.dersKodu).get(Source.SERVER).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    if(task.result["ogrenciler"] != null){
                        val ogrenciList = task.result["ogrenciler"] as List<*>

                        for (i in ogrenciList){

                            if(i in gelenlerOgrNoList){
                                firestore.collection("Students").whereEqualTo("studentNo", i).addSnapshotListener { value, error ->
                                    println("gelenler$i")
                                    if(error != null){
                                        _isRefreshing.value = false
                                    } else {
                                        if(value != null && !value.isEmpty) {
                                            val documents = value.documents
                                            for (doc in documents){
                                                val studentModel = StudentDevamsizlikModel(doc["id"].toString(), doc["studentNo"].toString(), doc["name"].toString() +" "+ doc["surname"].toString(), true)
                                                tempOgrenciList.add(studentModel)

                                            }
                                            tempOgrenciList.sortBy {
                                                it.ogrenciNo
                                            }
                                            _tumOgrencilerList.value = tempOgrenciList
                                        }
                                    }
                                }
                            } else {
                                println("gelmeyenler$i")
                                firestore.collection("Students").whereEqualTo("studentNo", i).addSnapshotListener { value, error ->
                                    if(error != null){
                                        _isRefreshing.value = false
                                    } else {
                                        if(value != null && !value.isEmpty) {
                                            val documents = value.documents
                                            for (doc in documents){
                                                val studentModel = StudentDevamsizlikModel(doc["id"].toString(), doc["studentNo"].toString(), doc["name"].toString() +" "+ doc["surname"].toString(), false)
                                                tempOgrenciList.add(studentModel)

                                            }
                                            tempOgrenciList.sortBy {
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
            if(!it.exists()){
                val qrByteArray = QRCode("${lessonModel.dersAdi} $hafta").render().getBytes()
                val reference = storage.reference
                val imageReference =reference.child("qrCodes")
                    .child("${lessonModel.dersAdi} ${hafta}.jpg")
                imageReference.putBytes(qrByteArray).addOnCompleteListener { task ->
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


    private fun getGelenOgrenciList(){
        try {
            firestore.collection("Lessons").document(lessonModel.dersKodu).collection("Devam").document(lessonModel.dersAdi + " " + hafta).get(Source.SERVER).addOnSuccessListener { task ->

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

    private fun kullaniciOgretmenMi(){
        firestore.collection("Teachers").whereEqualTo("id", auth.currentUser!!.uid).get().addOnSuccessListener {
            _ogretmenMi.value = !it.isEmpty
        }
    }

}