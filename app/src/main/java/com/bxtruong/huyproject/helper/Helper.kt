package com.bxtruong.huyproject.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.bxtruong.huyproject.model.User

object Helper {
    private const val ACCOUNT = "ACCOUNT"
    private const val USER_NAME = "USER_NAME"
    private const val PASS_WORD = "PASS_WORD"
    private const val TEACHER = "TEACHER"
    private const val FULL_NAME = "FULL_NAME"

    fun writeUser(activity: Activity, user: User) {
        val sharedPreferences: SharedPreferences =
            activity.getSharedPreferences(ACCOUNT, 0)
        val editor = sharedPreferences.edit()
        editor?.putString(ACCOUNT, user.username)
        editor?.putString(PASS_WORD, user.password)
        editor?.putString(FULL_NAME, user.full_name)
        editor?.putBoolean(TEACHER, user.teacher)
        editor?.commit()
    }

    fun readUser(activity: Activity): Boolean {
        val sharedPreferences =
            activity.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE)
        val username = sharedPreferences?.getString(USER_NAME, "")
        val password = sharedPreferences?.getString(PASS_WORD, "")
        if (username == "" || password == "") {
            return false
        }
        return true
    }

    fun checkTeacher(activity: Activity): Boolean {
        val sharedPreferences =
            activity.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(TEACHER, false)
    }
}