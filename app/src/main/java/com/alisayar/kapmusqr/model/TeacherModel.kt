package com.alisayar.kapmusqr.model

data class TeacherModel(
    val id: String,
    val personalNo: String,
    val email: String,
    val name: String,
    val surname: String,
    val roomNumber: String,
    val phoneNumber: String,
    var ppUrl: String
)
