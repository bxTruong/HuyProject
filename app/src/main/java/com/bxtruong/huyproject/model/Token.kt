package com.bxtruong.huyproject.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Token(
    val token: String = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "token" to token
        )
    }
}