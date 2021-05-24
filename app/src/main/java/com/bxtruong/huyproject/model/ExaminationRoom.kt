package com.bxtruong.huyproject.model
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ExaminationRoom(
    var id:String="",
    var nameRoom: String = "",
    var date: String = "",
    var timeStart: String = "",
    var timeFinish: String = "",
    var teacher1: String = "",
    var teacher2: String = "",
    var exam:String=""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "nameRoom" to nameRoom,
            "date" to date,
            "timeStart" to timeStart,
            "timeFinish" to timeFinish,
            "teacher1" to teacher1,
            "teacher2" to teacher2,
            "exam" to exam
        )
    }
}
