package com.bxtruong.huyproject.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.bxtruong.huyproject.model.User

object Helper {
    private const val ACCOUNT = "ACCOUNT"
    private const val FACULTY = "FACULTY"
    private const val TEACH = "TEACH"
    private const val TEACHER = "TEACHER"
    private const val FULL_NAME = "FULL_NAME"

    fun writeUser(activity: Activity, user: User) {
        val sharedPreferences: SharedPreferences =
            activity.getSharedPreferences(ACCOUNT, 0)
        val editor = sharedPreferences.edit()
        editor?.putString(FACULTY, user.faculty)
        editor?.putString(TEACH, user.teach)
        editor?.putString(FULL_NAME, user.full_name)
        editor?.putBoolean(TEACHER, user.teacher)
        editor?.commit()
    }

    fun readUser(activity: Activity): User {
        val sharedPreferences =
            activity.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE)
        val faculty = sharedPreferences?.getString(FACULTY, "")
        val teach = sharedPreferences?.getString(TEACH, "")
        val full_name = sharedPreferences?.getString(FULL_NAME, "")
        val teacher = sharedPreferences.getBoolean(TEACHER, false)
        return User("", "", full_name!!, teach!!, faculty!!, teacher)
    }

    fun checkTeacher(activity: Activity): Boolean {
        val sharedPreferences =
            activity.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(TEACHER, false)
    }
}