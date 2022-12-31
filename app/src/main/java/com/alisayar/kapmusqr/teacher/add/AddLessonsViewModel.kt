package com.alisayar.kapmusqr.teacher.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddLessonsViewModel: ViewModel() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val dersKodu = MutableLiveData<String>()
    val dersAdi = MutableLiveData<String>()
    val donem = MutableLiveData<String>()
    val sinif = MutableLiveData<String>()
    val derslik = MutableLiveData<String>()
    val gun = MutableLiveData<String>()
    val saat = MutableLiveData<String>()

    private val _dersKoduError = MutableLiveData<String>()
    val dersKoduError: LiveData<String> get() = _dersKoduError
    private val _dersAdiError = MutableLiveData<String>()
    val dersAdiError: LiveData<String> get() = _dersAdiError
    private val _donemError = MutableLiveData<String>()
    val donemError: LiveData<String> get() = _donemError
    private val _sinifError = MutableLiveData<String>()
    val sinifError: LiveData<String> get() = _sinifError
    private val _derslikError = MutableLiveData<String>()
    val derslikError: LiveData<String> get() = _derslikError
    private val _gunError = MutableLiveData<String>()
    val gunError: LiveData<String> get() = _gunError
    private val _saatError = MutableLiveData<String>()
    val saatError: LiveData<String> get() = _saatError

    private val _saveEvent = MutableLiveData<Boolean>()
    val saveEvent: LiveData<Boolean> get() = _saveEvent

    private val _toastError = MutableLiveData<String>()
    val toastError: LiveData<String> get() = _toastError

    fun save(){
        var isError = false
        if(dersKodu.value.toString() == "" || dersKodu.value == null){
            _dersKoduError.value = "Ders kodu boş bırakılamaz."
            isError = true
        } else
            _dersKoduError.value = ""
        if(dersAdi.value.toString() == "" || dersAdi.value == null){
            _dersAdiError.value = "Ders adı boş bırakılamaz."
            isError = true
        } else
            _dersAdiError.value = ""

        if(donem.value.toString() == "" || donem.value == null){
            _donemError.value = "Ders dönemi boş bırakılamaz."
            isError = true
        } else
            _donemError.value = ""

        if(sinif.value.toString() == "" || sinif.value == null){
            _sinifError.value = "Sınıf boş bırakılamaz."
            isError = true
        } else
            _sinifError.value = ""

        if(derslik.value.toString() == "" || derslik.value == null){
            _derslikError.value = "Derslik boş bırakılamaz."
            isError = true
        } else
            _derslikError.value = ""

        if(gun.value.toString() == "" || gun.value == null){
            _gunError.value = "Ders gününü seçiniz."
            isError = true
        } else
            _gunError.value = ""

        if(saat.value.toString() == "" || saat.value == null){
            _saatError.value = "Ders saatini seçiniz"
            isError = true
        } else
            _saatError.value = ""

        if(!isError){
            try {
                val hashMap = HashMap<String, Any>()
                hashMap["dersKodu"] = dersKodu.value.toString()
                hashMap["dersAdi"] = dersAdi.value.toString()
                hashMap["donem"] = donem.value.toString()
                hashMap["sinif"] = sinif.value.toString()
                hashMap["derslik"] = derslik.value.toString()
                hashMap["ogretimGorevlisi"] = auth.currentUser!!.uid
                hashMap["gun"] = gun.value.toString()
                hashMap["saat"] = saat.value.toString()

                firestore.collection("Lessons").document(dersKodu.value.toString()).set(hashMap).addOnCompleteListener {
                    if (it.isSuccessful){
                        _saveEvent.value = true
                        firestore.collection("Lessons").document(dersKodu.value.toString())
                            .collection("Devam Durumları").document(dersAdi.value.toString() + " 1. Hafta")
                        firestore.collection("Lessons").document(dersKodu.value.toString())
                            .collection("Devam Durumları").document(dersAdi.value.toString() + " 2. Hafta")
                        firestore.collection("Lessons").document(dersKodu.value.toString())
                            .collection("Devam Durumları").document(dersAdi.value.toString() + " 3. Hafta")
                        firestore.collection("Lessons").document(dersKodu.value.toString())
                            .collection("Devam Durumları").document(dersAdi.value.toString() + " 4. Hafta")
                        firestore.collection("Lessons").document(dersKodu.value.toString())
                            .collection("Devam Durumları").document(dersAdi.value.toString() + " 5. Hafta")
                        firestore.collection("Lessons").document(dersKodu.value.toString())
                            .collection("Devam Durumları").document(dersAdi.value.toString() + " 6. Hafta")
                        firestore.collection("Lessons").document(dersKodu.value.toString())
                            .collection("Devam Durumları").document(dersAdi.value.toString() + " 7. Hafta")
                        firestore.collection("Lessons").document(dersKodu.value.toString())
                            .collection("Devam Durumları").document(dersAdi.value.toString() + " 8. Hafta")
                        firestore.collection("Lessons").document(dersKodu.value.toString())
                            .collection("Devam Durumları").document(dersAdi.value.toString() + " 9. Hafta")
                        firestore.collection("Lessons").document(dersKodu.value.toString())
                            .collection("Devam Durumları").document(dersAdi.value.toString() + " 10. Hafta")
                        firestore.collection("Lessons").document(dersKodu.value.toString())
                            .collection("Devam Durumları").document(dersAdi.value.toString() + " 11. Hafta")
                        firestore.collection("Lessons").document(dersKodu.value.toString())
                            .collection("Devam Durumları").document(dersAdi.value.toString() + " 12. Hafta")
                        firestore.collection("Lessons").document(dersKodu.value.toString())
                            .collection("Devam Durumları").document(dersAdi.value.toString() + " 13. Hafta")
                        firestore.collection("Lessons").document(dersKodu.value.toString())
                            .collection("Devam Durumları").document(dersAdi.value.toString() + " 14. Hafta")
                    }
                }
            } catch (e: Exception){
                _toastError.value = e.localizedMessage
            }
        }
    }

    fun addLessonComplete(){
        _saveEvent.value = false
    }

    fun clearToastError() {
        _toastError.value = ""
    }

}