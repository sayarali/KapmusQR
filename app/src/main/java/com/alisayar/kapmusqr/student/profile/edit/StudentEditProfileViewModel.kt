package com.alisayar.kapmusqr.student.profile.edit

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alisayar.kapmusqr.model.StudentModel
import com.alisayar.kapmusqr.model.TeacherModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class StudentEditProfileViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    val newStudentNo = MutableLiveData<String>()
    val newName = MutableLiveData<String>()
    val newSurname = MutableLiveData<String>()
    val newPhoneNumber = MutableLiveData<String>()
    private val newPpUrlByteArray = MutableLiveData<ByteArray?>()

    private val _ogrenci = MutableLiveData<StudentModel>()
    val ogrenci: LiveData<StudentModel> get() = _ogrenci

    private val _toastError = MutableLiveData<String>()
    val toastError: LiveData<String> get() = _toastError
    private val _updatedProfileEvent = MutableLiveData<Boolean>()
    val updatedProfileEvent: LiveData<Boolean> get() = _updatedProfileEvent

    init {
        getStudentData(auth.currentUser!!.uid)
    }

    fun saveButton(){

        try{
            firestore.collection("Students").document(auth.currentUser!!.uid).update(
                "studentNo", newStudentNo.value,
                "name", newName.value,
                "surname", newSurname.value,
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
                                    firestore.collection("Students").document(auth.currentUser!!.uid).update("ppUrl", ppUrl)
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


    private fun getStudentData(studentId: String){
        try {
            firestore.collection("Students").document(studentId).get().addOnCompleteListener {
                if(it.isSuccessful){
                    val studentModel = StudentModel(
                        it.result["id"].toString(),
                        it.result["studentNo"].toString(),
                        it.result["email"].toString(),
                        it.result["name"].toString(),
                        it.result["surname"].toString(),
                        it.result["phoneNumber"].toString(),
                        it.result["ppUrl"].toString(),
                    )
                    _ogrenci.value = studentModel

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