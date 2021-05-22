package com.bxtruong.huyproject.model
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ExaminationRoom(
    var nameRoom: String = "",
    var date: String = "",
    var timeStart: String = "",
    var timeFinish: String = "",
    var teacher1: String = "",
    var teacher2: String = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "nameRoom" to nameRoom,
            "date" to date,
            "timeStart" to timeStart,
            "timeFinish" to timeFinish,
            "teacher1" to teacher1,
            "teacher2" to teacher2
        )
    }
}
