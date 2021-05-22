package com.bxtruong.huyproject.notification

import com.bxtruong.huyproject.notification.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InitRetrofit {
    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api by lazy {
            retrofit.create(NotificationAPI::class.java)
        }
    }
}