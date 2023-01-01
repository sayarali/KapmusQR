package com.alisayar.kapmusqr.model

data class StudentModel(
    val id: String,
    val studentNo: String,
    val email: String,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val ppUrl: String
): java.io.Serializable
