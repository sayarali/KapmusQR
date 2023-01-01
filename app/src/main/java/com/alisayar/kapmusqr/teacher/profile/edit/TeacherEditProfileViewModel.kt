package com.alisayar.kapmusqr.teacher.profile.edit

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alisayar.kapmusqr.model.TeacherModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class TeacherEditProfileViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    val newPersonalNo = MutableLiveData<String>()
    val newName = MutableLiveData<String>()
    val newSurname = MutableLiveData<String>()
    val newRoomNumber = MutableLiveData<String>()
    val newPhoneNumber = MutableLiveData<String>()
    private val newPpUrlByteArray = MutableLiveData<ByteArray?>()

    private val _ogretimGorevlisi = MutableLiveData<TeacherModel>()
    val ogretimGorevlisi: LiveData<TeacherModel> get() = _ogretimGorevlisi

    private val _toastError = MutableLiveData<String>()
    val toastError: LiveData<String> get() = _toastError
    private val _updatedProfileEvent = MutableLiveData<Boolean>()
    val updatedProfileEvent: LiveData<Boolean> get() = _updatedProfileEvent


    init {
        getTeacherData()
    }

    fun saveButton(){

        try{
            firestore.collection("Teachers").document(auth.currentUser!!.uid).update(
                "personalNo", newPersonalNo.value,
                "name", newName.value,
                "surname", newSurname.value,
                "roomNumber", newRoomNumber.value,
                "phoneNumber", newPhoneNumber.value
            ).addOnCompleteListener {
                if(it.isSuccessful){
                    _updatedProfileEvent.value = true
                }
            }
            auth.currentUser?.let{
                val reference = storage.reference
                val imageReference =reference.child("profilephotos")
                    .child("${auth.currentUser!!.uid}.jpg")
                newPpUrlByteArray.value.let {
                    imageReference.putBytes(newPpUrlByteArray.value!!).addOnCompleteListener {
                        if(it.isSuccessful){
                            imageReference.downloadUrl.addOnCompleteListener {
                                if (it.isSuccessful){
                                    val ppUrl = it.result.toString()
                                    firestore.collection("Teachers").document(auth.currentUser!!.uid).update("ppUrl", ppUrl)
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

    private fun getTeacherData() {
        try {
            firestore.collection("Teachers").document(auth.currentUser!!.uid).get().addOnCompleteListener {
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

                }
            }.addOnFailureListener {
                _toastError.value = it.localizedMessage
            }
        } catch (e: Exception){
            println(e.localizedMessage)
        }
    }

    fun chosenImage(bitmap: Bitmap){
        newPpUrlByteArray.value = getImageByteArray(bitmap)
    }

    private fun getImageByteArray(bitmap: Bitmap) : ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }


}