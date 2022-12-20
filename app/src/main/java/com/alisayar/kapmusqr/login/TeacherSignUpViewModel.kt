package com.alisayar.kapmusqr.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


class TeacherSignUpViewModel: ViewModel() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val personalNo = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val surname = MutableLiveData<String>()
    val roomNumber = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val passwordCheck = MutableLiveData<String>()

    private val _personalNoError = MutableLiveData<String>()
    val personalNoError: LiveData<String> get() = _personalNoError
    private val _emailError = MutableLiveData<String>()
    val emailError: LiveData<String> get() = _emailError
    private val _nameError = MutableLiveData<String>()
    val nameError: LiveData<String> get() = _nameError
    private val _surnameError = MutableLiveData<String>()
    val surnameError: LiveData<String> get() = _surnameError
    private val _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String> get() = _passwordError
    private val _passwordCheckError = MutableLiveData<String>()
    val passwordCheckError: LiveData<String> get() = _passwordCheckError

    private val _toastError = MutableLiveData<String>()
    val toastError: LiveData<String> get() = _toastError

    private val _successRegisterEvent = MutableLiveData<Boolean>()
    val successRegisterEvent: LiveData<Boolean> get() = _successRegisterEvent

    fun createTeacher(){
        var isError = false
        if(personalNo.value.toString() == "" || personalNo.value == null){
            _personalNoError.value = "Personel numarası boş bırakılamaz."
            isError = true
        } else
            _personalNoError.value = ""
        if(email.value.toString() == "" || email.value == null){
            _emailError.value = "E-posta boş bırakılamaz."
            isError = true
        } else
            _emailError.value = ""
        if(name.value.toString() == "" || name.value == null){
            _nameError.value = "İsim boş bırakılamaz."
            isError = true
        } else
            _nameError.value = ""
        if(surname.value.toString() == "" || surname.value == null){
            _surnameError.value = "Soyisim boş bırakılamaz."
            isError = true
        } else
            _surnameError.value = ""
        if(password.value.toString() == "" || password.value == null){
            _passwordError.value = "Parola boş bırakılamaz."
            isError = true
        } else
            _passwordError.value = ""
        if(passwordCheck.value.toString() == "" || passwordCheck.value == null){
            _passwordCheckError.value = "Parolayı tekrar giriniz."
            isError = true
        }
        else if(password.value.toString() != passwordCheck.value.toString()){
            _passwordCheckError.value = "Parolalar eşleşmiyor."
            isError = true
        } else
            _passwordCheckError.value = ""

        if(roomNumber.value.toString() == "" || roomNumber.value == null){
            roomNumber.value = ""
        }
        if(phoneNumber.value.toString() == "" || phoneNumber.value == null){
            phoneNumber.value = ""
        }

        if(!isError){
            try {
                auth.createUserWithEmailAndPassword(email.value.toString(), password.value.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        createUserCollection(auth.currentUser!!)
                    }
                }.addOnFailureListener {
                    _toastError.value = it.localizedMessage
                }
            } catch (e: Exception){
                println(e)
            }
        }
    }

    private fun createUserCollection(user: FirebaseUser){

        try {
            val teacherHashMap = HashMap<String, Any>()
            teacherHashMap["id"] = user.uid
            teacherHashMap["personalNo"] = personalNo.value.toString()
            teacherHashMap["email"] = email.value.toString()
            teacherHashMap["name"] = name.value.toString()
            teacherHashMap["surname"] = surname.value.toString()
            teacherHashMap["roomNumber"] = roomNumber.value.toString()
            teacherHashMap["phoneNumber"] = phoneNumber.value.toString()

            firestore.collection("Teachers").document(user.uid).set(teacherHashMap).addOnCompleteListener {
                _successRegisterEvent.value = true
            }.addOnFailureListener {
                _toastError.value = it.localizedMessage
            }


        } catch (e: Exception){
            println(e)
        }
    }
    fun registerEventComplete(){
        _successRegisterEvent.value = false
    }

}