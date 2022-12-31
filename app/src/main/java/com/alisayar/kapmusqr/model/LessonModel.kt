package com.alisayar.kapmusqr.model

data class LessonModel(
    val dersKodu: String,
    val dersAdi: String,
    val donem: String,
    val sinif: String,
    val derslik: String,
    val gun: String,
    val saat: String,
    val ogretimGorevlisiId: String
): java.io.Serializable
