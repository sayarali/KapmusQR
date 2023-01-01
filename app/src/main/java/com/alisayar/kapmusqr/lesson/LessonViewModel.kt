package com.alisayar.kapmusqr.lesson

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alisayar.kapmusqr.model.LessonModel
import com.alisayar.kapmusqr.model.TeacherModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class LessonViewModel(private val lessonModel: LessonModel): ViewModel() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()



    private val _toastError = MutableLiveData<String>()
    val toastError: LiveData<String> get() = _toastError

    private val _popUpEvent = MutableLiveData<Boolean>()
    val popUpEvent: LiveData<Boolean> get() = _popUpEvent

    private val _dersAdi = MutableLiveData<String>()
    val dersAdi: LiveData<String> get() = _dersAdi
    private val _dersKodu = MutableLiveData<String>()
    val dersKodu: LiveData<String> get() = _dersKodu
    private val _donem = MutableLiveData<String>()
    val donem: LiveData<String> get() = _donem
    private val _sinif = MutableLiveData<String>()
    val sinif: LiveData<String> get() = _sinif
    private val _derslik = MutableLiveData<String>()
    val derslik: LiveData<String> get() = _derslik
    private val _gun = MutableLiveData<String>()
    val gun: LiveData<String> get() = _gun
    private val _saat = MutableLiveData<String>()
    val saat: LiveData<String> get() = _saat
    private val _ogretimGorevlisi = MutableLiveData<TeacherModel>()
    val ogretimGorevlisi: LiveData<TeacherModel> get() = _ogretimGorevlisi
    private val _ogretimGorevlisiAd = MutableLiveData<String>()
    val ogretimGorevlisiAd: LiveData<String> get() = _ogretimGorevlisiAd


    private val _ogretmenMi = MutableLiveData<Boolean>()
    val ogretmenMi: LiveData<Boolean> get() = _ogretmenMi

    val studentNo = MutableLiveData<String>()
    init {
        kullaniciOgretmenMi()
        getTeacherData(lessonModel.ogretimGorevlisiId)
        _dersAdi.value = lessonModel.dersAdi
        _dersKodu.value = lessonModel.dersKodu
        _donem.value = lessonModel.donem
        _sinif.value = lessonModel.sinif
        _derslik.value = lessonModel.derslik
        _gun.value = lessonModel.gun
        _saat.value = lessonModel.saat

    }

    private fun getTeacherData(teacherId: String){
        try {
            firestore.collection("Teachers").document(teacherId).get().addOnCompleteListener {
                if(it.isSuccessful){
                    val teacherModel = TeacherModel(
                        it.result["id"].toString(),
                        it.result["personalNo"].toString(),
                        it.result["email"].toString(),
                        it.result["name"].toString(),
                        it.result["surname"].toString(),
                        it.result["roomNumber"].toString(),
                        it.result["phoneNumber"].toString(),
                        it.result["ppUrl"].toString(),
                    )
                    _ogretimGorevlisi.value = teacherModel
                    _ogretimGorevlisiAd.value = teacherModel.name + " " + teacherModel.surname

                }
            }.addOnFailureListener {
                _toastError.value = it.localizedMessage
            }
        } catch (e: Exception){
            println(e.localizedMessage)
        }
    }

    fun addStudent(studentNo: String){
        try {
            firestore.collection("Lessons").document(lessonModel.dersKodu).update("ogrenciler", FieldValue.arrayUnion(studentNo))
            firestore.collection("Students").whereEqualTo("studentNo", studentNo).get().addOnCompleteListener {
                if(it.isSuccessful){
                    val document = it.result.documents
                    var id = ""
                    for(i in document){
                        id = i.id
                    }
                    if (id.isNotEmpty()){
                        firestore.collection("Students").document(id).update("derslerim",FieldValue.arrayUnion(lessonModel.dersKodu))
                    }
                }
            }

        }catch (e: Exception){
            println(e.localizedMessage)
        }
    }


    fun deleteLesson(){
        firestore.collection("Lessons").document(lessonModel.dersKodu).delete().addOnCompleteListener {
            if (it.isSuccessful){
                _toastError.value = "Ders silindi"
                _popUpEvent.value = true
            }
        }.addOnFailureListener {
            _toastError.value = it.localizedMessage
        }
    }

    private fun kullaniciOgretmenMi(){
        firestore.collection("Teachers").whereEqualTo("id", auth.currentUser!!.uid).get().addOnSuccessListener {
            _ogretmenMi.value = !it.isEmpty
        }
    }

    fun completePopUp(){
        _popUpEvent.value = false
    }

}