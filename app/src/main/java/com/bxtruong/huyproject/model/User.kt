package com.bxtruong.huyproject.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val username: String = "",
    val password: String = "",
    val full_name: String = "",
    val teach: String = "",
    val faculty: String = "",
    val teacher: Boolean = true
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "username" to username,
            "password" to password,
            "full_name" to full_name,
            "teach" to teach,
            "faculty" to faculty,
            "teacher" to teacher
        )
    }
}