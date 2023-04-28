package com.miaekebom.mynotesapp.model

import com.miaekebom.mynotesapp.model.data.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface IRetrofitApi {

    companion object {

        fun create(authToken: String): IRetrofitApi {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val httpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)

                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $authToken")
                        .build()
                    chain.proceed(request)
                }
                .build()

            val retrofit = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .baseUrl("http://192.168.1.103:9000")
            .build()

        return retrofit.create(IRetrofitApi::class.java)
    }
}

    //user
    @GET("/getAllUsers")
    @Headers("Content-Type: application/json")
    suspend fun getAllUsers(@Header("Authorization") authToken: String): List<User>

    @POST("/loginUser")
    @Headers("Content-Type: application/json")
    suspend fun loginUser(@Body loginRequest: LoginRequest, @Header("Authorization") authToken: String? = null): LoginResponse

    @POST("/saveUser")
    @Headers("Content-Type: application/json")
    suspend fun saveUser(@Body user: User, @Header("Authorization") authToken: String? = null): com.miaekebom.mynotesapp.model.data.ResultResponse

    @PUT("/deleteUserImage/{userId}")
    @Headers("Content-Type: application/json")
    suspend fun deleteUserImage(@Path("userId") userId: Int, @Header("Authorization") authToken: String): ResultResponse

    @PUT("/updateUserImage/{userId}")
    @Headers("Content-Type: application/json")
    suspend fun updateUserImage(@Path("userId") userId: Int, @Body image: String, @Header("Authorization") authToken: String): ResultResponse

    @GET("/getUserImage/{userId}")
    @Headers("Content-Type: application/json")
    suspend fun getUserImage(@Path("userId") userId: Int, @Body image: String, @Header("Authorization") authToken: String): ResultResponse



    @DELETE("/deleteUser")
    @Headers("Content-Type: application/json")
    suspend fun deleteUser(@Body user: User, @Header("Authorization") authToken: String): Response<Unit>

    //list
    @GET("/getLists/{ownerId}")
    @Headers("Content-Type: application/json")
    suspend fun getLists(@Path("ownerId") ownerId: Int, @Header("Authorization") authToken: String): Response<List<com.miaekebom.mynotesapp.model.data.List>>

    @POST("/saveList")
    @Headers("Content-Type: application/json")
    suspend fun saveList(@Body list: com.miaekebom.mynotesapp.model.data.List, @Header("Authorization") authToken: String): ResultResponse

    @POST("/deleteList")
    @Headers("Content-Type: application/json")
    suspend fun deleteList(@Body list: com.miaekebom.mynotesapp.model.data.List, @Header("Authorization") authToken: String): ResultResponse

    @POST("/updateList")
    @Headers("Content-Type: application/json")
    suspend fun updateList(@Body list: com.miaekebom.mynotesapp.model.data.List, @Header("Authorization") authToken: String): Response<Unit>

    //note
    @POST("/saveNote")
    @Headers("Content-Type: application/json")
    suspend fun saveNote(@Body note: Note, @Header("Authorization") authToken: String): ResultResponse

    @POST("/deleteNote")
    @Headers("Content-Type: application/json")
    suspend fun deleteNote(@Body note: Note, @Header("Authorization") authToken: String): ResultResponse

    @POST("/updateNote")
    @Headers("Content-Type: application/json")
    suspend fun updateNote(@Body note: Note, @Header("Authorization") authToken: String): ResultResponse

    @GET("/getNotes/{ownerId}")
    @Headers("Content-Type: application/json")
    suspend fun getNotes(@Path("ownerId") ownerId: Int, @Header("Authorization") authToken: String): Response<List<Note>>

}