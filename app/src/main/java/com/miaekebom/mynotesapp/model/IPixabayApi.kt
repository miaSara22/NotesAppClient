package com.miaekebom.mynotesapp.model

import com.miaekebom.mynotesapp.model.data.ApiResponseHitsList
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface IPixabayApi {

    @GET("?key=30490454-92ce9aaffa0ffb834e62cbb6d&image_type=photo&pretty=true&q=profile")
    fun getImages(): Call<ApiResponseHitsList.ApiResponse>

    companion object {

        private const val BASE_URL = "https://pixabay.com/api/"
        fun create(): IPixabayApi {
            val retrofit = Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(IPixabayApi::class.java)

        }
    }
}